package com.feeling.emotion.phpassion.game.stonewars.deathstar

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import com.feeling.emotion.phpassion.global.InfoActivity
import com.feeling.emotion.phpassion.playingfield.Action

/**
 * Action to show Milky Way alert
 */
class MilkyWayAlert(base: Context?) : ContextWrapper(base), Action {

    override fun execute() {
        val intent = Intent(this, InfoActivity::class.java)
        val args = Bundle()
        args.putInt(InfoActivity.MODE, InfoActivity.MILKY_WAY_ALERT)
        intent.putExtras(args)
        startActivity(intent)
    }
}