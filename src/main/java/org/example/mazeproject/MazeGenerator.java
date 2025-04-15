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
    Random rand = new Random();

    // main method: generate the grid matrix of the maze
    public void generateMaze(int rows, int cols) {
        Tile start;
        int visitedCount = 0;

        initializeMazeParameters(rows, cols);

        start = grid[startX][startY];
        start.setVisited(true);
        ++visitedCount;

        // loop as long as we haven't fit all the tiles into the maze
        while (visitedCount < rows * cols) {
            int[] randomTile = getRandomUnvisitedTile();
            Map<String, int[]> path = createPath(randomTile);
            visitedCount += carvePath(path);
        }
    }

    // ---------- HELPER METHODS -----------

    // initialize no. of rows/colums, start/end points
    private void initializeMazeParameters(int rows, int cols) {
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
        int corner = rand.nextInt(4);
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
        List<int[]> neighbors = new ArrayList<>();
        if (row > 0) neighbors.add(new int[]{row - 1, col});
        if (row < rows - 1) neighbors.add(new int[]{row + 1, col});
        if (col > 0) neighbors.add(new int[]{row, col - 1});
        if (col < cols - 1) neighbors.add(new int[]{row, col + 1});
        return neighbors;
    }

    // find a path with no cycles from an unvisited tile to a visited tile
    private Map<String, int[]> createPath(int[] start) {
        int currentRow = start[0];
        int currentCol = start[1];
        Map<String, int[]> path = new LinkedHashMap<>();

        while (!grid[currentRow][currentCol].isVisited()) {
            List<int[]> neighbors = getNeighbors(currentRow, currentCol);
            int[] next = neighbors.get(rand.nextInt(neighbors.size()));
            String nextKey = next[0] + "," + next[1];

            if (path.containsKey(nextKey)) {
                Iterator<String> it = path.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next();
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
        int count = 0;
        for (Map.Entry<String, int[]> entry : path.entrySet()) {
            String[] aux = entry.getKey().split(",");
            int row = Integer.parseInt(aux[0]);
            int col = Integer.parseInt(aux[1]);
            int prevRow = entry.getValue()[0];
            int prevCol = entry.getValue()[1];
            Tile current = grid[row][col];
            Tile prev = grid[prevRow][prevCol];

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
