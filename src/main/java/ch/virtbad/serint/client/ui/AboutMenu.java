package ch.virtbad.serint.client.ui;

import ch.virtbad.serint.client.Serint;
import ch.virtbad.serint.client.graphics.ResourceHandler;
import ch.virtbad.serint.client.ui.components.BackgroundImage;
import ch.virtbad.serint.client.ui.components.Button;
import ch.virtbad.serint.client.ui.components.Label;
import ch.virtbad.serint.client.ui.components.LabelLink;

import static ch.virtbad.serint.client.ui.MainMenu.*;

/**
 * This class is the about menu
 * @author Virt
 */
public class AboutMenu extends MenuScene {

    private static final String WSB_NAME = "WhySoBad";
    private static final String VIRT_NAME = "Virt";

    private static final String GH_WSB = "https://github.com/WhySoBad";
    private static final String GH_VIRT = "https://github.com/VirtCode";

    private static final String GH_CLIENT = "https://github.com/virtbad/SerintClient";
    private static final String GH_SERVER = "https://github.com/virtbad/SerintServer";

    private static final String WEBSITE_OPENGL = "https://www.opengl.org/";
    private static final String WEBSITE_LWJGL = "https://www.lwjgl.org/";
    private static final String WEBSITE_GSON = "https://github.com/google/gson";
    private static final String WEBSITE_PSEUDOPACKETS = "https://github.com/VirtCode/PseudoPackets";

    @Override
    public void build() {

        float x = -7;

        BackgroundImage background = new BackgroundImage("background");
        addComponent(background);

        Button backButton = new Button(-BUTTON_WIDTH / 2, - (camera.getYMinUnits() / 2 - (BUTTON_SPACING)), BUTTON_WIDTH, BUTTON_HEIGHT, ResourceHandler.getLanguages().getString("ui.main.button.back"));
        backButton.setEvent(() -> switchScene(1));

        Label title = new Label(-10, 10 - (BUTTON_SPACING + BUTTON_HEIGHT), 20, 2, 0.75f, ResourceHandler.getLanguages().getString("name") + ResourceHandler.getLanguages().getString("ui.about.info.version") + Serint.VERSION, true, true);

        addComponent(backButton);
        addComponent(title);

        Label libraries = new Label(x, 10 - (BUTTON_SPACING + BUTTON_HEIGHT + BUTTON_SPACING + 0.5f), 10 - BUTTON_SPACING, 0.5f, 0.5f, ResourceHandler.getLanguages().getString("ui.about.info.libraries"), false, true);
        addComponent(libraries);

        addLinkedSubEntry(0, libraries, ResourceHandler.getLanguages().getString("ui.about.info.rendering"), ResourceHandler.getLanguages().getString("ui.about.info.opengl"), WEBSITE_OPENGL);
        addLinkedSubEntry(1, libraries, ResourceHandler.getLanguages().getString("ui.about.info.bindings"), ResourceHandler.getLanguages().getString("ui.about.info.lwjgl"), WEBSITE_LWJGL);
        addLinkedSubEntry(2, libraries, ResourceHandler.getLanguages().getString("ui.about.info.serialization"), ResourceHandler.getLanguages().getString("ui.about.info.gson"), WEBSITE_GSON);
        addLinkedSubEntry(3, libraries, ResourceHandler.getLanguages().getString("ui.about.info.networking"), ResourceHandler.getLanguages().getString("ui.about.info.pseudopackets"), WEBSITE_PSEUDOPACKETS);

        Label creators = new Label(x, 10 - (BUTTON_SPACING + BUTTON_HEIGHT + BUTTON_SPACING + 0.5f + 4), 10 - BUTTON_SPACING, 0.5f, 0.5f, ResourceHandler.getLanguages().getString("ui.about.info.creators"), false, true);
        addComponent(creators);

        addSubLink(0, creators, WSB_NAME, GH_WSB);
        addSubLink(1, creators, VIRT_NAME, GH_VIRT);

        Label projectView = new Label(x, 10 - (BUTTON_SPACING + BUTTON_HEIGHT + BUTTON_SPACING + 0.5f + 6.7f), 10 - BUTTON_SPACING, 0.5f, 0.5f, ResourceHandler.getLanguages().getString("ui.about.info.view.client"), false, true);
        addComponent(projectView);
        LabelLink projectViewGithub = new LabelLink(projectView.getX() + projectView.getTextWidth(), projectView.getY(), 10, 0.5f, 0.5f, ResourceHandler.getLanguages().getString("ui.about.info.github"), false, true);
        projectViewGithub.setEvent(() -> UiHelper.openLink(GH_CLIENT));
        addComponent(projectViewGithub);

        Label serverView = new Label(x, projectView.getY() - 0.75f, 10 - BUTTON_SPACING, 0.5f, 0.5f, ResourceHandler.getLanguages().getString("ui.about.info.view.server"), false, true);
        addComponent(serverView);
        LabelLink serverViewGithub = new LabelLink(serverView.getX() + serverView.getTextWidth(), serverView.getY(), 10, 0.5f, 0.5f, ResourceHandler.getLanguages().getString("ui.about.info.github"), false, true);
        serverViewGithub.setEvent(() -> UiHelper.openLink(GH_SERVER));
        addComponent(serverViewGithub);
    }

    /**
     * Adds a sub entry to the menu based of another label
     * The sub entry is a caption and a link
     */
    private void addLinkedSubEntry(int index, Label reference, String caption, String linkname, String link){
        Label c = new Label(reference.getX() + BUTTON_SPACING, reference.getY() - (0.6f * (index + 1)) - 0.2f, 10, 0.5f, 0.5f, caption, false, true);
        addComponent(c);
        LabelLink l = new LabelLink(c.getX() + c.getTextWidth(), c.getY(), 10, 0.5f, 0.5f, linkname, false, true);
        l.setEvent(() -> UiHelper.openLink(link));
        addComponent(l);
    }

    /**
     * Adds a sub link
     */
    private void addSubLink(int index, Label reference, String name, String link){
        LabelLink l = new LabelLink(reference.getX() + BUTTON_SPACING, reference.getY() - (0.6f * (index + 1)) - 0.2f, 10, 0.5f, 0.5f, name, false, true);
        l.setEvent(() -> UiHelper.openLink(link));
        addComponent(l);
    }

}
