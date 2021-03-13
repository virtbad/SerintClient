package ch.virtbad.serint.client.game;

import ch.virtbad.serint.client.engine.content.Camera;
import ch.virtbad.serint.client.game.features.Player;
import ch.virtbad.serint.client.graphics.Scene;
import ch.virtbad.serint.client.networking.Communications;
import ch.virtbad.serint.client.util.Time;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

/**
 * @author Virt
 */
public class Game extends Scene {

    private Communications communications;
    public Game(Communications communications){
        this.communications = communications;
        //communications.setGame(this); //TODO: Comment in PLS
    }

    private GameContext context;
    private float lastTime = Time.getSeconds();

    private Camera camera;

    private Player player;

    public void join(){

    }

    @Override
    public void init() {

        camera = new Camera(10, 10);
        camera.setScreenSize(1080, 720); //TODO: Dynamic


        context = new GameContext(camera, keyboard, mouse);

        player = new Player();
        player.setContext(context);
        player.init();
    }

    @Override
    public void update() {
        //System.out.println("updating");
        glClearColor(0, 0, 0, 1);
        glClear(GL11.GL_COLOR_BUFFER_BIT);

        float currentTime = Time.getSeconds();
        float delta = lastTime - currentTime;

        player.update(delta);

        lastTime = currentTime;
    }

    @Override
    public void draw() {
        player.draw();
    }
}
