package ch.virtbad.serint.client.game.item;

import ch.virtbad.serint.client.engine.resources.Texture;
import ch.virtbad.serint.client.game.objects.MeshedGameObject;
import ch.virtbad.serint.client.game.objects.positioning.FixedLocation;
import ch.virtbad.serint.client.graphics.ResourceHandler;
import ch.virtbad.serint.client.util.Time;
import lombok.Getter;

import static org.lwjgl.opengl.GL11.GL_FLOAT;

/**
 * @author Virt
 */
public class Item extends MeshedGameObject {

    @Getter
    private int id;

    private Texture texture;

    public Item(int id, FixedLocation location) {
        super(ItemOperations.getVertices(id), ItemOperations.getIndices(), "item", location, 1);

        this.id = id;
    }

    @Override
    public void init() {

        texture = ResourceHandler.getTextures().get("itemsheet");

        super.init();
    }

    @Override
    protected void uploadUniforms() {
        texture.bind();
        shader.uploadInt("textureDimension", 8);
        shader.uploadFloat("time", Time.getSeconds());
        super.uploadUniforms();
    }

    @Override
    protected void setAttribPointers() {
        mesh.addVertexAttribPointer(0, 2, GL_FLOAT, 4 * Float.BYTES, 0);
        mesh.addVertexAttribPointer(1, 2, GL_FLOAT, 4 * Float.BYTES, 2 * Float.BYTES);
    }
}
