package com.example.rythmmusic

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class FavoriteFragment {
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val mainActivity = requireActivity() as MainActivity
//
//        // 1. ФИЛЬТРУЕМ: Берем только те, где лайк
//        val favoriteSongs = mainActivity.allSongs.filter { it.isFavorite }
//
//        // 2. НАСТРАИВАЕМ RECYCLERVIEW
//        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_favorites)
//
//        // Используем твой же SongAdapter!
//        val adapter = SongAdapter(favoriteSongs) { selectedSong ->
//            // Тот же клик, что и в главном фрагменте
//            mainActivity.currentSong = selectedSong
//            val bottomNav = mainActivity.findViewById<BottomNavigationView>(R.id.bottom_navigation)
//            bottomNav.selectedItemId = R.id.page_track
//        }
//
//        recyclerView.adapter = adapter
//        recyclerView.layoutManager = LinearLayoutManager(context)
//    }
}