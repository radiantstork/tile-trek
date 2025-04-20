# Tile Trek 

## Table of contents 
- [Introduction](#introduction)
- [Wilson's Algorithm](#wilsons-algorithm)
- [Project Checkpoints](#project-checkpoints)
    - [Checkpoint 1](#checkpoint-1)
    - [Checkpoint 2](#checkpoint-2)

---

## <ins>1 - Introduction</ins>
TODO

---

## <ins>2 - Wilson's Algorithm</ins>
TODO

--- 

## <ins>3 - Project Checkpoints</ins>
### <ins>Checkpoint 1</ins>
- <ins>**Ten actions**</ins>:
    - Generate a new maze of variable dimensions **MxN**, with no cycles and a unique
    solution.
    - Render the generated maze.
    - Allow the player to move **UP/DOWN/LEFT/RIGHT**, only one tile at a time, unless
    there is a wall.
    - Ability to pick up a random effect from a Mystery Tile.
    - Apply the effect from the Mystery Tile.
    - Cancel the effect from the Mystery Tile.
    - Check the win condition.
    - Play background music.
    - Restart the game on command (using **R**), generating a random new level.
    - Restart the game once the player wins.

- <ins>**Eight objects**</ins>:
    - **class Tile**: the building block of the rendered maze. It is the parent of the
    classes **DefaultTile** and **MysteryTile**.
    - **interface Effect**: the effects that may be activated upon stepping on a Mystery
    Tile. It is the parent of some other subclasses, for various effects (such as 
    **InvertedMovementEffect** or **InvisibleWallsEffect**).
    - **class GameService**: API for the game.
    - **class MazeGenerator**: backend for generating the maze.
    - **class MazeRenderer**: rendering the maze, player, etc.
    - **class Player**: represents the user.
    - **class GameState**: the current settings of the game (active effects).
    - **enum Direction**: all the possible directions for moving the user.

### <ins>Checkpoint 2</ins>