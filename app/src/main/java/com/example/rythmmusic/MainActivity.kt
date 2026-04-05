package com.example.rythmmusic

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import com.example.rythmmusic.ui.theme.RythmMusicTheme
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    var currentSong: ClassSong? = null
    var currentItemId = R.id.page_home // Запоминаем текущий ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        if(androidx.core.content.ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != android.content.pm.PackageManager.PERMISSION_GRANTED){
            androidx.core.app.ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),100)
        }
        else{
            //loadSongs()
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNav.setOnItemSelectedListener { item ->
            if (item.itemId == currentItemId) return@setOnItemSelectedListener false

            val (enterAnim, exitAnim) = if (getMenuPosition(item.itemId) > getMenuPosition(currentItemId)) {
                R.anim.slide_in_right to R.anim.slide_out_left // Слайд вправо
            }
            else {
                android.R.anim.slide_in_left to android.R.anim.slide_out_right // Слайд влево
            }

            val fragment = when (item.itemId){
                R.id.page_home -> MainFragment()
                R.id.page_track -> TrackFragment()
                R.id.page_settings -> TrackFragment()
                else -> MainFragment()
            }

            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(enterAnim, exitAnim)
                .replace(R.id.fragment_container, fragment)
                .commit()

            currentItemId = item.itemId
            true
        }
    }

    private fun getMenuPosition(itemId: Int): Int {
        return when (itemId) {
            R.id.page_home -> 0
            R.id.page_track -> 1
            R.id.page_settings -> 2
            else -> 0
        }
    }
}