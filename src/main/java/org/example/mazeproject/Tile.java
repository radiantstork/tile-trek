package org.example.mazeproject;

public abstract class Tile {
    private boolean visited = false;
    private boolean topWall = true;
    private boolean rightWall = true;
    private boolean bottomWall = true;
    private boolean leftWall = true;

    public Tile() {}

    public boolean isVisited() {
        return visited;
    }

    public boolean hasTopWall() {
        return topWall;
    }

    public boolean hasRightWall() {
        return rightWall;
    }

    public boolean hasBottomWall() {
        return bottomWall;
    }

    public boolean hasLeftWall() {
        return leftWall;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public void removeTopWall() {
        this.topWall = false;
    }

    public void removeRightWall() {
        this.rightWall = false;
    }

    public void removeBottomWall() {
        this.bottomWall = false;
    }

    public void removeLeftWall() {
        this.leftWall = false;
    }

    public abstract boolean hasEffect();

    public abstract Effect getEffect();

    public abstract void setEffect(Effect effect);
}
