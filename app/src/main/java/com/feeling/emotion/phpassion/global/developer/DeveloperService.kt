package com.feeling.emotion.phpassion.global.developer

import com.feeling.emotion.phpassion.cluster.Cluster1
import com.feeling.emotion.phpassion.gamestate.SpielstandDAO
import com.feeling.emotion.phpassion.gamestate.TrophiesDAO
import com.feeling.emotion.phpassion.global.GlobalDataDAO
import com.feeling.emotion.phpassion.planet.IPlanet
import com.feeling.emotion.phpassion.planet.SpaceObjectStateDAO
import kotlin.system.exitProcess

class DeveloperService {

    /** ACHTUNG! Löscht ALLE Daten. */
    fun resetAll() {
        val dao = SpielstandDAO()
        val sosDAO = SpaceObjectStateDAO()

        GlobalDataDAO().delete()
        DeveloperDataDAO().delete()
        dao.deleteOldGame()
        Cluster1.spaceObjects.forEach { so ->
            sosDAO.delete(so)

            if (so is IPlanet) {
                for (i in so.gameDefinitions.indices) {
                    dao.delete(so, i)
                }
            }
        }
        TrophiesDAO().delete(1)

        exitProcess(0)
    }

    fun saveToday(date: String) {
        val dd = DeveloperData.get()
        dd.today = date
        dd.save()
    }

    fun loadToday(): String? {
        return DeveloperData.get().today
    }
}