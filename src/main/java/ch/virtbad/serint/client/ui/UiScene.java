package ch.virtbad.serint.client.ui;

import ch.virtbad.serint.client.engine.content.Camera;
import ch.virtbad.serint.client.graphics.Scene;
import ch.virtbad.serint.client.ui.components.base.Component;
import ch.virtbad.serint.client.util.Time;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

/**
 * This class is a scene that is able to store and update ui components
 * @author Virt
 */
public abstract class UiScene extends Scene {

    protected Context context;
    protected Camera camera;
    protected Vector4f background = new Vector4f(0, 0, 0, 1);

    private ArrayList<Component> components;

    private final float minUnits;

    private float lastTime;

    /**
     * Creates a ui scene
     * @param minUnits minimal units in width / height
     */
    public UiScene(float minUnits) {
        this.minUnits = minUnits;
    }

    /**
     * In this method, the ui components should be composed
     */
    public abstract void build();

    @Override
    public void init(int width, int height) {
        components = new ArrayList<>();

        camera = new Camera(minUnits, minUnits);
        camera.setScreenSize(width, height);

        context = new Context(camera, keyboard, mouse);

        build();
    }

    protected void addComponent(Component component){
        component.setContext(context);
        component.init();
        components.add(component);
    }

    @Override
    public void update() {
        float time = Time.getSeconds();
        float delta = time - lastTime;

        for (Component component : components) {
            component.update(delta);
        }

        lastTime = time;
    }

    @Override
    public void draw() {
        glClearColor(background.x, background.y, background.z, background.w);
        glClear(GL11.GL_COLOR_BUFFER_BIT);

        for (Component component : components) {
            component.draw();
        }
    }

    @Override
    public void resized(int width, int height) {
        camera.setScreenSize(width, height);
    }

    @Override
    public void shown() {

    }
}
