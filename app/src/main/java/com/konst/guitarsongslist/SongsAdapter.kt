package com.konst.guitarsongslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SongsAdapter(private val songs: ArrayList<Song>) : RecyclerView.Adapter<SongsAdapter.SongViewHolder>() {

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.song_name_text)
        val moreButton: ImageButton = itemView.findViewById(R.id.more_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.songs_list_row, parent, false)
        return SongViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.nameText.text = songs[position].name
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