package com.example.rythmmusic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.rythmmusic.ui.theme.RythmMusicTheme
import com.google.android.material.button.MaterialButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)
        val btn_PauseStart = findViewById<MaterialButton>(R.id.btn_play)
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
