package ch.virtbad.serint.client.game.map;

import ch.virtbad.serint.client.game.map.data.TextureLocation;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Virt
 */
@Getter @AllArgsConstructor
public class RenderedTile {
    private TextureLocation[] location;
}
