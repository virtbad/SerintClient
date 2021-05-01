package ch.virtbad.serint.client.game.player;

import ch.virtbad.serint.client.game.GameContext;
import ch.virtbad.serint.client.game.collisions.MapCollisions;
import ch.virtbad.serint.client.game.player.Player;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class keeps track of players
 *
 * @author Virt
 */
@Slf4j
public class PlayerRegister {

    @Getter
    private Player own;
    private boolean ownInit = false;
    @Getter @Setter
    private boolean showOwn = true;

    private HashMap<Integer, Player> players;

    // Initialization and Destruction have to be done on a update since they require the OpenGL context. That's why there are those lists.
    private ArrayList<Integer> toInit;
    private ArrayList<Integer> toDestroy;

    private final GameContext context;

    /**
     * Creates a PlayerRegister
     *
     * @param context context of the game
     */
    public PlayerRegister(GameContext context) {
        this.context = context;
        players = new HashMap<>();
        toInit = new ArrayList<>();
        toDestroy = new ArrayList<>();
    }

    /**
     * Sets the player of the client
     *
     * @param own own client
     */
    public void setOwn(Player own) {
        log.info("Own Player added");
        own.setContext(context);
        this.own = own;
    }

    /**
     * Adds a new player to the register
     *
     * @param player player to add
     */
    public void add(Player player) {
        log.info("Remote Player added");
        player.setContext(context);
        players.put(player.getId(), player);
        toInit.add(player.getId());
    }

    /**
     * Returns the player with the id
     *
     * @param id id of the player
     */
    public Player get(int id) {
        if (own != null && own.getId() == id) return own;
        return players.get(id);
    }

    /**
     * Get all players
     * @return array with all players
     */

    public Player[] getAll() {
        return players.values().toArray(new Player[0]);
    }

    /**
     * Removes a player from the register
     *
     * @param id player to remove
     */
    public void remove(int id) {
        toDestroy.add(id);
    }

    /**
     * Updates all players
     *
     * @param delta delta time
     */
    public void update(float delta) {

        // Init and Update Own
        if (own != null) {
            if (!ownInit) {
                own.init();
                ownInit = true;
            }

            own.update(delta);
        }

        // Initialize uninitialized Things
        if (toInit.size() != 0) {
            for (Integer integer : toInit) {
                players.get(integer).init();
            }

            toInit.clear();
        }

        // Destroy uninitialized Things
        if (toDestroy.size() != 0) {
            for (Integer integer : toDestroy) {
                if (!players.containsKey(integer)) continue;
                players.remove(integer).destroy();
            }

            toDestroy.clear();
        }

        // Update Objects
        for (Player value : players.values()) {
            value.update(delta);
        }
    }

    public void doMapCollisions(float delta, MapCollisions collisions) {
        if (own != null) own.getLocation().timeCollided(delta, collisions.checkCollisions(own.getBounds()));

        for (Player value : players.values()) {
            value.getLocation().timeCollided(delta, collisions.checkCollisions(value.getBounds()));
        }
    }

    /**
     * Draws all players
     */
    public void draw() {
        if (showOwn && own != null) {
            own.draw();
        }

        for (Player value : players.values()) {
            value.draw();
        }
    }
}
