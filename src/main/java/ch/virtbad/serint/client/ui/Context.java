package ch.virtbad.serint.client.ui;

import ch.virtbad.serint.client.engine.content.Camera;
import ch.virtbad.serint.client.engine.input.Keyboard;
import ch.virtbad.serint.client.engine.input.Mouse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * This class handles values or classes, every gui Component needs to be able to draw itself
 * @author Virt
 */
@Getter @Setter @AllArgsConstructor
public class Context {

    private Camera camera;

    private Keyboard keyboard;
    private Mouse mouse;

}
