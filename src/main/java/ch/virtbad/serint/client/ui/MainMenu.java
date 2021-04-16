package ch.virtbad.serint.client.ui;

import ch.virtbad.serint.client.ui.components.Button;

/**
 * This scene is the main menu that is shown when the game was started
 * @author Virt
 */
public class MainMenu extends UiScene {

    /**
     * Creates the main menu
     */
    public MainMenu() {
        super(20);
    }

    @Override
    public void build() {
        Button button = new Button(10, 10, 4, 2);
        addComponent(button);
    }
}
