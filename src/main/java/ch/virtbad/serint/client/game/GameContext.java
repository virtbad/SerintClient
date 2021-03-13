package ch.virtbad.serint.client.game;

import ch.virtbad.serint.client.engine.content.Camera;
import ch.virtbad.serint.client.engine.input.Keyboard;
import ch.virtbad.serint.client.engine.input.Mouse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * This class keeps track of variables that many classes need to access
 * @author Virt
 */
@Getter @Setter @AllArgsConstructor
public class GameContext {

    private Camera camera;

    private Keyboard keyboard;
    private Mouse mouse;

}
