package ch.virtbad.serint.client.ui;

import ch.virtbad.serint.client.config.ConfigHandler;
import ch.virtbad.serint.client.engine.events.BasicEvent;
import ch.virtbad.serint.client.graphics.ResourceHandler;
import ch.virtbad.serint.client.networking.Communications;
import ch.virtbad.serint.client.networking.NetworkHandler;
import ch.virtbad.serint.client.ui.components.*;
import ch.virtbad.serint.client.ui.components.Button;
import ch.virtbad.serint.client.ui.components.Container;
import ch.virtbad.serint.client.ui.components.Label;
import ch.virtbad.serint.client.util.Globals;

import java.awt.*;

import static ch.virtbad.serint.client.ui.MainMenu.*;

/**
 * This class contains the menu where you can select and connect to a server
 *
 * @author Virt
 */
public class ServerConnectMenu extends MenuScene {

    private Container prompt;
    private Container loading;
    private Container confirm;
    private Container failed;

    private EditText hostEdit;
    private Label progressLabel;
    private Label failedLabel;
    private Label errorLabel;
    private EditText colorEdit;
    private EditText nameEdit;
    private Label serverTitle;
    private Label serverDescription;

    private final NetworkHandler networkHandler;
    private final Communications communications;
    private final BasicEvent gameStart;

    private int lastCode;

    /**
     * Creates a server connect menu
     *
     * @param networkHandler network handler
     * @param communications communications
     * @param gameStart
     */
    public ServerConnectMenu(NetworkHandler networkHandler, Communications communications, BasicEvent gameStart) {
        this.networkHandler = networkHandler;
        this.communications = communications;
        this.gameStart = gameStart;
    }

