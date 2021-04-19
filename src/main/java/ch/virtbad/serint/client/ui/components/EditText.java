package ch.virtbad.serint.client.ui.components;

import ch.virtbad.serint.client.engine.events.BasicEvent;
import ch.virtbad.serint.client.engine.events.CharEvent;
import ch.virtbad.serint.client.engine.events.EventHelper;
import ch.virtbad.serint.client.engine.resources.textures.Texture;
import ch.virtbad.serint.client.graphics.ResourceHandler;
import ch.virtbad.serint.client.ui.UiHelper;
import ch.virtbad.serint.client.ui.components.base.PositionedComponent;
import ch.virtbad.serint.client.ui.components.base.QuadComponent;
import ch.virtbad.serint.client.ui.components.font.Text;
import ch.virtbad.serint.client.util.Time;
import lombok.Setter;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.*;

public class EditText extends QuadComponent {

    private String content;
    private boolean changed;
    private Text text;
    private int maxLength;

    private final float scale;
    private final boolean centered;

    private boolean pressed;
    private boolean focused;

    private boolean backspace;
    private boolean enter;

    private Texture texture;
    private float carriageX, carriageY, carriageWidth, carriageHeight;

    @Setter
    private BasicEvent submitListener;

    public EditText(float x, float y, float width, float height, float scale, boolean centered, int maxLength) {
        super(x, y, width, height, "edittext", true);
        content = "";
        this.maxLength = maxLength;

        this.scale = scale;
        this.centered = centered;
    }

    @Override
    public void init() {
        super.init();

        text = new Text(content, new Vector4f(1, 1, 1, 1), x, y, scale);
        text.setCamera(context.getCamera());
        text.init();

        updateText();

        texture = ResourceHandler.getTextures().get("edittext");
    }

    private void updateText(){
        text.setText(content);

        if (centered) text.setPosition(x + width / 2 - text.getWidth() / 2, y + height / 2 - text.getHeight() / 2);
        else text.setPosition(x + scale / 2, y + height / 2 - text.getHeight() / 2);

        carriageX = centered ? x + width / 2 + text.getWidth() / 2 : x + scale / 2 + text.getWidth();
        carriageY = y + height / 2 - text.getHeight() / 2;
        carriageHeight = text.getHeight();
        carriageWidth = 0.05f;
    }

    private void getFocus(){
        focused = true;
        context.getKeyboard().setCharListener(this::callback);
    }

    private void loseFocus(){
        focused = false;
    }

    private void callback(char letter){
        if (!focused) return;

        if (content.length() < maxLength) {
            content += letter;
            changed = true;
        }
    }

    @Override
    protected void uploadUniforms() {
        super.uploadUniforms();

        shader.uploadFloat("carriageX", carriageX);
        shader.uploadFloat("carriageY", carriageY);
        shader.uploadFloat("carriageWidth", carriageWidth);
        shader.uploadFloat("carriageHeight", carriageHeight);

        shader.uploadFloat("time", Time.getSeconds());

        shader.uploadInt("state", focused ? 1 : 0);
        texture.bind();
    }

    @Override
    public void update(float delta) {
        // Do focus processing

        boolean currentPressed = context.getMouse().isDown(GLFW_MOUSE_BUTTON_1);

        if (!currentPressed && pressed){
            if (UiHelper.mouseHovering(x, y, width, height, context)) getFocus();
            else loseFocus();
        }

        pressed = currentPressed;

        if (!focused) return;
        // Do text input processing

        boolean currentEnter = context.getKeyboard().isDown(GLFW_KEY_ENTER);
        boolean currentBackspace = context.getKeyboard().isDown(GLFW_KEY_BACKSPACE);

        // Check whether released
        if (!currentBackspace && backspace && content.length() != 0){
            content = content.substring(0, content.length() - 1);
            changed = true;
        }

        if (!currentEnter && enter){
            EventHelper.emitEvent(submitListener);
            loseFocus();
        }

        enter = currentEnter;
        backspace = currentBackspace;

        if (changed) updateText();
    }

    @Override
    public void draw() {
        super.draw();
        text.draw();
    }
}
