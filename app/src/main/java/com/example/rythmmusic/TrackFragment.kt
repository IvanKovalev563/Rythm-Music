package com.example.rythmmusic

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

class TrackFragment : Fragment(R.layout.fragment_player) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
}