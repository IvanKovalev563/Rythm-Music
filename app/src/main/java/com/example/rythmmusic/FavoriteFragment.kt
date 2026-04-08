package com.example.rythmmusic

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = requireActivity() as MainActivity

        // 1. ФИЛЬТРУЕМ: Берем только те, где лайк
        val favoriteSongs = mainActivity.allSongs.filter { it.favorite }

        // 2. НАСТРАИВАЕМ RECYCLERVIEW
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_songs)

        // Используем твой же SongAdapter!
        val adapter = SongAdapter(favoriteSongs) { selectedSong ->
            // Тот же клик, что и в главном фрагменте
            mainActivity.currentSong = selectedSong
            val bottomNav = mainActivity.findViewById<BottomNavigationView>(R.id.bottom_navigation)
            bottomNav.selectedItemId = R.id.page_track
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(context, 2)
    }
}