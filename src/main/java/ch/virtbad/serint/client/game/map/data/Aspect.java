package ch.virtbad.serint.client.game.map.data;

import com.google.gson.annotations.JsonAdapter;
import lombok.Getter;

@Getter
public class Aspect {

    private int target;
    private int layer;
    private TextureLocation texture;

    @JsonAdapter(AspectAdapter.class)
    private AspectRequirement top;
    @JsonAdapter(AspectAdapter.class)
    private AspectRequirement bottom;
    @JsonAdapter(AspectAdapter.class)
    private AspectRequirement left;
    @JsonAdapter(AspectAdapter.class)
    private AspectRequirement right;

    @JsonAdapter(AspectAdapter.class)
    private AspectRequirement topRight;
    @JsonAdapter(AspectAdapter.class)
    private AspectRequirement topLeft;
    @JsonAdapter(AspectAdapter.class)
    private AspectRequirement bottomRight;
    @JsonAdapter(AspectAdapter.class)
    private AspectRequirement bottomLeft;
}