    @Override
    public void build() {

        BackgroundImage background = new BackgroundImage("background");
        addComponent(background);

        // Prompt

        prompt = new Container();

        Button backButton = new Button(-BUTTON_WIDTH / 2, - (camera.getYMinUnits() / 2 - (BUTTON_SPACING)), BUTTON_WIDTH, BUTTON_HEIGHT, ResourceHandler.getLanguages().getString("ui.main.button.back"));
        backButton.setEvent(() -> switchScene(1));
        prompt.addComponent(backButton);

        Label hostLabel = new Label(-BUTTON_WIDTH / 2, 0 + BUTTON_SPACING / 2 + BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT, 0.5f, ResourceHandler.getLanguages().getString("ui.connect.title.ip"), true, false);
        prompt.addComponent(hostLabel);

        hostEdit = new EditText(-BUTTON_WIDTH / 2, 0 + BUTTON_SPACING / 2, BUTTON_WIDTH, BUTTON_HEIGHT, 0.5f, true, 29, (ConfigHandler.getConfig().getLastServerHost() == null ? "" : ConfigHandler.getConfig().getLastServerHost())); // Yes, this is the most letters that fit into the input and not an arbitrary number
        hostEdit.setSubmitListener(this::connect);
        prompt.addComponent(hostEdit);

        Button connectButton = new Button(-BUTTON_WIDTH / 2, 0 - BUTTON_SPACING / 2 - BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT, ResourceHandler.getLanguages().getString("ui.connect.button.connect"));
        connectButton.setEvent(this::connect);
        prompt.addComponent(connectButton);

        // Loading

        loading = new Container();

        progressLabel = new Label(-BUTTON_WIDTH / 2, 0 + BUTTON_SPACING / 2, BUTTON_WIDTH, BUTTON_HEIGHT, 0.5f, ResourceHandler.getLanguages().getString("ui.connect.status.connect"), true, false);
        loading.addComponent(progressLabel);
        Button cancelButton = new Button(-BUTTON_WIDTH / 2, 0 - BUTTON_SPACING / 2 - BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT, ResourceHandler.getLanguages().getString("ui.connect.button.cancel"));
        cancelButton.setEvent(this::cancel);
        loading.addComponent(cancelButton);

        // Confirm

        confirm = new Container();

        serverTitle = new Label(-BUTTON_WIDTH / 2, BUTTON_HEIGHT * 4, BUTTON_WIDTH, BUTTON_HEIGHT, 0.75f, "Server Name", true, false);
        confirm.addComponent(serverTitle);
        serverDescription = new Label(-BUTTON_WIDTH / 2, serverTitle.getY() - 1f, BUTTON_WIDTH, 1f, 0.5f, "Server Description", true, false);
        confirm.addComponent(serverDescription);

        nameEdit = new EditText(-BUTTON_WIDTH / 2, BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT, 0.5f, true, 11, ConfigHandler.getConfig().getLastName() == null ? "" : ConfigHandler.getConfig().getLastName()); // Maximal amount of letters fitting on to the hud
        confirm.addComponent(nameEdit);
        Label nameLabel = new Label(-BUTTON_WIDTH / 2, nameEdit.getY() + BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT, 0.5f, ResourceHandler.getLanguages().getString("ui.connect.title.name"), true, false);
        confirm.addComponent(nameLabel);

        colorEdit = new EditText(-BUTTON_WIDTH / 2, -BUTTON_SPACING, BUTTON_WIDTH, BUTTON_HEIGHT, 0.5f, true, 29, ConfigHandler.getConfig().getLastColor() == null ? "" : ConfigHandler.getConfig().getLastColor());
        confirm.addComponent(colorEdit);
        Label colorLabel = new Label(-BUTTON_WIDTH / 2, colorEdit.getY() + BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT, 0.5f, ResourceHandler.getLanguages().getString("ui.connect.title.color"), true, false);
        confirm.addComponent(colorLabel);

        Button disconnectButton = new Button(-BUTTON_WIDTH / 2, 0 - BUTTON_SPACING - BUTTON_HEIGHT * 2, BUTTON_WIDTH, BUTTON_HEIGHT, ResourceHandler.getLanguages().getString("ui.connect.button.disconnect"));
        confirm.addComponent(disconnectButton);
        disconnectButton.setEvent(this::disconnect);
        Button proceedButton = new Button(-BUTTON_WIDTH / 2, disconnectButton.getY() - BUTTON_SPACING - BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT, ResourceHandler.getLanguages().getString("ui.connect.button.join"));
        confirm.addComponent(proceedButton);
        proceedButton.setEvent(this::join);

        // Failed

        failed = new Container();
        failedLabel = new Label(-BUTTON_WIDTH / 2, 0 + BUTTON_SPACING / 2 + BUTTON_HEIGHT + BUTTON_SPACING, BUTTON_WIDTH, BUTTON_HEIGHT, 0.5f, ResourceHandler.getLanguages().getString("ui.connect.status.failed.connect"), true, false);
        failed.addComponent(failedLabel);
        errorLabel = new Label(-BUTTON_WIDTH / 2, 0 + BUTTON_SPACING / 2 + BUTTON_SPACING, BUTTON_WIDTH, BUTTON_HEIGHT, 0.5f, "No further Information", true, false);
        failed.addComponent(errorLabel);
        Button failedBackButton = new Button(-BUTTON_WIDTH / 2, 0 - BUTTON_SPACING / 2 - BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT, ResourceHandler.getLanguages().getString("ui.connect.button.back"));
        failedBackButton.setEvent(() -> {
            failed.setVisible(false);
            prompt.setVisible(true);
        });
        failed.addComponent(failedBackButton);

        // Finishing up

        addComponent(prompt);
        addComponent(loading);
        addComponent(confirm);
        addComponent(failed);
        prompt.setVisible(true);
        loading.setVisible(false);
        confirm.setVisible(false);
        failed.setVisible(false);
    }

