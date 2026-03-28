package com.example.rythmmusic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class SongAdapter(private val songs: List<ClassSong>) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    // 1. "Кладовка" (ViewHolder): находит и держит ссылки на View внутри одного бокса,
    // чтобы не искать их через findViewById каждый раз при скролле (это ускоряет работу).
    class SongViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.text_title)
        val author: TextView = view.findViewById(R.id.text_artist)
        val cover: ImageView = view.findViewById(R.id.img_cover)
    }

    // 2. "Создание упаковки": вызывается, когда системе нужна новая пустая плитка.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return SongViewHolder(view)
    }

    // 3. "Заполнение": берет пустую плитку и вписывает в неё данные конкретной песни.
    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.title.text = song.title
        holder.author.text = song.author
        holder.cover.setImageResource(song.coverImg)
    }

    // 4. "Отчет": говорит системе, сколько всего товаров на полке.
    override fun getItemCount() = songs.size
}