package ch.virtbad.serint.client.ui;

import ch.virtbad.serint.client.graphics.ResourceHandler;
import ch.virtbad.serint.client.networking.Communications;
import ch.virtbad.serint.client.ui.components.BackgroundImage;
import ch.virtbad.serint.client.ui.components.Button;
import ch.virtbad.serint.client.ui.components.Label;

import static ch.virtbad.serint.client.ui.MainMenu.*;

public class KickScene extends MenuScene {

    boolean dirty = false;
    private Label error;

    private final Communications communications;

    public KickScene(Communications communications) {
        this.communications = communications;
    }

    @Override
    public void build() {

        BackgroundImage background = new BackgroundImage("background");
        addComponent(background);

        Label title = new Label(-BUTTON_WIDTH / 2, 0 + BUTTON_SPACING / 2 + BUTTON_HEIGHT + BUTTON_SPACING, BUTTON_WIDTH, BUTTON_HEIGHT, 0.5f, ResourceHandler.getLanguages().getString("ui.kick.title"), true, false);
        addComponent(title);
        error = new Label(-BUTTON_WIDTH / 2, 0 + BUTTON_SPACING / 2 + BUTTON_SPACING, BUTTON_WIDTH, BUTTON_HEIGHT, 0.5f, "No further Information", true, false);
        addComponent(error);
        Button back = new Button(-BUTTON_WIDTH / 2, 0 - BUTTON_SPACING / 2 - BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT, ResourceHandler.getLanguages().getString("ui.kick.button.back"));
        back.setEvent(() -> {
            switchScene(4);
        });
        addComponent(back);

    }

    @Override
    public void update() {
        super.update();

        if (dirty) {
            error.setText(communications.getStatusInformation());
            dirty = false;
        }
    }

    @Override
    public void shown() {
        dirty = true;
    }
}
