package geometry;

/**
 * Created by a.pomosov on 25/12/2017.
 */
public class Geometry {
    public static boolean isInRadius(Position target, Position center, int radius) {
        return ((target.getX() == center.getX()) && (target.getY() >= center.getY() - radius) && (target.getY() <= center.getY() + radius)) ||
                ((target.getY() == center.getY()) && (target.getX() >= center.getX() - radius) && (target.getX() <= center.getX() + radius));
    }
}
