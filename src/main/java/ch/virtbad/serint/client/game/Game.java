package ch.virtbad.serint.client.game;

import ch.virtbad.serint.client.engine.content.Camera;
import ch.virtbad.serint.client.game.client.Cinematography;
import ch.virtbad.serint.client.game.client.Controls;
import ch.virtbad.serint.client.game.client.Lighting;
import ch.virtbad.serint.client.game.item.Item;
import ch.virtbad.serint.client.game.item.ItemRegister;
import ch.virtbad.serint.client.game.map.MapRegister;
import ch.virtbad.serint.client.game.map.TileMap;
import ch.virtbad.serint.client.game.objects.positioning.FixedLocation;
import ch.virtbad.serint.client.game.player.Player;
import ch.virtbad.serint.client.game.player.PlayerAttributes;
import ch.virtbad.serint.client.game.player.PlayerRegister;
import ch.virtbad.serint.client.graphics.Scene;
import ch.virtbad.serint.client.networking.Communications;
import ch.virtbad.serint.client.util.Time;
import lombok.extern.slf4j.Slf4j;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Virt
 */
@Slf4j
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

    private volatile boolean joined;


    private GameContext context;
    private float lastTime = Time.getSeconds();

    private Camera camera;
    private Cinematography cinematography;
    private Controls controls;
    private Lighting lighting;

    private PlayerRegister players;
    private ItemRegister items;

    private GameRenderer renderer;

    private MapRegister map;

    private int nearPlayer = -1;
    private int nearItem = -1;


    @Override
    public void init(int width, int height) {
        joined = false;

        camera = new Camera(10, 10);
        camera.setScreenSize(width, height);

        context = new GameContext(camera, keyboard, mouse);

        controls = new Controls(context);
        cinematography = new Cinematography(context);
        players = new PlayerRegister(context);
        items = new ItemRegister(context);
        map = new MapRegister(context);

        lighting = new Lighting();

        renderer = new GameRenderer(width, height, lighting, camera);
    }

    @Override
    public void update() {
        if (!joined) return;

        float currentTime = Time.getSeconds();
        float delta = currentTime - lastTime;

        cinematography.update();
        if (controls.doMovement()) com.pushPlayerLocation(players.getOwn().getLocation());

        players.update(delta);
        lighting.setPlayerVision(players.getOwn());
        synchronized (items){
            items.update(delta);
        }

        map.update(delta);
        players.doMapCollisions(delta, map.getMap().getCollisions());

        // Interactions

        calculateNear();
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
        if (!joined) return; // TODO: What is THIS?

        if (keyboard.isDown(GLFW.GLFW_KEY_TAB)) glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        else glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        // DRAW MAP

        renderer.bindMap();

        glClearColor(0, 0, 0, 0);
        glClear(GL11.GL_COLOR_BUFFER_BIT);

        map.draw();

        synchronized (items){
            items.draw();
        }

        // DRAW PLAYERS

        renderer.bindPlayer();

        glClearColor(0, 0, 0, 0);
        glClear(GL11.GL_COLOR_BUFFER_BIT);

        players.draw();


        renderer.draw();
    }

    @Override
    public void resized(int width, int height) {
        camera.setScreenSize(width, height);
        renderer.resize(width, height);
    }

    public void calculateNear() { //TODO: Do better

        // Maybe use Pythagoras?

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
        joined = true;

        log.info("Joined the Game successfully!");
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
        this.map.create(map);
        lighting.loadMap(map);
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