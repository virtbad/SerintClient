package ch.virtbad.serint.client.game.map;

import ch.virtbad.serint.client.engine.content.Mesh;
import ch.virtbad.serint.client.engine.resources.Texture;
import ch.virtbad.serint.client.engine.resources.TextureLoader;
import ch.virtbad.serint.client.game.objects.MeshedGameObject;
import ch.virtbad.serint.client.game.objects.positioning.FixedLocation;
import ch.virtbad.serint.client.graphics.ResourceHandler;

import static org.lwjgl.opengl.GL11.GL_FLOAT;

/**
 * @author Virt
 */
public class MapObject extends MeshedGameObject {

    public TileMap map;
    public Texture texture;

    public MapObject(TileMap map) {
        super(new float[0], new int[0], "map", new FixedLocation(), -1);

        this.map = map;
    }

    @Override
    public void init() {
        mesh = new Mesh(MapOperations.generateVertices(MapOperations.createRenderMap(map.getTiles(), map.getWidth(), map.getHeight()), map.getWidth(), map.getHeight()), MapOperations.generateIndices(map.getWidth(), map.getHeight()));

        texture = ResourceHandler.getTextures().get("tilesheet");

        super.init();
    }

    @Override
    protected void setAttribPointers() {
        int stride = (2 + MapOperations.TEXTURE_LAYERS * 2) * Float.BYTES; // Since all are of length 2
        int index = 0;
        int type = GL_FLOAT;

        mesh.addVertexAttribPointer(index++, 2, type, stride, 0);

        for (int i = 0; i < MapOperations.TEXTURE_LAYERS; i++) {
            mesh.addVertexAttribPointer(index++, 2, type, stride, (index - 1) * 2 * Float.BYTES);
        }
    }

    @Override
    protected void uploadUniforms() {
        texture.bind();
        shader.uploadFloat("textureDimension", 16f);
        super.uploadUniforms();
    }
}
