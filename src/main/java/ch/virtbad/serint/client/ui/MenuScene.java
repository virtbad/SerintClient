package ch.virtbad.serint.client.ui;

import ch.virtbad.serint.client.config.Config;
import ch.virtbad.serint.client.config.ConfigHandler;

/**
 * This class represents a scene that is specific for menu purpose. Components are automatically centered and the gui scale is applied
 * @author Virt
 */
public abstract class MenuScene extends UiScene{

    /**
     * Creates a menu scene
     */
    public MenuScene() {
        super(UiHelper.calculateFromUiScale(ConfigHandler.getConfig().getUiScale()));
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
