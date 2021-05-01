package ch.virtbad.serint.client.ui.components;

import ch.virtbad.serint.client.engine.content.Mesh;
import ch.virtbad.serint.client.engine.content.MeshHelper;
import ch.virtbad.serint.client.engine.resources.textures.Texture;
import ch.virtbad.serint.client.graphics.ResourceHandler;
import ch.virtbad.serint.client.ui.UiHelper;
import ch.virtbad.serint.client.ui.components.base.QuadComponent;
import ch.virtbad.serint.client.ui.components.font.Text;
import lombok.Getter;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

public class NumberSpinner extends QuadComponent {

    Texture texture;
    int minusShift = 0;
    int plusShift = 0;

    boolean plusHover, plusPress;
    boolean minusHover, minusPress;

    float step;
    @Getter
    float number;
    Text display;

    public NumberSpinner(float x, float y, float width, float height, float start, float step) {
        super(x, y, width, height, null, true);

        number = start;
        this.step = step;
    }

    @Override
    public void init() {
        shader = ResourceHandler.getShaders().get("numberspinner");
        texture = ResourceHandler.getTextures().get("numberspinner");

        float xOffset = width - height;

        mesh = new Mesh(new float[] {
                0     , 0     , 0, 0.5f,
                height, 0     , 1, 0.5f,
                0     , height, 0, 0   ,
                height, height, 1, 0   ,

                xOffset, 0     , 0, 1   ,
                width  , 0     , 1, 1   ,
                xOffset, height, 0, 0.5f,
                width  , height, 1, 0.5f,
        }, new int[] {
                0, 1, 3,
                0, 3, 2,

                4, 5, 7,
                4, 7, 6
        });

        mesh.init();
        setAttributePointers();

        updateWorldMatrix();

        display = new Text("" + number, new Vector4f(1, 1, 1, 1), 0, 0, 0.5f);
        display.setCamera(context.getCamera());
        display.init();

        display.setPosition(x + width / 2 - display.getWidth() / 2, y + 0.25f);
    }

    @Override
    protected void uploadUniforms() {
        texture.bind();

        shader.uploadInt("minusShift", minusShift);
        shader.uploadInt("plusShift", plusShift);

        super.uploadUniforms();
    }

    @Override
    public void update(float delta) {
        boolean pressing = context.getMouse().isDown(GLFW.GLFW_MOUSE_BUTTON_1);
        boolean changed = false;

        if (UiHelper.mouseHovering(x, y, height, height, context)){
            minusShift = pressing ? 2 : 1;
            minusHover = true;

            if (pressing) {
                minusPress = true;
            }else {
                if (minusPress){
                    number -= step;
                    changed = true;
                }
                minusPress = false;
            }

        }else {
            minusShift = 0;
            minusPress = false;
            minusHover = false;
        }

        if (UiHelper.mouseHovering(width - height, y, height, height, context)){
            plusShift = pressing ? 2 : 1;
            plusHover = true;

            if (pressing) {
                plusPress = true;
            }else {
                if (plusPress) {
                    number += step;
                    changed = true;
                }
                plusPress = false;
            }

        }else {
            plusShift = 0;
            plusPress = false;
            plusHover = false;
        }

        if (changed) {
            display.setText("" + number);
            display.setPosition(x + width / 2 - display.getWidth() / 2, y + 0.25f);
        }
    }

    @Override
    public void draw() {
        super.draw();

        display.draw();
    }
}
