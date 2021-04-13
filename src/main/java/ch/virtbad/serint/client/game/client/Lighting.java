package ch.virtbad.serint.client.game.client;

import ch.virtbad.serint.client.engine.resources.shaders.Shader;
import ch.virtbad.serint.client.game.map.TileMap;
import lombok.extern.slf4j.Slf4j;
import org.joml.Vector3f;

@Slf4j
public class Lighting {
    private static final int SOURCE_MAX = 100;

    private TileMap.LightSource[] sources;

    private float[] positions;
    private float[] colors;
    private float[] intensities;
    private int amount;

    public Lighting() {
        positions = new float[0];
        colors = new float[0];
        intensities = new float[0];
    }

    public void loadMap(TileMap map){
        log.info("Loading light map with {} sources", map.getLights().length);
        sources = map.getLights();

        extractSources();
    }

    public void extractSources(){

        // One additional for the player vision
        int length = sources.length;
        if (length > SOURCE_MAX - 1){
            log.warn("Maximal amount ({}) of light sources exceeded! Not all light will be rendered.", SOURCE_MAX);
            length = SOURCE_MAX - 1;
        }

        positions = new float[(length + 1) * 2];
        colors = new float[(length + 1) * 3];
        intensities = new float[(length + 1)];


        for (int i = 0; i < length; i++) {
            TileMap.LightSource current = sources[i];

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

    public void upload(Shader shader){
        shader.uploadVec2Array("lightPositions", positions);
        shader.uploadVec3Array("lightColors", colors);
        shader.uploadFloatArray("lightIntensities", intensities);
        shader.uploadInt("lightSources", amount);
    }
}
