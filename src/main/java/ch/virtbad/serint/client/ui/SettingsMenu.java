package ch.virtbad.serint.client.ui;

import ch.virtbad.serint.client.config.ConfigHandler;
import ch.virtbad.serint.client.graphics.ResourceHandler;
import ch.virtbad.serint.client.ui.components.Button;
import ch.virtbad.serint.client.ui.components.Label;
import ch.virtbad.serint.client.ui.components.NumberSpinner;
import ch.virtbad.serint.client.ui.components.OptionsButton;

import static ch.virtbad.serint.client.ui.MainMenu.*;

/**
 * This class is the settings menu
 * @author Virt
 */
public class SettingsMenu extends MenuScene {

    NumberSpinner uiScale;
    OptionsButton languages;

    NumberSpinner fps;
    OptionsButton aspects;
    OptionsButton cosmetics;
    NumberSpinner lightSources;

    OptionsButton showFps;

    @Override
    public void build() {
        Button backButton = new Button(-BUTTON_WIDTH / 2, - (10 - (BUTTON_SPACING)), BUTTON_WIDTH, BUTTON_HEIGHT, ResourceHandler.getLanguages().getString("ui.main.button.back"));
        backButton.setEvent(this::checkout);
        addComponent(backButton);

        Label warningLabel = new Label(-camera.getXMinUnits() / 2, backButton.getY() + BUTTON_HEIGHT, camera.getXMinUnits(), 0.5f, 0.25f, ResourceHandler.getLanguages().getString("ui.settings.disclaimer"), true, false);
        addComponent(warningLabel);

        float x = -camera.getXMinUnits() / 2 + 1f;
        float subX = x + 0.5f;
        String[] onOff = new String[] {ResourceHandler.getLanguages().getString("ui.settings.option.on"), ResourceHandler.getLanguages().getString("ui.settings.option.off")};

        // GUI

        Label guiTitle = new Label(x, camera.getYMinUnits() / 2 - 1.5f, camera.getXMinUnits(), 0.5f, 0.5f, ResourceHandler.getLanguages().getString("ui.settings.title.gui"), false, false);
        addComponent(guiTitle);

        // GUI Scale
        Label guiScaleLabel = new Label(subX, guiTitle.getY() - 1 - 0.5f, camera.getXMinUnits(), 1, 0.5f, ResourceHandler.getLanguages().getString("ui.settings.setting.uiscale"), false, true);
        addComponent(guiScaleLabel);
        uiScale = new NumberSpinner(camera.getYMinUnits() / 2 - 8, guiScaleLabel.getY(), 8, 1, ConfigHandler.getConfig().getUiScale(), 0.5f, 1, 4);
        addComponent(uiScale);

        // Languages

        Label languageLabel = new Label(subX, guiScaleLabel.getY() - 1 - 0.3f, camera.getXMinUnits(), 1, 0.5f, ResourceHandler.getLanguages().getString("ui.settings.setting.language"), false, true);
        addComponent(languageLabel);

        int selected = 0;
        String[] langs = new String[ResourceHandler.getLanguages().getLanguages().length];
        for (int i = 0; i < ResourceHandler.getLanguages().getLanguages().length; i++) {
            langs[i] = ResourceHandler.getLanguages().getLanguages()[i].getName();
            if (ResourceHandler.getLanguages().getSelectedIdentifier().equals(ResourceHandler.getLanguages().getLanguages()[i].getIdentifier())) selected = i;

        }

        languages = new OptionsButton(camera.getYMinUnits() / 2 - 8, languageLabel.getY(), 8, 1, langs, selected);
        addComponent(languages);

        // GRAPHICS

        Label graphicsTitle = new Label(x, languageLabel.getY() - 2f, camera.getXMinUnits(), 0.5f, 0.5f, ResourceHandler.getLanguages().getString("ui.settings.title.graphics"), false, false);
        addComponent(graphicsTitle);

        // Fps

        Label fpsLabel = new Label(subX, graphicsTitle.getY() - 1 - 0.5f, camera.getXMinUnits(), 1, 0.5f, ResourceHandler.getLanguages().getString("ui.settings.setting.fps"), false, true);
        addComponent(fpsLabel);

        fps = new NumberSpinner(camera.getYMinUnits() / 2 - 8, fpsLabel.getY(), 8, 1, ConfigHandler.getConfig().getFps(), 10, 10, 1000);
        addComponent(fps);

        // Aspects

        Label aspectLabel = new Label(subX, fpsLabel.getY() - 1 - 0.3f, camera.getXMinUnits(), 1, 0.5f, ResourceHandler.getLanguages().getString("ui.settings.setting.aspect"), false, true);
        addComponent(aspectLabel);

        aspects = new OptionsButton(camera.getYMinUnits() / 2 - 8, aspectLabel.getY(), 8, 1, onOff, ConfigHandler.getConfig().isEnableAspects() ? 0 : 1);
        addComponent(aspects);

        // Cosmetics

        Label cosmeticLabel = new Label(subX, aspectLabel.getY() - 1 - 0.3f, camera.getXMinUnits(), 1, 0.5f, ResourceHandler.getLanguages().getString("ui.settings.setting.cosmetic"), false, true);
        addComponent(cosmeticLabel);

        cosmetics = new OptionsButton(camera.getYMinUnits() / 2 - 8, cosmeticLabel.getY(), 8, 1, onOff, ConfigHandler.getConfig().isEnableCosmetics() ? 0 : 1);
        addComponent(cosmetics);

        // Light Source

        Label lightSourceLabel = new Label(subX, cosmeticLabel.getY() - 1 - 0.3f, camera.getXMinUnits(), 1, 0.5f, ResourceHandler.getLanguages().getString("ui.settings.setting.lightsource"), false, true);
        addComponent(lightSourceLabel);

        lightSources = new NumberSpinner(camera.getYMinUnits() / 2 - 8, lightSourceLabel.getY(), 8, 1, ConfigHandler.getConfig().getMaxLightSources(), 1f, 1, 1000);
        addComponent(lightSources);

        // MISCELLANEOUS

        Label miscTitle = new Label(x, lightSourceLabel.getY() - 2f, camera.getXMinUnits(), 0.5f, 0.5f, ResourceHandler.getLanguages().getString("ui.settings.title.misc"), false, false);
        addComponent(miscTitle);

        // Show Fps

        Label showFpsLabel = new Label(subX, miscTitle.getY() - 1 - 0.5f, camera.getXMinUnits(), 1, 0.5f, ResourceHandler.getLanguages().getString("ui.settings.setting.showfps"), false, true);
        addComponent(showFpsLabel);

        showFps = new OptionsButton(camera.getYMinUnits() / 2 - 8, showFpsLabel.getY(), 8, 1, onOff, ConfigHandler.getConfig().isShowFps() ? 0 : 1);
        addComponent(showFps);

    }

    public void checkout(){

        ConfigHandler.getConfig().setUiScale(uiScale.getNumber());
        ConfigHandler.getConfig().setLanguage(ResourceHandler.getLanguages().getLanguages()[languages.getIndex()].getIdentifier());

        ConfigHandler.getConfig().setFps((int) fps.getNumber());
        ConfigHandler.getConfig().setEnableAspects(aspects.getIndex() == 0);
        ConfigHandler.getConfig().setEnableCosmetics(cosmetics.getIndex() == 0);
        ConfigHandler.getConfig().setMaxLightSources((int) lightSources.getNumber());

        ConfigHandler.getConfig().setShowFps(showFps.getIndex() == 0);

        ConfigHandler.save();

        switchScene(1);
    }


}
