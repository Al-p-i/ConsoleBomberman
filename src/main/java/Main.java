import map.GameMap;
import mechanics.Action;
import mechanics.GameSession;
import mechanics.Message;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by a.pomosov on 25/12/2017.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        while (askForNextGame()) {
            GameSession game = createGame();
            new Thread(() -> {
                while (!game.gameOver) {
                    try {
                        int command = System.in.read();
                        if (command == 'w') {
                            game.messages.add(new Message(game.players.get(0), Action.MOVE_UP));
                        }
                        if (command == 's') {
                            game.messages.add(new Message(game.players.get(0), Action.MOVE_DOWN));
                        }
                        if (command == 'a') {
                            game.messages.add(new Message(game.players.get(0), Action.MOVE_LEFT));
                        }
                        if (command == 'd') {
                            game.messages.add(new Message(game.players.get(0), Action.MOVE_RIGHT));
                        }
                        if (command == ' ') {
                            game.messages.add(new Message(game.players.get(0), Action.PLANT_BOMB));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, "input-reader").start();
            game.start();
        }
    }

    private static boolean askForNextGame() {
        System.out.println("To play new game press any key, to exit print 'q'");
        try {
            return (System.in.read() != 'q');
        } catch (IOException e) {
            return false;
        }
    }

    private static GameSession createGame() {
        return new GameSession(new GameMap());
    }
}
