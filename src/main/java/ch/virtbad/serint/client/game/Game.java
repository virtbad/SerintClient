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
    public static final float INTERACTION_RADIUS = 2;
    public static final float KILL_COOLDOWN = 10;

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


    private volatile boolean ingame;
    private volatile boolean joined;

    private GameContext context;
    private float lastTime = Time.getSeconds();


    private Camera camera;
    private Cinematography cinematography;
    private Controls controls;
    private Lighting lighting;
    private GameUI gui;

    private GameRenderer renderer;


    private PlayerRegister players;
    private ItemRegister items;
    private MapRegister map;


    // Ingame Variables
    private float lastKill = Time.getSeconds();
    private boolean showPlayer = true;


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

        // Create UI
        gui = new GameUI(() -> {
            // Disconnect

            com.disconnect();
            switchScene(4);

        }, () -> {
            // Back to game

            if (gui.isPaused()){
                gui.hideScreen();
                ingame = true;
            }

        });
        gui.setKeyboard(keyboard);
        gui.setMouse(mouse);
        gui.init(width, height);
        gui.setSceneSwitcher(this::switchScene);
        gui.hideHud();

        lighting = new Lighting();

        renderer = new GameRenderer(width, height, lighting, camera);
    }

    @Override
    public void update() {
        if (!joined) return;

        float currentTime = Time.getSeconds();
        float delta = currentTime - lastTime;

        cinematography.update();
        if (ingame && controls.doMovement()) com.pushPlayerLocation(players.getOwn().getLocation());

        players.update(delta);
        lighting.setPlayerVision(players.getOwn(), showPlayer);
        synchronized (items){
            items.update(delta);
        }

        map.update(delta);
        players.doMapCollisions(delta, map.getMap().getCollisions());

        // Interactions
        if(ingame && controls.isAttacking() && lastKill + KILL_COOLDOWN < Time.getSeconds() ) {

            // Do cooldown anyway
            lastKill = Time.getSeconds();
            gui.setLastKill(lastKill);

            for (Player player : players.getAll()) {
                if(player.getId() == players.getOwn().getId()) continue;

                if (players.getOwn().getLocation().distanceTo(player.getLocation()) < INTERACTION_RADIUS) {
                    com.attackPlayer(player.getId());
                    break;
                }
            }
        }

        if(ingame && controls.isCollecting()) {

            for (Item item : items.getAll()) {
                if (players.getOwn().getLocation().distanceTo(item.getLocation()) < INTERACTION_RADIUS) {
                    com.collectItem(item.getId());
                    break;
                }
            }

        }

        // Pause
        if (controls.isPausing()){
            if (gui.isPaused()){
                gui.hideScreen();
                ingame = true;
            }
            else if (ingame) {
                ingame = false;
                gui.showPauseScreen();
            }
        }

        gui.update();

        lastTime = currentTime;
    }

    @Override
    public void draw() {
        if (!joined) return;

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

        // DRAW GUI

        renderer.bindGui();
        glClearColor(1, 0, 0, 0);
        glClear(GL11.GL_COLOR_BUFFER_BIT);

        gui.draw();

        renderer.draw();
    }

    @Override
    public void resized(int width, int height) {
        camera.setScreenSize(width, height);
        renderer.resize(width, height);
        gui.resized(width, height);
    }

    @Override
    public void shown() {

    }

    private void setPlayerVisible(boolean whether){
        showPlayer = whether;
        players.setShowOwn(whether);
    }

    /**
     * Is called when the player has successfully joined the game.
     *
     * @param ownId assigned id by the server
     */
    public void joined(int ownId, Color ownColor, String ownName) {
        players.setOwn(new Player(ownId, new Vector3f(ownColor.getRed() / 255f, ownColor.getGreen() / 255f, ownColor.getBlue() / 255f), ownName));
        gui.setOwn(players.getOwn());
        controls.setPlayer(players.getOwn());
        cinematography.follow(players.getOwn().getLocation());
        joined = true;
        ingame = true;

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

    public void absorbed(float delay, String issuer){
        log.info("Absorbed!");

        gui.showDeathScreen(issuer, delay);
        renderer.setIngui(true);
        setPlayerVisible(false);
        ingame = false;
    }

    public void respawn(){
        log.info("Respawned!");

        gui.hideScreen();
        renderer.setIngui(false);
        setPlayerVisible(true);
        ingame = true;
    }

    public void loose(String lastIssuer){
        gui.showLoseScreen(lastIssuer);
        renderer.setIngui(true);
        setPlayerVisible(false);
        ingame = false;
    }

    public void win(){
        gui.showWinScreen();
        renderer.setIngui(true);
        ingame = false;
    }

    public void peakStart(float delay){
        gui.showStartScreen(delay);
    }

    public void start(){
        gui.hideScreen();

        // Start cooldown
        lastKill = Time.getSeconds();
        gui.setLastKill(lastKill);
    }
}