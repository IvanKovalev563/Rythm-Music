package com.example.rythmmusic

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

class TrackFragment : Fragment(R.layout.fragment_track) {

    private val handler = android.os.Handler(android.os.Looper.getMainLooper())
    private lateinit var runnable: Runnable

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
        val btn_nextSong = view.findViewById<MaterialButton>(R.id.btn_next)
        val btn_prevSong = view.findViewById<MaterialButton>(R.id.btn_prev)
        val seekBar = view.findViewById<SeekBar>(R.id.seekBar)
        val textTime = view.findViewById<TextView>(R.id.timeText)

        btn_PauseStart.setOnClickListener {
            val isPlaying = (requireActivity() as MainActivity).PlayOrPause()
            if (!isPlaying) {
                btn_PauseStart.setIconResource(R.drawable.ic_play)
            }
            else {
                btn_PauseStart.setIconResource(R.drawable.ic_pause)
            }
        }

        btn_nextSong.setOnClickListener {
            val main = requireActivity() as MainActivity
            main.nextSong()
            updateUI()
        }

        btn_prevSong.setOnClickListener {
            val main = requireActivity() as MainActivity
            main.prevSong()
            updateUI()
        }

        seekBar.setOnSeekBarChangeListener(object: android.widget.SeekBar.OnSeekBarChangeListener {
            val main = requireActivity() as MainActivity
            override fun onProgressChanged(obj: SeekBar?, progress: Int, destination: Boolean) {
                if(destination) main.seekTo(progress)
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        runnable = Runnable {
            val main = requireActivity() as MainActivity
            val current = main.getCurrentPosition()
            val totalDuration = main.getDuration()

            seekBar.max = totalDuration
            seekBar.progress = current

            textTime.text = "${main.formatTime(current)} / ${main.formatTime(totalDuration)}"

            if(current >= totalDuration) main.nextSong()

            handler.postDelayed(runnable, 500) // Повторяем через 0.5 секунды
        }
        handler.post(runnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(runnable) // Обязательно останавливаем "будильник", когда закрываем экран
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