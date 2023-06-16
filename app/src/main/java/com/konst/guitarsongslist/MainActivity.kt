package com.konst.guitarsongslist

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.konst.guitarsongslist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var songs: ArrayList<Song>
    private lateinit var songsAdapter: SongsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        songs = ArrayList()

        songsAdapter = SongsAdapter(songs)
        binding.songsRecyclerView.adapter = songsAdapter
        binding.songsRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_add -> {
            val addSongDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this).apply outer@ {
                setTitle("Add song")
                val dialogView  = layoutInflater.inflate(R.layout.new_song_dialog_view, null)
                setView(dialogView)

                setPositiveButton("OK") { _, _ ->
                    val songName = dialogView.findViewById<EditText>(R.id.song_name_input).text.toString()
                    val artist = dialogView.findViewById<EditText>(R.id.artist_input).text.toString()
                    songs.add(Song(songName, artist))
                    songsAdapter.notifyItemInserted(songs.size - 1)
                }
                setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            }

            addSongDialogBuilder.show()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

}