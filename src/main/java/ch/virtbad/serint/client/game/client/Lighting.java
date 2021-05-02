package ch.virtbad.serint.client.game.client;

import ch.virtbad.serint.client.config.ConfigHandler;
import ch.virtbad.serint.client.engine.resources.shaders.Shader;
import ch.virtbad.serint.client.game.map.TileMap;
import ch.virtbad.serint.client.game.player.Player;
import lombok.extern.slf4j.Slf4j;
import org.joml.Vector3f;

@Slf4j
public class Lighting {
    private static final Vector3f VISION_COLOR = new Vector3f(1, 1, 1);

    private TileMap.LightSource[] sources;

    private float[] positions;
    private float[] colors;
    private float[] intensities;
    private int amount;

    public Lighting() {
        positions = new float[2];
        colors = new float[3];
        intensities = new float[1];
    }

    public void loadMap(TileMap map){
        log.info("Loading light map with {} sources", map.getLights().length);
        sources = map.getLights();

        extractSources();
    }

    public void extractSources(){

        // One additional for the player vision
        int length = sources.length;
        if (length > ConfigHandler.getConfig().getMaxLightSources() - 1){
            log.warn("Maximal amount ({}) of light sources exceeded! Not all light will be rendered.", ConfigHandler.getConfig().getMaxLightSources());
            length = ConfigHandler.getConfig().getMaxLightSources() - 1;
        }

        positions = new float[(length + 1) * 2];
        colors = new float[(length + 1) * 3];
        intensities = new float[(length + 1)];

        for (int i = 1; i < length + 1; i++) {
            TileMap.LightSource current = sources[i - 1];

            // Fill positions
            positions[i * 2    ] = current.getX();
            positions[i * 2 + 1] = current.getY();

            // Fill colours (and convert to float)
            colors[i * 3    ] = current.getColor().getR() / 255f;
            colors[i * 3 + 1] = current.getColor().getG() / 255f;
            colors[i * 3 + 2] = current.getColor().getB() / 255f;

            // Fill intensities
            intensities[i] = current.getIntensity();
        }

        amount = length + 1; // Also player vision
    }

    public void setPlayerVision(Player player, boolean visible){
        // Fill positions
        // TODO: Use dynamic center
        positions[0] = player.getLocation().getPosX() + 0.5f;
        positions[1] = player.getLocation().getPosY() + 0.5f;

        // Fill colours (and convert to float)
        colors[0] = VISION_COLOR.x;
        colors[1] = VISION_COLOR.y;
        colors[2] = VISION_COLOR.z;

        // Fill intensities
        intensities[0] = visible ? player.getAttributes().getVision() : 0;
    }

    public void upload(Shader shader){
        shader.uploadVec2Array("lightPositions", positions);
        shader.uploadVec3Array("lightColors", colors);
        shader.uploadFloatArray("lightIntensities", intensities);
        shader.uploadInt("lightSources", amount);
    }
}
