package com.feeling.emotion.phpassion.gamestate;

import com.feeling.emotion.phpassion.global.AbstractDAO;

public final class TrophiesDAO extends AbstractDAO<Trophies> {

    public Trophies load(int cluster) {
        return load("C"+ cluster);
    }

    public void save(int cluster, Trophies t) {
        save("C" + cluster, t);
    }

    public boolean delete(int cluster) {
        return delete("C" + cluster);
    }

    @Override
    protected Class<Trophies> getTClass() {
        return Trophies.class;
    }
}
