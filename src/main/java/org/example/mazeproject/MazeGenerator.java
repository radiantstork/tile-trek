// Used for generating the Tile-matrix representation of a random maze.

package org.example.mazeproject;

import java.util.*;

public class MazeGenerator {
    private int rows;
    private int cols;
    private int startX;
    private int startY;
    private int endX, endY;
    private Tile[][] grid;
    private final Random rand = new Random();

    // main method: generate the grid matrix of the maze
    public void generateMaze(int rows, int cols) {
        Tile start;
        int visitedCount;
        int effectCount;

        visitedCount = 0;

        initializeMazeParameters(rows, cols);

        start = grid[startX][startY];
        start.setVisited(true);
        ++visitedCount;

        // loop as long as we haven't fit all the tiles into the maze
        while (visitedCount < rows * cols) {
            int[] randomTile;
            Map<String, int[]> path;

            randomTile = getRandomUnvisitedTile();
            path = createPath(randomTile);

            visitedCount += carvePath(path);
        }

        effectCount = rand.nextInt(5, 15);
        placeEffects(effectCount);
    }

    // ---------- HELPER METHODS -----------

    // initialize no. of rows/colums, start/end points
    private void initializeMazeParameters(int rows, int cols) {
        int corner;

        this.rows = rows;
        this.cols = cols;
        this.grid = new Tile[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new Tile();
            }
        }

        // generate a random starting point (one of the 4 corners)
        // the end is the opposite of the start
        corner = rand.nextInt(4);
        switch (corner) {
            case 0 -> {
                startX = 0;
                startY = 0;
            }
            case 1 -> {
                startX = 0;
                startY = cols - 1;
            }
            case 2 -> {
                startX = rows - 1;
                startY = 0;
            }
            case 3 -> {
                startX = rows - 1;
                startY = cols - 1;
            }
        }
        endX = rows - 1 - startX;
        endY = cols - 1 - startY;
    }

    // return a random unvisited tile
    private int[] getRandomUnvisitedTile() {
        int randomRow, randomCol;
        do {
            randomRow = rand.nextInt(rows);
            randomCol = rand.nextInt(cols);
        } while (grid[randomRow][randomCol].isVisited());
        return new int[]{randomRow, randomCol};
    }

    // return the list of all valid/possible neighbors for a specific tile
    private List<int[]> getNeighbors(int row, int col) {
        List<int[]> neighbors;

        neighbors = new ArrayList<>();

        if (row > 0) neighbors.add(new int[]{row - 1, col});
        if (row < rows - 1) neighbors.add(new int[]{row + 1, col});
        if (col > 0) neighbors.add(new int[]{row, col - 1});
        if (col < cols - 1) neighbors.add(new int[]{row, col + 1});

        return neighbors;
    }

    // find a path with no cycles from an unvisited tile to a visited tile
    private Map<String, int[]> createPath(int[] start) {
        int currentRow, currentCol;
        Map<String, int[]> path;

        currentRow = start[0];
        currentCol = start[1];
        path = new LinkedHashMap<>();

        while (!grid[currentRow][currentCol].isVisited()) {
            List<int[]> neighbors;
            int[] next;
            String nextKey;

            neighbors = getNeighbors(currentRow, currentCol);
            next = neighbors.get(rand.nextInt(neighbors.size()));
            nextKey = next[0] + "," + next[1];

            if (path.containsKey(nextKey)) {
                Iterator<String> it;

                it = path.keySet().iterator();

                while (it.hasNext()) {
                    String key;

                    key = it.next();

                    it.remove();

                    if (key.equals(nextKey)) break;
                }
            }
            path.put(nextKey, new int[]{currentRow, currentCol});
            currentRow = next[0];
            currentCol = next[1];
        }
        return path;
    }

    // update the walls of a path and return the number of newly visited tiles
    private int carvePath(Map<String, int[]> path) {
        int count;

        count = 0;

        for (Map.Entry<String, int[]> entry : path.entrySet()) {
            String[] aux;
            int row, col;
            int prevRow, prevCol;
            Tile current, prev;

            aux = entry.getKey().split(",");
            row = Integer.parseInt(aux[0]);
            col = Integer.parseInt(aux[1]);
            prevRow = entry.getValue()[0];
            prevCol = entry.getValue()[1];
            current = grid[row][col];
            prev = grid[prevRow][prevCol];

            removeWalls(current, row, col, prev, prevRow, prevCol);

            if (!current.isVisited()) {
                current.setVisited(true);
                count++;
            }
            if (!prev.isVisited()) {
                prev.setVisited(true);
                count++;
            }
        }

        return count;
    }

    // remove the correct wall for 2 connected tiles
    private void removeWalls(Tile current, int currRow, int currCol, Tile prev, int prevRow, int prevCol) {
        if (currRow == prevRow + 1) {
            current.removeTopWall();
            prev.removeBottomWall();
        } else if (currRow == prevRow - 1) {
            current.removeBottomWall();
            prev.removeTopWall();
        } else if (currCol == prevCol + 1) {
            current.removeLeftWall();
            prev.removeRightWall();
        } else if (currCol == prevCol - 1) {
            current.removeRightWall();
            prev.removeLeftWall();
        }
    }

    private void placeEffects(int count) {
        List<Effect> effects;

        effects = List.of(new VignetteEffect(), new InvertedMovementEffect(), new InvisibleWallsEffect());

        for (int i = 0; i < count; ++i) {
            int row, col;
            Tile tile;

            row = rand.nextInt(rows);
            col = rand.nextInt(cols);
            tile = grid[row][col];

            if (!tile.hasEffect() && !(row == startX && col == startY)) {
                tile.setEffect(effects.get(rand.nextInt(effects.size())));
            }
        }
    }

    // ---------- GETTERS ----------

    public int[] getStart() {
        return new int[]{startX, startY};
    }

    public int[] getEnd() {
        return new int[]{endX, endY};
    }

    public Tile[][] getGrid() {
        return grid;
    }
}
