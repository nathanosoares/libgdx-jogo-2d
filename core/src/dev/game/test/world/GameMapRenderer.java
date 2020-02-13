package dev.game.test.world;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class GameMapRenderer extends OrthogonalTiledMapRenderer {

    public GameMapRenderer(TiledMap map, float unitScale) {
        super(map, unitScale);
    }

    @Override
    public void renderObject(MapObject object) {
        beginRender();
        if (object instanceof Player) {
            Player player = (Player) object;
            player.draw(this.batch);
        }
        endRender();
    }
}
