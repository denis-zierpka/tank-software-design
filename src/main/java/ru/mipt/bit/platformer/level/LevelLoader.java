package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;

import java.util.*;

public final class LevelLoader {

    public static LevelPopulation fromTextFile(TiledMapTileLayer ground, String path) {
        FileHandle fh = Gdx.files.internal(path);
        String[] lines = fh.readString("UTF-8").split("\n");

        int h = ground.getHeight();
        int w = ground.getWidth();

        if (lines.length != h)
            throw new IllegalArgumentException("Height doesn't match in level files");

        GridPoint2 player = null;
        List<GridPoint2> trees = new ArrayList<>();

        for (int row = 0; row < h; ++row) {
            String line = lines[row];
            if (line.length() != w)
                throw new IllegalArgumentException("Width doesn't match in level files");

            int y = h - 1 - row;
            for (int x = 0; x < w; x++) {
                char c = line.charAt(x);
                switch (c) {
                    case 'T':
                        trees.add(new GridPoint2(x, y));
                        break;
                    case 'X':
                        if (player != null)
                            throw new IllegalArgumentException("More than one X in the level file");
                        player = new GridPoint2(x, y);
                        break;
                    case '_':
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown symbol in the level file");
                }
            }
        }
        if (player == null)
            throw new IllegalArgumentException("There is no X (player) in the level file");

        return new LevelPopulation(player, trees);
    }

    public static LevelPopulation random(TiledMapTileLayer ground, int treeCount) {
        int w = ground.getWidth();
        int h = ground.getHeight();
        Random rnd = new Random(System.currentTimeMillis());

        GridPoint2 player = new GridPoint2(rnd.nextInt(w), rnd.nextInt(h));

        List<GridPoint2> free = new ArrayList<>(w * h - 1);
        for (int y = 0; y < h; ++y) {
            for (int x = 0; x < w; ++x) {
                if (x == player.x && y == player.y)
                    continue;
                
                free.add(new GridPoint2(x, y));
            }
        }
        Collections.shuffle(free, rnd);

        int n = Math.min(treeCount, free.size());
        List<GridPoint2> trees = free.subList(0, n);

        return new LevelPopulation(player, trees);
    }
}
