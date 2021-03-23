package ch.virtbad.serint.client.game.map.data.requirements;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * This class handles deserialization of a requirement for it to be passed into its subclasses
 */
public class AspectRequirementDeserializer implements JsonDeserializer<AspectRequirement> {

    @Override
    public AspectRequirement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        if (json.getAsJsonObject().get("type").getAsString().equals(AspectRequirement.Type.TYPE.toString())){
            return context.deserialize(json, AspectTypeRequirement.class);
        } else if (json.getAsJsonObject().get("type").getAsString().equals(AspectRequirement.Type.TILE.toString())){
            return context.deserialize(json, AspectTileRequirement.class);
        } else {
            return context.deserialize(json, AspectRequirement.class);
        }

    }
}
