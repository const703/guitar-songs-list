package com.konst.guitarsongslist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.konst.guitarsongslist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val songs = listOf(
            Song("Test - 1"),
            Song("Test - 2"),
            Song("Test - 3"),
            Song("Test - 4"),
        )

        binding.songsRecyclerView.adapter = SongsAdapter(songs)
        binding.songsRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}