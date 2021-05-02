package ch.virtbad.serint.client.game.map;

import ch.virtbad.serint.client.config.ConfigHandler;
import ch.virtbad.serint.client.engine.content.Mesh;
import ch.virtbad.serint.client.engine.resources.shaders.Shader;
import ch.virtbad.serint.client.engine.resources.textures.Texture;
import ch.virtbad.serint.client.game.collisions.MapCollisions;
import ch.virtbad.serint.client.game.objects.MeshedGameObject;
import ch.virtbad.serint.client.game.objects.positioning.FixedLocation;
import ch.virtbad.serint.client.graphics.ResourceHandler;
import lombok.Getter;

import static org.lwjgl.opengl.GL11.GL_FLOAT;

/**
 * @author Virt
 */
public class MapObject extends MeshedGameObject {

    public TileMap map;
    public Texture texture;

    @Getter
    private MapCollisions collisions;

    private Mesh cosmetics;
    private Texture cosmeticTexture;
    private Shader cosmeticShader;

    public MapObject(TileMap map) {
        super(new float[0], new int[0], "map", new FixedLocation(), -1);

        this.map = map;
        collisions = new MapCollisions(map);
    }

    @Override
    public void init() {
        mesh = new Mesh(MapOperations.generateVertices(MapOperations.createRenderMap(map.getTiles(), map.getWidth(), map.getHeight()), map.getWidth(), map.getHeight()), MapOperations.generateIndices(map.getWidth(), map.getHeight()));

        texture = ResourceHandler.getTextures().get("tilesheet");

        super.init();

        cosmetics = new Mesh(MapOperations.generateCosmeticVertices(map.getCosmetics()), MapOperations.generateCosmeticIndices(map.getCosmetics()));
        cosmeticTexture = ResourceHandler.getTextures().get("cosmeticsheet");
        cosmeticShader = ResourceHandler.getShaders().get("cosmeticshader");

        cosmetics.init();
        cosmetics.addVertexAttribPointer(0, 2, GL_FLOAT, 4 * Float.BYTES, 0);
        cosmetics.addVertexAttribPointer(1, 2, GL_FLOAT, 4 * Float.BYTES, 2 * Float.BYTES);
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
        shader.uploadInt("enableAspects", ConfigHandler.getConfig().isEnableAspects() ? 1 : 0);
        super.uploadUniforms();
    }

    @Override
    public void draw() {
        super.draw();

        if (ConfigHandler.getConfig().isEnableCosmetics()) {
            cosmeticShader.bind();
            cosmeticShader.uploadMatrix("worldMatrix", worldMatrix);
            cosmeticShader.uploadMatrix("viewMatrix", context.getCamera().getViewMatrix());
            cosmeticShader.uploadInt("textureDimension", 8);
            cosmeticTexture.bind();
            cosmetics.draw();
        }
    }
}
