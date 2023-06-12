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
            val songNameEditText = EditText(this).apply { inputType = InputType.TYPE_CLASS_TEXT }
            val addSongDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this).apply outer@ {
                setTitle("Add song")
                setView(songNameEditText)
                setPositiveButton("OK") { _, _ ->
                    val newSongName = songNameEditText.text.toString()
                    songs.add(Song(newSongName))
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