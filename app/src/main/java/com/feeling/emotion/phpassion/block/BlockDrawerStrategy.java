package com.feeling.emotion.phpassion.block;

/**
 * Kästchen ausfüllen Strategie
 */
public interface BlockDrawerStrategy {

    /**
     * @return IBlockDrawer für ein nicht leeres Kästchen
     */
    IBlockDrawer get(int x, int y);
}
