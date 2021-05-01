package ch.virtbad.serint.client.ui;

import ch.virtbad.serint.client.graphics.ResourceHandler;
import ch.virtbad.serint.client.ui.components.BackgroundImage;
import ch.virtbad.serint.client.ui.components.Button;
import ch.virtbad.serint.client.ui.components.Label;
import ch.virtbad.serint.client.ui.components.NumberSpinner;

import static ch.virtbad.serint.client.ui.MainMenu.*;

/**
 * This class is the settings menu
 * @author Virt
 */
public class SettingsMenu extends MenuScene {

    @Override
    public void build() {

        BackgroundImage background = new BackgroundImage("background");
        addComponent(background);

        Button backButton = new Button(-BUTTON_WIDTH / 2, - (10 - (BUTTON_SPACING)), BUTTON_WIDTH, BUTTON_HEIGHT, ResourceHandler.getLanguages().getString("ui.main.button.back"));
        backButton.setEvent(() -> switchScene(-1));
        addComponent(backButton);

        float x = -camera.getXMinUnits() / 2 + 1f;
        float subX = x + 0.5f;

        Label guiTitle = new Label(x, camera.getYMinUnits() / 2 - 1.5f, camera.getXMinUnits(), 0.5f, 0.5f, ResourceHandler.getLanguages().getString("ui.settings.title.gui"), false, false);
        addComponent(guiTitle);

        Label guiScaleLabel = new Label(subX, guiTitle.getY() - 0.5f - 0.5f, camera.getXMinUnits(), 1, 0.5f, ResourceHandler.getLanguages().getString("ui.settings.setting.uiscale"), false, true);
        addComponent(guiScaleLabel);
        Label languageLabel = new Label(subX, guiScaleLabel.getY() - 1 - 0.3f, camera.getXMinUnits(), 1, 0.5f, ResourceHandler.getLanguages().getString("ui.settings.setting.language"), false, true);
        addComponent(languageLabel);

        NumberSpinner numberSpinner = new NumberSpinner(0, 0, 5, 1, 12, 0.5f);
        addComponent(numberSpinner);
    }


}
