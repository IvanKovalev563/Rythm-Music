package com.example.rythmmusic

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class MainFragment : Fragment(R.layout.fragment_main) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_songs)

        val mySongs = listOf(
            ClassSong("Starboy", "The Weeknd", R.drawable.water, true),
            ClassSong("After Hours", "The Weeknd", R.drawable.wind, true)
        )

        val adapter = SongAdapter(mySongs){ selectedSong ->
            val bottomNav = requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_navigation)

            val mainActivity = requireActivity() as MainActivity
            mainActivity.currentSong = selectedSong


            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_up,   // Вход нового фрагмента
                    android.R.anim.fade_out,      // Выход старого фрагмента
                    android.R.anim.fade_in,       // Вход старого при нажатии "Назад"
                    R.anim.slide_out_down
                )
                .replace(R.id.fragment_container, TrackFragment())
                .addToBackStack(null)
                .commit()
            bottomNav.menu.findItem(R.id.page_track).isChecked = true
            mainActivity.currentItemId = R.id.page_track
        }

        recyclerView.layoutManager = GridLayoutManager(context, 2)

        recyclerView.adapter = adapter

        val btn_favorite = view.findViewById<MaterialButton>(R.id.btn_favorite)

        btn_favorite.setOnClickListener {

        }
    }
}