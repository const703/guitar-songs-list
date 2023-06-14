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
import java.text.DateFormat
import java.util.Calendar
import java.util.Date


class SongsAdapter(private val songs: ArrayList<Song>) : RecyclerView.Adapter<SongsAdapter.SongViewHolder>() {

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.song_name_text)
        val moreButton: ImageButton = itemView.findViewById(R.id.more_button)
        private val lastPlayedText: TextView = itemView.findViewById(R.id.last_played_text)

        fun setLastPlayed(lastPlayedDate: Date?) {
            if (lastPlayedDate == null) {
                lastPlayedText.text = "Last played: Never"
            }
            else {
                lastPlayedText.text = "Last played: " + DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(lastPlayedDate)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.songs_list_row, parent, false)
        return SongViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.nameText.text = songs[position].name
        holder.setLastPlayed(songs[position].lastPlayed)
        holder.moreButton.setOnClickListener {
            val popup = PopupMenu(holder.itemView.context, holder.moreButton)
            popup.menuInflater.inflate(R.menu.song_menu, popup.menu)
            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_delete -> {
                        songs.removeAt(position)
                        notifyItemRemoved(position)
                        true
                    }
                    R.id.action_mark_as_just_played -> {
                        songs[position].lastPlayed = Calendar.getInstance().time
                        holder.setLastPlayed(songs[position].lastPlayed)
                        true
                    }
                    R.id.action_choose_last_played_time -> {
                        val calendar = Calendar.getInstance()

                        var selectedDate = Date(0)
                        val year = calendar.get(Calendar.YEAR)
                        val month = calendar.get(Calendar.MONTH)
                        val day = calendar.get(Calendar.DAY_OF_MONTH)

                        val datePickerDialog = DatePickerDialog(holder.itemView.context,
                            { _, selectedYear, selectedMonth, selectedDay ->
                                selectedDate = Calendar.getInstance().apply {
                                    set(selectedYear, selectedMonth - 1, selectedDay, 0, 0)
                                }.time

                                val timePickerDialog = TimePickerDialog(holder.itemView.context,
                                    { _, selectedHour, selectedMinute ->
                                        selectedDate = Date(selectedDate.time + 1000 * 60 * ((selectedHour * 60) + selectedMinute))

                                        songs[position].lastPlayed = selectedDate
                                        holder.setLastPlayed(songs[position].lastPlayed)
                                    },
                                    0, 0, false
                                )
                                timePickerDialog.show()


                            }, year, month, day
                        )
                        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
                        datePickerDialog.show()

                        true
                    }
                    else ->{
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
}