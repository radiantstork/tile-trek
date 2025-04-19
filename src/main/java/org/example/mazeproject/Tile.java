package org.example.mazeproject;

public class Tile {
    private boolean visited;
    private boolean topWall;
    private boolean rightWall;
    private boolean bottomWall;
    private boolean leftWall;
    private Effect effect;

    public Tile() {
        this.visited = false;
        this.topWall = true;
        this.rightWall = true;
        this.bottomWall = true;
        this.leftWall = true;
    }

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

    public boolean hasEffect() {
        return effect != null;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
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
}
