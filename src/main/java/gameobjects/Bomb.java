package gameobjects;

import geometry.Position;
import tick.Tickable;

/**
 * Created by a.pomosov on 25/12/2017.
 */
public class Bomb extends GameObject implements Positionable {
    public static long BOMB_COUNTDOWN = 3000;
    private final Position position;
    private long countdown = BOMB_COUNTDOWN;
    private int radius;

    public Bomb(Position position, int radius) {
        this.position = position;
        this.radius = radius;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    public boolean tick(long elapsed) {
        countdown -= elapsed;
        System.out.println(this + "[" + countdown + "]");
        if(countdown <= 0){
            return true;
        }
        return false;
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return "Bomb{" +
                "position=" + position +
                '}';
    }
}
