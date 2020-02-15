package dev.game.test.world;

import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import dev.game.test.world.entities.Player;

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

    public void renderDecorations(Player player) {
        beginRender();
        MapLayer layer = map.getLayers().get(1);
        renderMapLayer(layer, player);
        endRender();
    }

    protected void renderMapLayer(MapLayer layer, Player player) {
        if (!layer.isVisible()) return;

        if (layer instanceof MapGroupLayer) {
            MapLayers childLayers = ((MapGroupLayer) layer).getLayers();
            for (int i = 0; i < childLayers.size(); i++) {
                MapLayer childLayer = childLayers.get(i);
                if (!childLayer.isVisible()) continue;
                renderMapLayer(childLayer);
            }
        } else {
            if (layer instanceof TiledMapTileLayer) {
                renderTileLayer((TiledMapTileLayer) layer);
            } else if (layer instanceof TiledMapImageLayer) {
                renderImageLayer((TiledMapImageLayer) layer);
            } else {
                renderObjects(layer);
            }
        }
    }
}
