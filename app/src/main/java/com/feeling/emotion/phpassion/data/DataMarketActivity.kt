package com.feeling.emotion.phpassion.data

import android.content.ClipData
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.feeling.emotion.phpassion.R
import com.feeling.emotion.phpassion.databinding.ActivityDataMarketBinding
import com.feeling.emotion.phpassion.game.GameEngineFactory
import com.feeling.emotion.phpassion.game.stonewars.deathstar.SpaceNebulaRoute
import com.feeling.emotion.phpassion.gamestate.TrophiesDAO
import com.feeling.emotion.phpassion.global.AbstractDAO
import com.feeling.emotion.phpassion.global.GlobalData
import com.feeling.emotion.phpassion.global.messages.MessageFactory

/**
 * Datenmarktplatz auf Planeten
 */
class DataMarketActivity : AppCompatActivity() {


    lateinit var binding : ActivityDataMarketBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDataMarketBinding.inflate(layoutInflater);
        setContentView(binding.root);

        if (Build.VERSION.SDK_INT >= 21) {
            window.navigationBarColor = ContextCompat.getColor(this, R.color.navigationBackground)
        }
        AbstractDAO.init(this)

        binding.pasteBtn.setOnClickListener { onPaste() }
        binding.copyBtn.setOnClickListener { onCopy() }
        binding.enterPlayername.setOnClickListener { startActivity(Intent(this, PlayerNameActivity::class.java)) }
    }

    override fun onResume() {
        super.onResume()
        try {
            // Data exchange not possible in Death Star mode
            val enabled = SpaceNebulaRoute.isNoDeathStarMode
            binding.pasteBtn.isEnabled = enabled
            binding.copyBtn.isEnabled = enabled && GlobalData.get().isPlayernameEntered

            binding.dataview.text = if (enabled) DataService().get() else ""
            binding.trophies.text = getTrophiesText()
        } catch (e: Exception) {
            Toast.makeText(this, e.javaClass.toString() + ": " + e.message + "\n" + e.stackTrace[0].toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun onCopy() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val o = ClipData.newPlainText("BlockPuzzleDataPacket", DataService().get())
        clipboard.setPrimaryClip(o)
        Toast.makeText(this, resources.getString(R.string.copied), Toast.LENGTH_SHORT).show()
    }

    private fun onPaste() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val messages = MessageFactory(this)
        if (clipboard.hasPrimaryClip()) {
            if (clipboard.primaryClipDescription!!.hasMimeType(MIMETYPE_TEXT_PLAIN)) {
                val item = clipboard.primaryClip!!.getItemAt(0)
                val pasteData = item.text
                if (pasteData != null) {
                    DataService().put(pasteData.toString(), messages).show()
                    return // success
                }
            }
        }
        messages.nothingToInsert.show()
    }

    private fun getTrophiesText(): String {
        val planet = GameEngineFactory().getPlanet()
        val trophies = TrophiesDAO().load(planet.clusterNumber)
        val platinum = GlobalData.get().platinumTrophies
        return resources.getString(R.string.trophies, planet.clusterNumber, trophies.bronze, trophies.silver, trophies.golden, platinum)
    }
}