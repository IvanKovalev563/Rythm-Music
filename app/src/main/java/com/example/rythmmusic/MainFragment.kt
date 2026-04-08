package com.example.rythmmusic

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import android.text.Editable

class MainFragment : Fragment(R.layout.fragment_main) {

    val mainActivity = MainActivity()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_songs)

        val mySongs = (requireActivity() as MainActivity).allSongs
        if (mySongs.isEmpty()) {
            // Выведи сообщение, если список пуст
            android.util.Log.d("MY_APP", "Список песен в активити всё еще пуст!")
        }

        val adapter = SongAdapter(mySongs){ selectedSong ->
            val bottomNav = requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_navigation)

            val mainActivity = requireActivity() as MainActivity
            mainActivity.currentSong = selectedSong
            mainActivity.playMusic(selectedSong.path)

            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_up,   // Вход нового фрагмента
                    android.R.anim.fade_out,      // Выход старого фрагмента
                    android.R.anim.fade_in,       // Вход старого при нажатии "Назад"
                    R.anim.slide_out_down  // Выход нового при нажатии "Назад"
                )
                .replace(R.id.fragment_container, TrackFragment())
                .addToBackStack(null)
                .commit()
            bottomNav.menu.findItem(R.id.page_track).isChecked = true
            mainActivity.currentItemId = R.id.page_track
        }

        recyclerView.layoutManager = GridLayoutManager(context, 2)

        recyclerView.adapter = adapter

        val btn_favorite = view.findViewById<MaterialButton>(R.id.btn_favorites)

        btn_favorite.setOnClickListener {
            (requireActivity() as MainActivity).openFavoriteFragment()
        }

        val searchEditText = view.findViewById<android.widget.EditText>(R.id.search_text)
        val allSongs = (requireActivity() as MainActivity).allSongs

        searchEditText.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                val query = s.toString().trim().lowercase() // Добавили trim(), чтобы пробелы не мешали

                // Берем АКТУАЛЬНЫЙ список прямо сейчас
                val currentAllSongs = (requireActivity() as MainActivity).allSongs

                val filteredList = if (query.isEmpty()) {
                    currentAllSongs // Если поиск пустой — возвращаем всё
                } else {
                    currentAllSongs.filter {
                        it.title.lowercase().contains(query) ||
                                it.author.lowercase().contains(query)
                    }
                }

                adapter.updateList(filteredList)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}