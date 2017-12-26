package mechanics;

import gameobjects.*;
import geometry.Geometry;
import geometry.Position;
import map.GameMap;
import tick.Tickable;
import tick.Ticker;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameSession implements Tickable {
    private static final int STEP_SIZE = 1;
    public final Queue<Message> messages = new ConcurrentLinkedQueue<>();
    public final List<Player> players = new ArrayList<>();
    private final List<Bomb> bombs = new ArrayList<>();
    private final List<Bonus> bonuses = new ArrayList<>();
    private final List<Wall> walls = new ArrayList<>();
    private final List<UnbreakableWall> unbreakableWalls = new ArrayList<>();
    private int mapWidth;
    private int mapHeight;
    private final Ticker ticker = new Ticker();
    public volatile boolean gameOver = false;

    public GameSession(GameMap gameMap) {
        initGameMap(gameMap);
    }

    private void initGameMap(GameMap gameMap) {
        //TODO init from file
        this.players.add(new Player("Sasha", new Position(0, 0)));
        this.players.add(new Player("Sergey", new Position(9, 9)));
        mapHeight = 10;
        mapWidth = 10;
    }

    public void start() {
        ticker.registerTickable(this);
        ticker.gameLoop();
    }

    @Override
    public void tick(long elapsed) {
        tickPlayers(elapsed);
        tickBombs(elapsed);
        replicate();
        checkGameOver();
    }

    private void replicate() {
        for (Player player : players) {
            System.out.println(player);
        }
    }

    private void checkGameOver() {
        if (players.stream().filter(p -> !p.isDead()).count() <= 1) {
            gameOver = true;
            ticker.stop();
            System.out.println("===== GAME OVER =====");
            Optional<Player> winner = players.stream().filter(p -> !p.isDead()).findFirst();
            if (winner.isPresent()) {
                System.out.println("Player " + winner.get() + " wins!");
            } else {
                System.out.println("Draw!");
            }
        }
    }

    private void tickPlayers(long elapsed) {
        for (Player player : players) {
            Optional<Message> movement = messages.stream().filter(m -> m.getPlayer().equals(player)).filter(m ->
                    m.getAction().equals(Action.MOVE_DOWN) ||
                            m.getAction().equals(Action.MOVE_UP) ||
                            m.getAction().equals(Action.MOVE_LEFT) ||
                            m.getAction().equals(Action.MOVE_RIGHT)).findFirst();
            Optional<Message> bombPlanted = messages.stream().filter(m -> m.getPlayer().equals(player)).filter(m ->
                    m.getAction().equals(Action.PLANT_BOMB)).findFirst();
            movement.ifPresent(message -> tryMove(player, message.getAction(), elapsed));
            if (bombPlanted.isPresent()) {
                tryPlantBomb(player);
            }
        }
        messages.clear();
    }

    private void tickBombs(long elapsed) {
        List<Bomb> bombsToRemove = new ArrayList<>();
        for (Bomb bomb : bombs) {
            if (bomb.tick(elapsed)) {
                bombBoom(bomb);
                bombsToRemove.add(bomb);
            }
        }
        bombs.removeAll(bombsToRemove);
    }

    private void bombBoom(Bomb bomb) {
        System.out.println(bomb + " BOOM!");
        for (Player player : players) {
            if (Geometry.isInRadius(player.getPosition(), bomb.getPosition(), bomb.getRadius())) {
                player.setDead();
                System.out.println(player + " IS DEAD!");
            }
        }
        List<Wall> wallsToRemove = new ArrayList<>();
        for (Wall wall : walls) {
            if (Geometry.isInRadius(wall.getPosition(), bomb.getPosition(), bomb.getRadius())) {
                wallsToRemove.add(wall);
            }
        }
        walls.removeAll(wallsToRemove);
    }

    private boolean tryPlantBomb(Player player) {
        if (!player.isBombCanBePlanted()) {
            return false;
        }
        Bomb bomb = new Bomb(player.getPosition(), player.getBombRadius());
        bombs.add(bomb);
        System.out.println(player + " set " + bomb);
        return true;
    }

    private void tryMove(Player player, Action action, long elapsed) {
        for (int step = 0; step < player.getSpeed(); step++) {
            Position newPosition;
            if (action == Action.MOVE_DOWN) {
                newPosition = new Position(player.getPosition().getX(), player.getPosition().getY() + STEP_SIZE);
            } else if (action == Action.MOVE_UP) {
                newPosition = new Position(player.getPosition().getX(), player.getPosition().getY() - STEP_SIZE);
            } else if (action == Action.MOVE_RIGHT) {
                newPosition = new Position(player.getPosition().getX() + STEP_SIZE, player.getPosition().getY());
            } else if (action == Action.MOVE_LEFT) {
                newPosition = new Position(player.getPosition().getX() - STEP_SIZE, player.getPosition().getY());
            } else {
                newPosition = player.getPosition();
            }
            if (newPosition.getX() > mapWidth || newPosition.getX() < 0 || newPosition.getY() > mapHeight || newPosition.getY() < 0) {
                return;
            }
            for (Wall wall : walls) {
                if (player.getPosition().equals(wall.getPosition())) {
                    return;
                }
            }
            for (UnbreakableWall unbreakableWall : unbreakableWalls) {
                if (player.getPosition().equals(unbreakableWall.getPosition())) {
                    return;
                }
            }
            player.setPosition(newPosition);
        }
    }
}