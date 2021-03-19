package ch.virtbad.serint.client.game;

import ch.virtbad.serint.client.engine.content.Camera;
import ch.virtbad.serint.client.game.features.Player;
import ch.virtbad.serint.client.game.registers.PlayerRegister;
import ch.virtbad.serint.client.graphics.Scene;
import ch.virtbad.serint.client.networking.Communications;
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

    private Communications communications;

    /**
     * Creates a game scene
     * @param communications Communications for game to be based on
     */
    public Game(Communications communications){
        this.communications = communications;
        communications.setGame(this);
    }


    private GameContext context;
    private float lastTime = Time.getSeconds();

    private Camera camera;

    private PlayerRegister players;


    @Override
    public void init() {

        camera = new Camera(10, 10);
        camera.setScreenSize(1080, 720); //TODO: Dynamic

        context = new GameContext(camera, keyboard, mouse);

        players = new PlayerRegister(context);
    }

    @Override
    public void update() {
        glClearColor(0, 0, 0, 1);
        glClear(GL11.GL_COLOR_BUFFER_BIT);

        float currentTime = Time.getSeconds();
        float delta = lastTime - currentTime;

        controls();
        players.update(delta);

        lastTime = currentTime;
    }

    @Override
    public void draw() {
        players.draw();
    }


    /**
     * This methods takes input from the user and applies velocities to the player.
     * It should be replaced by a specific controller class one day
     */
    @Deprecated
    public void controls(){
        if (players.getOwn() == null) return; // Only run if local player has already been created

        float moveVelocity = 4;
        float velocityX = 0;
        float velocityY = 0;

        if (context.getKeyboard().isDown(GLFW.GLFW_KEY_UP)) velocityY = moveVelocity;
        if (context.getKeyboard().isDown(GLFW.GLFW_KEY_DOWN)) velocityY = -moveVelocity;
        if (context.getKeyboard().isDown(GLFW.GLFW_KEY_RIGHT)) velocityX = moveVelocity;
        if (context.getKeyboard().isDown(GLFW.GLFW_KEY_LEFT)) velocityX = -moveVelocity;

        boolean updated = players.getOwn().getLocation().getVelocityX() != velocityX || players.getOwn().getLocation().getVelocityY() != velocityY;

        players.getOwn().getLocation().setVelocityX(velocityX);
        players.getOwn().getLocation().setVelocityY(velocityY);

        if (updated) communications.pushPlayerLocation(players.getOwn().getLocation());
    }

    /**
     * Is called when the player has successfully joined the game.
     * @param ownId assigned id by the server
     */
    public void joined(int ownId){
        players.setOwn(new Player(ownId, new Vector3f(1, 1, 0), "Own Test")); // TODO: Replace info here with actual info sent to the server
    }

    /**
     * Creates a remote player
     * @param id id of the player
     * @param color colour of the player
     * @param name name of the player
     */
    public void createPlayer(int id, int color, String name){
        Color c = new Color(color);
        players.add(new Player(id, new Vector3f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f), name));
    }

    /**
     * Destroys / removes a remote player
     * @param id if of the player
     */
    public void destroyPlayer(int id){
        players.remove(id);
    }

    /**
     * Sets the Position and Velocity of a player
     * @param id id of the player
     * @param x x coordinate of player
     * @param y y coordinate of player
     * @param velX x velocity of player
     * @param velY y velocity of player
     */
    public void relocatePlayer(int id, float x, float y, float velX, float velY){
        Player p = players.get(id);

        p.getLocation().setPosX(x);
        p.getLocation().setPosY(y);
        p.getLocation().setVelocityX(velX);
        p.getLocation().setVelocityY(velY);
    }
}
