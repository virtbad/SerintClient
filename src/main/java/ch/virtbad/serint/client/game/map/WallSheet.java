package ch.virtbad.serint.client.game.map;

import lombok.Getter;

/**
 * This class represents one single wall sheet
 */
@Getter
public class WallSheet {
    private int x,y;
    private boolean top,left,right,bottom;
}
