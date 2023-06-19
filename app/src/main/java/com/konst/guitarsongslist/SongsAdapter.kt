package com.konst.guitarsongslist

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.konst.guitarsongslist.data.AppDatabase
import com.konst.guitarsongslist.data.Song
import com.konst.guitarsongslist.data.SongDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class SongsAdapter(
    private val songDao: SongDao,
    private val coroutineScope: CoroutineScope,
) : RecyclerView.Adapter<SongsAdapter.SongViewHolder>() {
    private var songs: ArrayList<Song>

    init {
        runBlocking {
            songs = ArrayList()
            songs.addAll(songDao.getAllSongs())
        }

        songs.sortByDescending { s -> s.lastPlayedEpochSeconds }
    }

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var song: Song
            private set

        private val nameText: TextView = itemView.findViewById(R.id.song_name_text)
        private val artistText: TextView = itemView.findViewById(R.id.artist_text)
        private val lastPlayedText: TextView = itemView.findViewById(R.id.last_played_text)
        val moreButton: ImageButton = itemView.findViewById(R.id.more_button)

        fun setSong(newSong: Song) {
            song = newSong
            updateView()
        }

        private fun updateView() {
            if (!this::song.isInitialized)
                return

            nameText.text = song.songName
            artistText.text = song.artistName
            lastPlayedText.text = "Last played: " +
                    if (song.lastPlayedEpochSeconds == 0.toLong()) "Never"
                    else DateTimeFormatter
                        .ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)
                        .withZone(ZoneId.systemDefault())
                        .format(Instant.ofEpochSecond(song.lastPlayedEpochSeconds))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.songs_list_row, parent, false)
        return SongViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SongViewHolder, originalPosition: Int) {
        holder.setSong(songs[originalPosition])

        holder.moreButton.setOnClickListener {
            val popup = PopupMenu(holder.itemView.context, holder.moreButton)
            popup.menuInflater.inflate(R.menu.song_menu, popup.menu)
            popup.setOnMenuItemClickListener {
                val position = holder.adapterPosition
                when (it.itemId) {
                    R.id.action_delete -> {
                        songs.removeAt(position)
                        notifyItemRemoved(position)
                        coroutineScope.launch {
                            songDao.deleteSong(holder.song)
                        }
                        true
                    }

                    R.id.action_mark_as_just_played -> {
                        setLastPlayed(holder, Clock.systemDefaultZone().instant())
                        true
                    }

                    R.id.action_choose_last_played_time -> {
                        val currentDate = LocalDate.now()

                        val datePickerDialog = DatePickerDialog(
                            holder.itemView.context,
                            { _, selectedYear, selectedMonth, selectedDay ->
                                val timePickerDialog = TimePickerDialog(
                                    holder.itemView.context,
                                    { _, selectedHour, selectedMinute ->
                                        val selectedTime = ZonedDateTime.of(
                                            selectedYear,
                                            selectedMonth + 1,
                                            selectedDay,
                                            selectedHour,
                                            selectedMinute,
                                            0,
                                            0,
                                            ZoneId.systemDefault()
                                        ).toInstant()
                                        setLastPlayed(holder, selectedTime)
                                    },
                                    0, 0, false
                                )
                                timePickerDialog.show()
                            }, currentDate.year, currentDate.monthValue, currentDate.dayOfMonth
                        )
                        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
                        datePickerDialog.show()

                        true
                    }

                    else -> {
                        true
                    }
                }
            }
            popup.show()
        }
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    fun addSong(song: Song) {
        coroutineScope.launch {
            val songId = songDao.upsertSong(song)
            val newSong = song.copy(id = songId)
            songs.add(newSong)
            songs.sortByDescending { s -> s.lastPlayedEpochSeconds }
            notifyItemInserted(songs.indexOf(newSong))
        }
    }

    private fun setLastPlayed(holder: SongViewHolder, lastPlayedTime: Instant?) {
        coroutineScope.launch {
            val updatedSong = holder.song.copy(lastPlayedEpochSeconds = lastPlayedTime?.epochSecond ?: 0)
            songDao.upsertSong(updatedSong)

            songs.remove(holder.song)
            songs.add(updatedSong)
            songs.sortByDescending { s -> s.lastPlayedEpochSeconds }

            holder.setSong(updatedSong)
            notifyItemMoved(holder.adapterPosition, songs.indexOf(holder.song))
        }
    }
}