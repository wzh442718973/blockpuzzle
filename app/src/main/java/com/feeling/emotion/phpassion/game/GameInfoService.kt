package com.feeling.emotion.phpassion.game

import com.feeling.emotion.phpassion.gamedefinition.SSLiberatedInfo
import com.feeling.emotion.phpassion.gamestate.SpielstandDAO
import com.feeling.emotion.phpassion.planet.IPlanet

class GameInfoService {

    fun isPlanetFullyLiberated(planet: IPlanet): Boolean {
        val defs = planet.gameDefinitions
        val dao = SpielstandDAO()
        for (gi in defs.indices) {
            val ss = dao.load(planet, gi)
            val info = SSLiberatedInfo(ss)
            if (!defs[gi].isLiberated(info)) {
                return false
            }
        }
        return defs.size > 0
    }

    fun executeLiberationFeature(planet: IPlanet) {
        planet.gameDefinitions[0].liberatedFeature?.start()
    }
}
