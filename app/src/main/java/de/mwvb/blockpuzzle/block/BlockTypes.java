package de.mwvb.blockpuzzle.block;

import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mwvb.blockpuzzle.R;
import de.mwvb.blockpuzzle.block.special.ISpecialBlock;
import de.mwvb.blockpuzzle.block.special.LockBlock;
import de.mwvb.blockpuzzle.block.special.StarBlock;

// TODO Move to spielstein package
// Bauplan für alle Blöcke
public class BlockTypes {
    public static final int ONE_COLOR = 10;
    /** key: block type char, value: block type number */
    private final Map<String, Integer> charMap = new HashMap<>();
    /** key: block type number, value: block drawing strategy */
    private final Map<Integer, IBlockDrawer> blockDrawerMap = new HashMap<>();
    private final View view;
    private final List<ISpecialBlock> specialBlockTypes = new ArrayList<>();
    public static final int MIN_SPECIAL = 20;
    public static final int MAX_SPECIAL = 29; // TODO Ich muss die 30er umziehen, um mehr Platz zu bekommen

    public BlockTypes(View view) {
        this.view = view;
        specialBlockTypes.add(new StarBlock());
        specialBlockTypes.add(new LockBlock());

        add(1, R.color.colorNormal);
        add(2, R.color.orange);
        add(3, R.color.red);
        add(4, R.color.blue);
        add(5, R.color.pink);
        add(6, R.color.yellow);
        add(ONE_COLOR, 'f', R.color.oneColor);
        add(11, 'o', R.color.oneColorOld);

        for (ISpecialBlock s : getSpecialBlockTypes()) {
            charMap.put("" + s.getBlockTypeChar(), s.getBlockType());
            if (view != null) {
                blockDrawerMap.put(s.getBlockType(), s.getBlockDrawer(view));
            }
        }
    }

    public List<ISpecialBlock> getSpecialBlockTypes() {
        return specialBlockTypes;
    }

    private void add(int blockType, int color) {
        if (blockType > 9) {
            throw new RuntimeException("Use other add() for blockTypes > 9 !");
        }
        charMap.put("" + blockType, blockType);
        if (view != null) {
            blockDrawerMap.put(blockType, ColorBlockDrawer.byRColor(view, color));
        }
    }

    private void add(int blockType, char cBlockType, int color) {
        charMap.put("" + cBlockType, blockType);
        if (view != null) {
            blockDrawerMap.put(blockType, ColorBlockDrawer.byRColor(view, color));
        }
    }

    public int toBlockType(char defChar, String definition) {
        String key = "" + defChar;
        if (charMap.containsKey(key)) {
            return charMap.get(key);
        } else {
            throw new RuntimeException("Wrong game piece definition!\n" +
                    "Unsupported char: " + key + "\nline: " + definition);
        }
    }

    public IBlockDrawer getBlockDrawer(int blockType) {
        return blockDrawerMap.get(blockType);
    }

    public char getBlockTypeChar(int blockTypeNumber) {
        for (Map.Entry<String, Integer> e : charMap.entrySet()) {
            if (e.getValue() == blockTypeNumber) {
                return e.getKey().charAt(0);
            }
        }
        throw new RuntimeException("Unknown block type number: " + blockTypeNumber);
    }

    public Integer getBlockTypeNumber(char c) {
        return charMap.get("" + c);
    }
}
