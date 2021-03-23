package ch.virtbad.serint.client.game.map.data;

import ch.virtbad.serint.client.game.map.data.requirements.AspectRequirement;
import ch.virtbad.serint.client.game.map.data.requirements.AspectRequirementDeserializer;
import com.google.gson.annotations.JsonAdapter;
import lombok.Getter;

/**
 * This class represents an aspect
 * An aspect is an overlay over a specific tile
 */
@Getter
public class Aspect {

    private int target;
    private int layer;
    private TextureLocation texture;

    @JsonAdapter(AspectRequirementDeserializer.class)
    private AspectRequirement top;
    @JsonAdapter(AspectRequirementDeserializer.class)
    private AspectRequirement bottom;
    @JsonAdapter(AspectRequirementDeserializer.class)
    private AspectRequirement left;
    @JsonAdapter(AspectRequirementDeserializer.class)
    private AspectRequirement right;

    @JsonAdapter(AspectRequirementDeserializer.class)
    private AspectRequirement topRight;
    @JsonAdapter(AspectRequirementDeserializer.class)
    private AspectRequirement topLeft;
    @JsonAdapter(AspectRequirementDeserializer.class)
    private AspectRequirement bottomRight;
    @JsonAdapter(AspectRequirementDeserializer.class)
    private AspectRequirement bottomLeft;
}
