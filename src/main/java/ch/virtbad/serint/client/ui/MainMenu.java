package ch.virtbad.serint.client.ui;

import ch.virtbad.serint.client.engine.events.BasicEvent;
import ch.virtbad.serint.client.graphics.ResourceHandler;
import ch.virtbad.serint.client.ui.components.BackgroundImage;
import ch.virtbad.serint.client.ui.components.FancyButton;
import ch.virtbad.serint.client.ui.components.Image;

/**
 * This scene is the main menu that is shown when the game was started
 * @author Virt
 */
public class MainMenu extends MenuScene {

    public static final float BUTTON_WIDTH = 16;
    public static final float BUTTON_HEIGHT = 2;
    public static final float BUTTON_SPACING = 0.5f;
    public static final float LOGO_HEIGHT = 6;

    private final BasicEvent close;

    public MainMenu(BasicEvent close) {
        this.close = close;
    }

    @Override
    public void build() {

        BackgroundImage background = new BackgroundImage("background");
        Image logo = new Image(-BUTTON_WIDTH / 2, BUTTON_HEIGHT + BUTTON_SPACING * 2, BUTTON_WIDTH, LOGO_HEIGHT,"logo");

        FancyButton playButton = new FancyButton(-BUTTON_WIDTH / 2, 0, BUTTON_WIDTH, BUTTON_HEIGHT, ResourceHandler.getLanguages().getString("ui.main.button.play"));
        FancyButton optionsButton = new FancyButton(playButton.getX(), playButton.getY() - (BUTTON_HEIGHT + BUTTON_SPACING), BUTTON_WIDTH, BUTTON_HEIGHT, ResourceHandler.getLanguages().getString("ui.main.button.options"));
        FancyButton aboutButton = new FancyButton(optionsButton.getX(), optionsButton.getY() - (BUTTON_HEIGHT + BUTTON_SPACING), BUTTON_WIDTH, BUTTON_HEIGHT, ResourceHandler.getLanguages().getString("ui.main.button.about"));
        FancyButton quitButton = new FancyButton(aboutButton.getX(), aboutButton.getY() - (BUTTON_HEIGHT + BUTTON_SPACING), BUTTON_WIDTH, BUTTON_HEIGHT, ResourceHandler.getLanguages().getString("ui.main.button.quit"));

        playButton.setEvent(() -> switchScene(4));
        optionsButton.setEvent(() -> switchScene(2));
        aboutButton.setEvent(() -> switchScene(3));
        quitButton.setEvent(close);

        addComponent(background);

        addComponent(logo);
        addComponent(playButton);
        addComponent(optionsButton);
        addComponent(aboutButton);
        addComponent(quitButton);
    }

}
