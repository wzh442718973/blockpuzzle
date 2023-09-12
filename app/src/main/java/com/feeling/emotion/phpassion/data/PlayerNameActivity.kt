package com.feeling.emotion.phpassion.data

import android.os.Build
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.feeling.emotion.phpassion.R
import com.feeling.emotion.phpassion.databinding.ActivityPlayerNameBinding
import com.feeling.emotion.phpassion.global.AbstractDAO
import com.feeling.emotion.phpassion.global.GlobalData
import com.feeling.emotion.phpassion.planet.SpaceObjectStateService

class PlayerNameActivity : AppCompatActivity() {

    lateinit var binding:ActivityPlayerNameBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerNameBinding.inflate(layoutInflater);
        setContentView(binding.root);

        if (Build.VERSION.SDK_INT >= 21) {
            window.navigationBarColor = ContextCompat.getColor(this, R.color.navigationBackground)
        }
        AbstractDAO.init(this)

        binding.playername.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onSaveBtn()
                true
            } else {
                false
            }
        }
        binding.saveBtn.setOnClickListener { onSaveBtn() }
    }

    override fun onResume() {
        super.onResume()
        try {
            val gd = GlobalData.get()
            binding.playername.setText(gd.playername?:"")
            binding.gameSounds.isChecked = gd.isGameSounds
            binding.sunMode.isChecked = gd.isSunMode
        } catch (e: Exception) {
            Toast.makeText(this, e.javaClass.toString() + ": " + e.message + "\n" + e.stackTrace[0].toString(), Toast.LENGTH_LONG).show()
        }
    }

    override fun onPause() {
        super.onPause()
        val gd = GlobalData.get()
        gd.isGameSounds = binding.gameSounds.isChecked
        gd.isSunMode = binding.sunMode.isChecked
        gd.save()
    }

    private fun onSaveBtn() {
        val pn = binding.playername.text.toString().trim()
        when {
            pn.isEmpty() -> return
            pn == "open_map" -> SpaceObjectStateService().openMap()
            else -> savePlayername(pn)
        }
        finish()
    }

    private fun savePlayername(pn: String) {
        val gd = GlobalData.get()
        gd.playername = pn
        gd.isPlayernameEntered = true
        gd.save()
    }
}