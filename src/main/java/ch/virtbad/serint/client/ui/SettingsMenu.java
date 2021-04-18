package ch.virtbad.serint.client.ui;

import ch.virtbad.serint.client.graphics.ResourceHandler;
import ch.virtbad.serint.client.ui.components.Button;

import static ch.virtbad.serint.client.ui.MainMenu.*;

/**
 * This class is the settings menu
 * @author Virt
 */
public class SettingsMenu extends UiScene{

    /**
     * Creates a settings menu
     */
    public SettingsMenu() {
        super(20);
    }

    @Override
    public void build() {
        Button backButton = new Button(-BUTTON_WIDTH / 2, - (10 - (BUTTON_SPACING)), BUTTON_WIDTH, BUTTON_HEIGHT, ResourceHandler.getLanguages().getString("ui.main.button.back"));

        backButton.setEvent(() -> switchScene(1));

        addComponent(backButton);
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