    /**
     * Connects with the current values
     */
    public void connect() {
        prompt.setVisible(false);
        loading.setVisible(true);

        progressLabel.setText(ResourceHandler.getLanguages().getString("ui.connect.status.initializing"));

        // Default Options
        String host = ConfigHandler.getConfig().getServerHost();
        int port = ConfigHandler.getConfig().getServerPort();

        String input = hostEdit.getContent();

        // Only Hostname
        if (input.length() > 0){
            host = input;
            ConfigHandler.getConfig().setLastServerHost(input);
        }

        // With Port
        if (input.contains(":")) {
            String[] args = input.split(":");
            if (args.length == 2) {
                host = args[0];

                if (args[1].matches("[0-9]+")){
                    port = Integer.parseInt(args[1]);
                    if (port > 65535) port = ConfigHandler.getConfig().getServerPort(); // Prevent out of range Ports
                    else {
                        ConfigHandler.getConfig().setLastServerHost(input);
                    }
                }
            }
        }

        ConfigHandler.save();

        lastCode = 0;

        networkHandler.connect(communications, host, port);

    }

    /**
     * Cancels the connection
     */
    public void cancel() {
        loading.setVisible(false);
        prompt.setVisible(true);

        // Cancels in the correct way (client may not even be connected)
        if (communications.isConnected()) communications.disconnect();
        else networkHandler.cancelConnection();
    }

    /**
     * Disconnects again from the server
     */
    public void disconnect() {
        communications.disconnect();

        confirm.setVisible(false);
        prompt.setVisible(true);
    }


    /**
     * Joins the game with current values
     */
    public void join() {
        String color = colorEdit.getContent();

        Color c;
        if (color.matches("([0123456789AaBbCcDdEeFf]){6}")){
            c = Color.decode("#" + color); // Assert that color has right format
            ConfigHandler.getConfig().setLastColor(color);
        }
        else if (color.matches("#([0123456789AaBbCcDdEeFf]){6}")){
            c = Color.decode(color);
            ConfigHandler.getConfig().setLastColor(color);
        }
        else c = Color.getHSBColor((float) (Math.random() * 360), 1, 1);

        gameStart.emit();

        ConfigHandler.getConfig().setLastName(nameEdit.getContent());
        ConfigHandler.save();

        communications.join(c, nameEdit.getContent());

        confirm.setVisible(false);
        loading.setVisible(true);

        progressLabel.setText(ResourceHandler.getLanguages().getString("ui.connect.status.joining"));
    }

    @Override
    public void update() {
        super.update();

        if (communications.getStatus() != lastCode) {
            lastCode = communications.getStatus();

            switch (lastCode) {
                case Communications.ESTABLISHING:
                    progressLabel.setText(ResourceHandler.getLanguages().getString("ui.connect.status.connect"));
                    break;
                case Communications.HANDSHAKE:
                    progressLabel.setText(ResourceHandler.getLanguages().getString("ui.connect.status.handshake"));
                    break;
                case Communications.FAILED_HANDSHAKE:
                    loading.setVisible(false);
                    failed.setVisible(true);

                    errorLabel.setText(communications.getStatusInformation());
                    failedLabel.setText(ResourceHandler.getLanguages().getString("ui.connect.status.failed.handshake"));
                    break;
                case Communications.IN:
                    progressLabel.setText(ResourceHandler.getLanguages().getString("ui.connect.status.success"));

                    serverTitle.setText(Globals.getNetwork().getServerName());
                    serverDescription.setText(Globals.getNetwork().getServerDescription());

                    loading.setVisible(false);
                    confirm.setVisible(true);

                    break;
                case Communications.FAILED_ESTABLISH:
                    loading.setVisible(false);
                    failed.setVisible(true);

                    errorLabel.setText(communications.getStatusInformation());
                    failedLabel.setText(ResourceHandler.getLanguages().getString("ui.connect.status.failed.connect"));

                    break;
                case Communications.FAILED_JOIN:
                    loading.setVisible(false);
                    failed.setVisible(true);

                    errorLabel.setText(communications.getStatusInformation());
                    failedLabel.setText(ResourceHandler.getLanguages().getString("ui.connect.status.failed.join"));

                    break;
                case Communications.DONE:
                    switchScene(10);
                    break;
            }
        }
    }

    @Override
    public void shown() {
        super.shown();
        prompt.setVisible(true);
        loading.setVisible(false);
        confirm.setVisible(false);
        failed.setVisible(false);
    }
}
