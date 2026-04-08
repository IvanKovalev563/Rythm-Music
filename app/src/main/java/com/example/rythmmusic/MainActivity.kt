package com.example.rythmmusic

import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
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

    private var mediaPlayer: android.media.MediaPlayer? = null
    var allSongs: List<ClassSong> = listOf()
    var currentSong: ClassSong? = null
    var currentItemId = R.id.page_home // Запоминаем текущий ID

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            // Разрешение получено, сканируем музыку
            scanMusic()
            // И можно сразу загрузить фрагмент
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_up,
                    android.R.anim.fade_in
                )
                .replace(R.id.fragment_container, MainFragment())
                .commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        if(androidx.core.content.ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != android.content.pm.PackageManager.PERMISSION_GRANTED){
            androidx.core.app.ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),100)
        }
        else{
            scanMusic()
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
                R.id.page_favorite -> FavoriteFragment()
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

    fun openFavoriteFragment() {
        val fragment = FavoriteFragment()
        supportFragmentManager.beginTransaction()
            // Используем анимацию "выезда" в зависимости от логики меню
            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
            .replace(R.id.fragment_container, fragment)
            .commit()

        // ВАЖНО: Переключи иконку в нижнем меню на "Избранное"
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.menu.findItem(R.id.page_favorite).isChecked = true // Замени ID на свой для избранного
        currentItemId = R.id.page_favorite
    }

    private fun getMenuPosition(itemId: Int): Int {
        return when (itemId) {
            R.id.page_home -> 0
            R.id.page_track -> 1
            R.id.page_favorite -> 2
            else -> 0
        }
    }

    fun getMusicFromDefaultFolder(): List<ClassSong> {
        val songs = mutableListOf<ClassSong>()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val defaultMusicPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).absolutePath

        val projection = arrayOf(
            android.provider.MediaStore.Audio.Media.TITLE,
            android.provider.MediaStore.Audio.Media.ARTIST,
            android.provider.MediaStore.Audio.Media.DATA,
            android.provider.MediaStore.Audio.Media.ALBUM_ID
        )

        val selection = "${android.provider.MediaStore.Audio.Media.DATA} LIKE ?"
        val selectionArgs = arrayOf("$defaultMusicPath%")

        val cursor = contentResolver.query(uri, projection, selection, selectionArgs, null)

        cursor?.use {
            val titleId = it.getColumnIndexOrThrow(android.provider.MediaStore.Audio.Media.TITLE)
            val artistId = it.getColumnIndexOrThrow(android.provider.MediaStore.Audio.Media.ARTIST)
            val pathId = it.getColumnIndexOrThrow(android.provider.MediaStore.Audio.Media.DATA)
            val albumIdId = it.getColumnIndexOrThrow(android.provider.MediaStore.Audio.Media.ALBUM_ID)

            while(it.moveToNext()){
                val albumId = it.getLong(albumIdId)

                val coverURI = android.content.ContentUris.withAppendedId(
                    android.net.Uri.parse("content://media/external/audio/albumart"),
                    albumId
                ).toString()

                songs.add(ClassSong(
                    title = it.getString(titleId) ?: "Unknown",
                    author = it.getString(artistId) ?: "Unknown",
                    coverImg = coverURI,
                    favorite = false,
                    path = it.getString(pathId)
                ))
            }
        }
        return songs
    }

    fun playMusic(songPath: String){
        mediaPlayer?.stop()
        mediaPlayer?.release()

        mediaPlayer = android.media.MediaPlayer().apply {
            setDataSource(songPath)
            prepare()
            start()
        }
    }

    fun PlayOrPause(): Boolean {
        val mediaPlayer_ = mediaPlayer ?: return false

        if(mediaPlayer_.isPlaying){
            mediaPlayer_.pause()
            return false
        }
        else{
            mediaPlayer_.start()
            return true
        }
    }

    fun nextSong(){
        if (allSongs.isEmpty()){
            return
        }

        val currentIndex = allSongs.indexOf(currentSong)
        val nextIndex = (currentIndex + 1) % allSongs.size

        currentSong = allSongs[nextIndex]
        currentSong?.let { playMusic(it.path) }
    }

    fun prevSong(){
        if (allSongs.isEmpty()){
            return
        }

        val currentIndex = allSongs.indexOf(currentSong)
        var prevIndex = currentIndex - 1
        if (prevIndex < 0) prevIndex = allSongs.size - 1

        currentSong = allSongs[prevIndex]
        currentSong?.let { playMusic(it.path) }
    }

    fun getDuration(): Int = mediaPlayer?.duration ?: 0
    fun getCurrentPosition(): Int = mediaPlayer?.currentPosition ?: 0
    fun seekTo(position: Int) { mediaPlayer?.seekTo(position) }

    fun formatTime(ms: Int): String {
        val minutes = (ms / 1000) / 60
        val seconds = (ms / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    fun toggleFavorite(songPath: String) {
        val prefs = getSharedPreferences("MusicPrefs", MODE_PRIVATE)
        val favorites = getFavorites().toMutableSet()

        if (favorites.contains(songPath)) {
            favorites.remove(songPath)
        } else {
            favorites.add(songPath)
        }

        prefs.edit().putStringSet("fav_list", favorites).apply()

        allSongs.find { it.path == songPath }?.favorite = favorites.contains(songPath)
    }

    fun getFavorites(): Set<String> {
        val prefs = getSharedPreferences("MusicPrefs", MODE_PRIVATE)
        return prefs.getStringSet("fav_list", setOf()) ?: setOf()
    }

    fun scanMusic() {
        allSongs = getMusicFromDefaultFolder()

        // СИНХРОНИЗАЦИЯ: отмечаем лайками те песни, которые сохранены в памяти
        val savedFavs = getFavorites() // та функция из SharedPreferences
        allSongs.forEach { song ->
            if (savedFavs.contains(song.path)) {
                song.favorite = true
            }
        }
    }

        override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
