package ch.virtbad.serint.client.ui;

import ch.virtbad.serint.client.graphics.ResourceHandler;
import ch.virtbad.serint.client.ui.components.BackgroundImage;
import ch.virtbad.serint.client.ui.components.FancyButton;

/**
 * This scene is the main menu that is shown when the game was started
 * @author Virt
 */
public class MainMenu extends UiScene {

    public static final float BUTTON_WIDTH = 16;
    public static final float BUTTON_HEIGHT = 2;
    public static final float BUTTON_SPACING = 0.5f;

    /**
     * Creates the main menu
     */
    public MainMenu() {
        super(20);
    }

    @Override
    public void build() {

        BackgroundImage background = new BackgroundImage("background");

        FancyButton playButton = new FancyButton(-BUTTON_WIDTH / 2, 0, BUTTON_WIDTH, BUTTON_HEIGHT, ResourceHandler.getLanguages().getString("ui.main.button.play"));
        FancyButton optionsButton = new FancyButton(playButton.getX(), playButton.getY() - (BUTTON_HEIGHT + BUTTON_SPACING), BUTTON_WIDTH, BUTTON_HEIGHT, ResourceHandler.getLanguages().getString("ui.main.button.options"));
        FancyButton aboutButton = new FancyButton(optionsButton.getX(), optionsButton.getY() - (BUTTON_HEIGHT + BUTTON_SPACING), BUTTON_WIDTH, BUTTON_HEIGHT, ResourceHandler.getLanguages().getString("ui.main.button.about"));
        FancyButton quitButton = new FancyButton(aboutButton.getX(), aboutButton.getY() - (BUTTON_HEIGHT + BUTTON_SPACING), BUTTON_WIDTH, BUTTON_HEIGHT, ResourceHandler.getLanguages().getString("ui.main.button.quit"));

        optionsButton.setEvent(() -> switchScene(2));
        aboutButton.setEvent(() -> switchScene(3));

        addComponent(background);

        addComponent(playButton);
        addComponent(optionsButton);
        addComponent(aboutButton);
        addComponent(quitButton);
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
