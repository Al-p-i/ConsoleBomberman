package gameobjects;

import geometry.Position;

/**
 * Created by a.pomosov on 25/12/2017.
 */
public class Player extends GameObject implements Positionable {
    private static final int DEFAULT_BOMB_RADIUS = 1;
    private static final int DEFAULT_SPEED = 1;
    private final String name;
    private long nextBombCanBePlantedIn;
    private boolean isDead;
    private int speed;

    private Position position;
    private int bombRadius;

    public Player(String name, Position position) {
        this.name = name;
        this.position = position;
        this.bombRadius = DEFAULT_BOMB_RADIUS;
        this.speed = DEFAULT_SPEED;
    }

    public boolean isBombCanBePlanted() {
        return nextBombCanBePlantedIn <= 0;
    }

    public void tick(long elapsed) {
        if (nextBombCanBePlantedIn > 0) {
            nextBombCanBePlantedIn -= elapsed;
        }
    }

    @Override
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setNextBombCanBePlantedIn(long nextBombCanBePlantedIn) {
        this.nextBombCanBePlantedIn = nextBombCanBePlantedIn;
    }

    public void setDead() {
        isDead = true;
    }

    public int getBombRadius() {
        return bombRadius;
    }

    public boolean isDead() {
        return isDead;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", isDead=" + isDead +
                ", position=" + position +
                '}';
    }
}
