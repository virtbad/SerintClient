package ch.virtbad.serint.client.ui;

import ch.virtbad.serint.client.ui.components.EditText;

import static ch.virtbad.serint.client.ui.MainMenu.*;

public class ServerConnectMenu extends UiScene{

    public ServerConnectMenu() {
        super(20);
    }

    @Override
    public void build() {
        EditText editText = new EditText(-BUTTON_WIDTH / 2, - (10 - (BUTTON_SPACING)), BUTTON_WIDTH, BUTTON_HEIGHT, 0.5f, true, 29);
        addComponent(editText);
    }

    @Override
    public void resized(int width, int height) {
        super.resized(width, height);

        camera.setWorldLocation(camera.getXUnits() / -2, camera.getYUnits() / -2);
    }

    @Override
    public void init(int width, int height) {
        super.init(width, height);

        camera.setWorldLocation(camera.getXUnits() / -2, camera.getYUnits() / -2);
    }
}
