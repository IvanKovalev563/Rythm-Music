package com.example.rythmmusic

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

class TrackFragment : Fragment(R.layout.fragment_track) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val song = (requireActivity() as MainActivity).currentSong

        song?.let {
            view.findViewById<TextView>(R.id.text_title)?.text = it.title
            view.findViewById<TextView>(R.id.text_artist)?.text = it.author
            val imageView = view.findViewById<ImageView>(R.id.img_cover)
            if (imageView != null) {
                com.bumptech.glide.Glide.with(this)
                    .load(it.coverImg)
                    .placeholder(R.drawable.ic_track)
                    .error(R.drawable.ic_error)
                    .into(imageView)
            }
        }

        // ВНИМАНИЕ: кнопки ищем через view.findViewById
        val btn_PauseStart = view.findViewById<MaterialButton>(R.id.btn_play)
        var isPlaying = false

        btn_PauseStart.setOnClickListener {
            if (!isPlaying) {
                btn_PauseStart.setIconResource(R.drawable.ic_pause)
                isPlaying = true
            } else {
                btn_PauseStart.setIconResource(R.drawable.ic_play)
                isPlaying = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateUI() // Обновляем данные при каждом возврате на этот экран
    }

    private fun updateUI() {
        // Проверяем, что экран (view) вообще создан и существует
        val currentView = view ?: return

        val song = (requireActivity() as? MainActivity)?.currentSong

        song?.let {
            currentView.findViewById<TextView>(R.id.text_title)?.text = it.title
            currentView.findViewById<TextView>(R.id.text_artist)?.text = it.author
            val imageView = view?.findViewById<ImageView>(R.id.img_cover)
            if (imageView != null) {
                com.bumptech.glide.Glide.with(this)
                    .load(it.coverImg)
                    .placeholder(R.drawable.ic_track)
                    .error(R.drawable.ic_error)
                    .into(imageView)
            }
        }
    }
}