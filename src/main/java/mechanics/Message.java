package mechanics;

import gameobjects.Player;

/**
 * Created by a.pomosov on 25/12/2017.
 */
public class Message {
    private final Player player;
    private final Action action;

    public Message(Player player, Action action) {
        this.player = player;
        this.action = action;
    }

    public Player getPlayer() {
        return player;
    }

    public Action getAction() {
        return action;
    }
}
