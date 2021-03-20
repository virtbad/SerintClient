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
        RenderedTile[] renderedTiles = MapOperations.createRenderMap(map.getTiles(), map.getWidth(), map.getHeight());
        mesh = new Mesh(MapOperations.generateVertices(renderedTiles, map.getWidth(), map.getHeight()), MapOperations.generateIndices(renderedTiles, map.getWidth(), map.getHeight()));

        texture = ResourceHandler.getTextures().get("tilesheet");

        super.init();
    }

    @Override
    protected void setAttribPointers() {
        mesh.addVertexAttribPointer(0, 2, GL_FLOAT, 4 * Float.BYTES, 0);
        mesh.addVertexAttribPointer(1, 2, GL_FLOAT, 4 * Float.BYTES, 2 * Float.BYTES);
    }

    @Override
    protected void uploadUniforms() {
        texture.bind();
        super.uploadUniforms();
    }
}
