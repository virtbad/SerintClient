package ch.virtbad.serint.client.game.map.data;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class AspectAdapter extends TypeAdapter<AspectRequirement> {

    @Override
    public void write(JsonWriter out, AspectRequirement value) throws IOException {
        // This is never going to be serialized so..
    }

    @Override
    public AspectRequirement read(JsonReader in) throws IOException {
        // Dirty but it should work
        AspectRequirement req = new Gson().fromJson(in, AspectRequirement.class);
        in.beginObject();

        if (req.getType() == AspectRequirement.Type.TYPE){
            req = new Gson().fromJson(in, AspectTypeRequirement.class);
        }else if (req.getType() == AspectRequirement.Type.TILE) {
            req = new Gson().fromJson(in, AspectTypeRequirement.class);
        }

        return req;
    }

}
