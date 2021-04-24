package ch.virtbad.serint.client.ui;

import ch.virtbad.serint.client.config.Config;
import ch.virtbad.serint.client.config.ConfigHandler;
import ch.virtbad.serint.client.graphics.ResourceHandler;
import ch.virtbad.serint.client.networking.Communications;
import ch.virtbad.serint.client.networking.NetworkHandler;
import ch.virtbad.serint.client.ui.components.Button;
import ch.virtbad.serint.client.ui.components.Container;
import ch.virtbad.serint.client.ui.components.EditText;
import ch.virtbad.serint.client.ui.components.Label;

import javax.imageio.event.IIOReadProgressListener;
import java.awt.event.TextEvent;

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

    private final NetworkHandler networkHandler;
    private final Communications communications;

    private boolean connecting;
    private int lastCode;

    public ServerConnectMenu(NetworkHandler networkHandler, Communications communications) {
        this.networkHandler = networkHandler;
        this.communications = communications;
    }

    @Override
    public void build() {

        prompt = new Container();

        Button backButton = new Button(-BUTTON_WIDTH / 2, -(10 - (BUTTON_SPACING)), BUTTON_WIDTH, BUTTON_HEIGHT, ResourceHandler.getLanguages().getString("ui.main.button.back"));
        backButton.setEvent(() -> switchScene(1));
        prompt.addComponent(backButton);

        Label hostLabel = new Label(-BUTTON_WIDTH / 2, 0 + BUTTON_SPACING / 2 + BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT, 0.5f, ResourceHandler.getLanguages().getString("ui.connect.title.ip"), true, false);
        prompt.addComponent(hostLabel);

        hostEdit = new EditText(-BUTTON_WIDTH / 2, 0 + BUTTON_SPACING / 2, BUTTON_WIDTH, BUTTON_HEIGHT, 0.5f, true, 29); // Yes, this is the most letters that fit into the input and not an arbitrary number
        hostEdit.setSubmitListener(this::connect);
        prompt.addComponent(hostEdit);

        Button connectButton = new Button(-BUTTON_WIDTH / 2, 0 - BUTTON_SPACING / 2 - BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT, ResourceHandler.getLanguages().getString("ui.connect.button.connect"));
        connectButton.setEvent(this::connect);
        prompt.addComponent(connectButton);

        loading = new Container();

        progressLabel = new Label(-BUTTON_WIDTH / 2, 0 + BUTTON_SPACING / 2, BUTTON_WIDTH, BUTTON_HEIGHT, 0.5f, ResourceHandler.getLanguages().getString("ui.connect.status.connect"), true, false);
        loading.addComponent(progressLabel);
        Button cancelButton = new Button(-BUTTON_WIDTH / 2, 0 - BUTTON_SPACING / 2 - BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT, ResourceHandler.getLanguages().getString("ui.connect.button.cancel"));
        cancelButton.setEvent(this::cancel);
        loading.addComponent(cancelButton);

        confirm = new Container();

        Label serverTitle = new Label(-BUTTON_WIDTH / 2, 0 + BUTTON_SPACING / 2 + BUTTON_HEIGHT + BUTTON_SPACING + BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT, 0.75f, ResourceHandler.getLanguages().getString("Lorem Ipsum"), true, false);
        confirm.addComponent(serverTitle);

        Label serverDescription = new Label(-BUTTON_WIDTH / 2, 0 + BUTTON_SPACING / 2 + BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT, 0.5f, ResourceHandler.getLanguages().getString("Lorem Ipsum Server in Serint!"), true, true);
        confirm.addComponent(serverDescription);

        Button disconnectButton = new Button(-BUTTON_WIDTH / 2, 0 - BUTTON_SPACING / 2 - BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT, ResourceHandler.getLanguages().getString("ui.connect.button.disconnect"));
        confirm.addComponent(disconnectButton);
        Button proceedButton = new Button(-BUTTON_WIDTH / 2, 0 - BUTTON_SPACING / 2 - BUTTON_HEIGHT - BUTTON_SPACING - BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT, ResourceHandler.getLanguages().getString("ui.connect.button.proceed"));
        confirm.addComponent(proceedButton);

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

        addComponent(prompt);
        addComponent(loading);
        addComponent(confirm);
        addComponent(failed);
        prompt.setVisible(true);
        loading.setVisible(false);
        confirm.setVisible(false);
        failed.setVisible(false);
    }

    public void connect() {
        prompt.setVisible(false);
        loading.setVisible(true);

        progressLabel.setText(ResourceHandler.getLanguages().getString("ui.connect.status.initializing"));

        // Default Options
        String host = ConfigHandler.getConfig().getServerHost();
        int port = ConfigHandler.getConfig().getServerPort();

        String input = hostEdit.getContent();

        // Only Hostname
        if (host.length() > 0) host = input;

        // With Port
        if (input.contains(":")) {
            String[] args = input.split(":");
            if (args.length == 2) {
                host = args[0];

                if (args[1].matches("[0-9]+")) port = Integer.parseInt(args[1]);
                if (port > 65535) port = ConfigHandler.getConfig().getServerPort(); // Prevent out of range Ports
            }
        }

        connecting = true;
        lastCode = 0;

        networkHandler.connect(communications, host, port);

    }

    public void cancel(){
        loading.setVisible(false);
        prompt.setVisible(true);

        // Cancels in the correct way (client may not even be connected)
        if (communications.isConnected()) communications.disconnect();
        else networkHandler.cancelConnection();
    }

    @Override
    public void update() {
        super.update();

        if (connecting && communications.getStatus() != lastCode) {
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

                    switchScene(5);
                    break;
                case Communications.FAILED_ESTABLISH:
                    loading.setVisible(false);
                    failed.setVisible(true);

                    errorLabel.setText(communications.getStatusInformation());
                    failedLabel.setText(ResourceHandler.getLanguages().getString("ui.connect.status.failed.connect"));

                    connecting = false;
                    break;
            }
        }
    }
}
