package com.marton.roguelike.world;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapLoader {



    public GameMap loadMap(String filePath) throws IOException {
        List<String> lines = readLines(filePath);
        int width = getMapWidth(lines);
        int height = getMapHeight(lines);

        GameMap gameMap = new GameMap(width, height);
        populateMap(gameMap, lines);

        return gameMap;
    }

    private CellType getCellTypeFromSymbol(char character) {
        switch(character) {
            case '#': return CellType.WALL;
            case '.': return CellType.FLOOR;
            case 'T': return CellType.TREE;
            case ' ': return CellType.EMPTY;
        }
        throw new IllegalArgumentException("Invalid character");
    }

    private List<String> readLines(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new IOException("Error reading file");
        }

        return lines;
    }

    private int getMapWidth(List<String> lines) {
        return lines.stream()
            .mapToInt(String::length)
            .max()
            .orElse(0);
    }

    private int getMapHeight(List<String> lines) {
        return lines.size();
    }

    private void populateMap(GameMap gameMap, List<String> lines) {
        for (int y = 0; y < gameMap.getHeight(); y++) {
            String currentLine = lines.get(y);
            for (int x = 0; x < gameMap.getWidth(); x++) {
                if (x < currentLine.length()) {
                    char symbol = currentLine.charAt(x);
                    gameMap.setCell(x, y, new Cell(new TilePosition(x, y), getCellTypeFromSymbol(symbol)));
                } else {
                    gameMap.setCell(x, y, new Cell(new TilePosition(x, y), CellType.EMPTY));
                }
            }
        }
    }
}
