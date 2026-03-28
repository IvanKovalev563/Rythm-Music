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

        // ВНИМАНИЕ: кнопки ищем через view.findViewById
        // 1. Находим "полку"
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_songs)

        // 2. Создаем список "товаров" (данных)
        val mySongs = listOf(
            ClassSong("Starboy", "The Weeknd", R.drawable.ic_pause, true),
            ClassSong("After Hours", "The Weeknd", R.drawable.ic_home, true)
        )

        // 3. Нанимаем "продавца" (Адаптер) и отдаем ему товары
        val adapter = SongAdapter(mySongs)

        // 4. Указываем, КАК расставлять товары (в 2 колонки)
        recyclerView.layoutManager = GridLayoutManager(getContext(), 2)

        // 5. Привязываем продавца к полке
        recyclerView.adapter = adapter
    }
}