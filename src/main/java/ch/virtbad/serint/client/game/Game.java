package ch.virtbad.serint.client.game;

import ch.virtbad.serint.client.engine.content.Camera;
import ch.virtbad.serint.client.game.client.Cinematography;
import ch.virtbad.serint.client.game.client.Controls;
import ch.virtbad.serint.client.game.collisions.CollisionResult;
import ch.virtbad.serint.client.game.item.Item;
import ch.virtbad.serint.client.game.item.ItemRegister;
import ch.virtbad.serint.client.game.map.MapObject;
import ch.virtbad.serint.client.game.map.TileMap;
import ch.virtbad.serint.client.game.objects.positioning.FixedLocation;
import ch.virtbad.serint.client.game.player.Player;
import ch.virtbad.serint.client.game.player.PlayerAttributes;
import ch.virtbad.serint.client.game.player.PlayerRegister;
import ch.virtbad.serint.client.graphics.Scene;
import ch.virtbad.serint.client.networking.Communications;
import ch.virtbad.serint.client.util.Globals;
import ch.virtbad.serint.client.util.Time;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Virt
 */
public class Game extends Scene {
    private static final float INTERACTION_RADIUS = 1;

    private Communications com;

    /**
     * Creates a game scene
     *
     * @param communications Communications for game to be based on
     */
    public Game(Communications communications) {
        this.com = communications;
        communications.setGame(this);
    }


    private GameContext context;
    private float lastTime = Time.getSeconds();

    private Camera camera;
    private Cinematography cinematography;
    private Controls controls;

    private PlayerRegister players;
    private ItemRegister items;

    private MapObject map;
    boolean mapInit = true;

    private float lastS = 0;

    private int nearPlayer = -1;
    private int nearItem = -1;


    @Override
    public void init() {

        camera = new Camera(10, 10);
        camera.setScreenSize(1080, 720); //TODO: Dynamic

        context = new GameContext(camera, keyboard, mouse);

        controls = new Controls(context);
        cinematography = new Cinematography(context);
        players = new PlayerRegister(context);
        items = new ItemRegister(context);
    }

    @Override
    public void update() { // TODO: More Responsive update Design

        float currentTime = Time.getSeconds();
        float delta = lastTime - currentTime;

        if (controls.doMovement()) com.pushPlayerLocation(players.getOwn().getLocation());
        players.update(delta);
        items.update(delta);

        cinematography.update();

        if (!mapInit) {
            map.setContext(context);
            map.init();
            mapInit = true;
        }
        if (map != null) {
            map.update(delta);

            players.doMapCollisions(delta, map.getCollisions());
        }

        if (players.getOwn() != null) calculateNear();

        if(nearPlayer != -1 && controls.isAttacking()) {
            com.attackPlayer(nearPlayer);
        }
        if(nearItem != -1 && controls.isCollecting()) {
            com.collectItem(nearItem);
        }


        lastTime = currentTime;
    }

    @Override
    public void draw() {
        glClearColor(0, 0, 0, 1);
        glClear(GL11.GL_COLOR_BUFFER_BIT);

        if (keyboard.isDown(GLFW.GLFW_KEY_TAB)) glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        else glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        if (map != null && mapInit) map.draw();

        synchronized (items){
            items.draw();
        }

        players.draw();
    }

    public void calculateNear() {
        Item[] currentItems = items.getAll();
        Player[] currentPlayers = players.getAll();
        Player own = players.getOwn();
        float startX = own.getLocation().getPosX() - INTERACTION_RADIUS;
        float startY = own.getLocation().getPosY() - INTERACTION_RADIUS;
        float endX = own.getLocation().getPosX() + INTERACTION_RADIUS;
        float endY = own.getLocation().getPosY() + INTERACTION_RADIUS;
        boolean itemNear = false;
        boolean playerNear = false;


        for (Item item : currentItems) {
            float posX = item.getLocation().getPosX();
            float posY = item.getLocation().getPosY();
            if (posX >= startX && posX <= endX && posY >= startY && posY <= endY) {
                nearItem = item.getId();
                itemNear = true;
                break;
            }
        }
        if (!itemNear) nearItem = -1;

        for (Player player : currentPlayers) {
            if(player.getId() == own.getId()) continue;
            float posX = player.getLocation().getPosX();
            float posY = player.getLocation().getPosY();
            if (posX >= startX && posX <= endX && posY >= startY && posY <= endY) {
                nearPlayer = player.getId();
                playerNear = true;
                break;
            }
        }
        if (!playerNear) nearPlayer = -1;
    }

    /**
     * Is called when the player has successfully joined the game.
     *
     * @param ownId assigned id by the server
     */
    public void joined(int ownId) {
        players.setOwn(new Player(ownId, new Vector3f(1, 1, 0), "Own Test")); // TODO: Replace info here with actual info sent to the server
        controls.setPlayer(players.getOwn());
        cinematography.follow(players.getOwn().getLocation());
    }

    /**
     * Creates a remote player
     *
     * @param id    id of the player
     * @param color colour of the player
     * @param name  name of the player
     */
    public void createPlayer(int id, int color, String name) {
        Color c = new Color(color);
        players.add(new Player(id, new Vector3f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f), name));
    }

    /**
     * Destroys / removes a remote player
     *
     * @param id if of the player
     */
    public void destroyPlayer(int id) {
        players.remove(id);
    }

    /**
     * Sets the Position and Velocity of a player
     *
     * @param id   id of the player
     * @param x    x coordinate of player
     * @param y    y coordinate of player
     * @param velX x velocity of player
     * @param velY y velocity of player
     */
    public void relocatePlayer(int id, float x, float y, float velX, float velY) {
        Player p = players.get(id);
        p.getLocation().setPosX(x);
        p.getLocation().setPosY(y);
        p.getLocation().setVelocityX(velX);
        p.getLocation().setVelocityY(velY);
    }

    public void updatePlayerAttributes(int id, PlayerAttributes attributes) {
        players.get(id).setAttributes(attributes);
    }

    public void createMap(TileMap map) {
        this.map = new MapObject(map);
        mapInit = false;
    }

    public void createItem(int id, float x, float y, int type) {
        synchronized (items) {
            items.add(new Item(type, new FixedLocation(x, y)), id);
        }
    }

    public void destroyItem(int id) {
        synchronized (items) {
            items.remove(id);
        }
    }
}