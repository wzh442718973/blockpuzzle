package com.feeling.emotion.phpassion.gamestate

import com.feeling.emotion.phpassion.global.GlobalData

class TrophyService {
    private val dao = TrophiesDAO()

    fun clear() {
        dao.delete(1)
        val gd = GlobalData.get()
        gd.platinumTrophies = 0
        gd.save()
    }
}