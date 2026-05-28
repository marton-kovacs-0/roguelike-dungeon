package com.marton.roguelike.world;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapLoader {



    public GameMap loadMap() {
        int width = 0;
        int height = 0;
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("assets/maps/testmap.txt"))) {

            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            width = lines.get(0).length();
            height = lines.size();
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }

        GameMap gameMap = new GameMap(width, height);

        for (int y = 0; y < lines.size(); y++) {
            for (int x = 0; x < lines.get(y).length(); x++) {
                gameMap.setCell(x, y, new Cell(x, y, getCellTypeFromSymbol(lines.get(y).charAt(x))));
            }
        }
        return gameMap;
    }

    private CellType getCellTypeFromSymbol(char character) {
        switch(character) {
            case '#': return CellType.WALL;
            case '.': return CellType.FLOOR;
            case 'T': return CellType.TREE;
            case '\0': return CellType.EMPTY;
        }
        throw new IllegalArgumentException("Invalid character");
    }


}
