package ch.virtbad.serint.client.ui;

import ch.virtbad.serint.client.engine.events.BasicEvent;
import ch.virtbad.serint.client.graphics.ResourceHandler;
import ch.virtbad.serint.client.networking.Communications;
import ch.virtbad.serint.client.ui.components.Button;
import ch.virtbad.serint.client.ui.components.EditText;
import ch.virtbad.serint.client.ui.components.Label;

import java.awt.*;

import static ch.virtbad.serint.client.ui.MainMenu.*;

public class ServerJoinMenu extends MenuScene{
    private final Communications communications;
    private final BasicEvent gameStart;

    private EditText colorEdit;
    private EditText nameEdit;

    public ServerJoinMenu(Communications communications, BasicEvent gameStart) {
        this.communications = communications;
        this.gameStart = gameStart;
    }

    @Override
    public void build() {

        Button backButton = new Button(-BUTTON_WIDTH / 2, 0 - BUTTON_SPACING / 2 - BUTTON_HEIGHT - BUTTON_SPACING - BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT, ResourceHandler.getLanguages().getString("ui.main.button.back"));
        backButton.setEvent(() -> switchScene(4));
        addComponent(backButton);

        Label nameLabel = new Label(-BUTTON_WIDTH / 2, 0 + BUTTON_HEIGHT * 2 + BUTTON_SPACING * 3, BUTTON_WIDTH, BUTTON_HEIGHT, 0.5f, ResourceHandler.getLanguages().getString("ui.join.title.name"), true, false);
        addComponent(nameLabel);
        nameEdit = new EditText(-BUTTON_WIDTH / 2, BUTTON_HEIGHT + BUTTON_SPACING * 3, BUTTON_WIDTH, BUTTON_HEIGHT, 0.5f, true, 29); // Yes, this is the most letters that fit into the input and not an arbitrary number
        addComponent(nameEdit);

        Label colourLabel = new Label(-BUTTON_WIDTH / 2, BUTTON_HEIGHT + BUTTON_SPACING, BUTTON_WIDTH, BUTTON_HEIGHT, 0.5f, ResourceHandler.getLanguages().getString("ui.join.title.color"), true, false);
        addComponent(colourLabel);
        colorEdit = new EditText(-BUTTON_WIDTH / 2, BUTTON_SPACING, BUTTON_WIDTH, BUTTON_HEIGHT, 0.5f, true, 29); // Yes, this is the most letters that fit into the input and not an arbitrary number
        addComponent(colorEdit);

        Button joinButton = new Button(-BUTTON_WIDTH / 2, 0 - BUTTON_SPACING / 2 - BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT, ResourceHandler.getLanguages().getString("ui.join.button.join"));
        addComponent(joinButton);
        joinButton.setEvent(this::join);

    }

    public void join(){
        String color = colorEdit.getContent();

        Color c;
        if (color.matches("([0123456789AaBbCcDdEeFf]){6}")) c = Color.decode("#" + color); // Assert that color has right format
        else c = Color.getHSBColor((float) (Math.random() * 360), 1, 1);

        gameStart.emit();

        communications.join(c, nameEdit.getContent());
        switchScene(10);
    }
}
