package ch.virtbad.serint.client.game.map;

import ch.virtbad.serint.client.game.GameContext;
import lombok.Getter;

public class MapRegister {

    @Getter
    private MapObject map;
    private boolean init;

    private GameContext context;

    public MapRegister(GameContext context) {
        this.context = context;
    }

    public void create(TileMap map){
        this.map = new MapObject(map);
        init = true;
    }

    public void update(float delta){
        if (map == null) return;

        if (init) {
            map.setContext(context);
            map.init();
            init = false;
        }

        map.update(delta);
    }

    public void draw(){
        if (map == null || init) return;

        map.draw();
    }
}
