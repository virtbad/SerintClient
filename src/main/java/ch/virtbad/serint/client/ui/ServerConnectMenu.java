package ch.virtbad.serint.client.ui;

import ch.virtbad.serint.client.ui.components.EditText;

import static ch.virtbad.serint.client.ui.MainMenu.*;

/**
 * This class contains the menu where you can select and connect to a server
 * @author Virt
 */
public class ServerConnectMenu extends MenuScene {


    @Override
    public void build() {
        EditText editText = new EditText(-BUTTON_WIDTH / 2, - (10 - (BUTTON_SPACING)), BUTTON_WIDTH, BUTTON_HEIGHT, 0.5f, true, 29);
        addComponent(editText);
    }

}
