package gameobjects;

import geometry.Position;

/**
 * Created by a.pomosov on 25/12/2017.
 */
public class Bonus extends GameObject implements Positionable {
    private final Position position;

    public Bonus(Position position) {
        this.position = position;
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
