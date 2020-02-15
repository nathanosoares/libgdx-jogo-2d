package dev.game.test.world;

import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import dev.game.test.world.entity.Entity;
import dev.game.test.world.entity.Player;

public class GameMapRenderer extends OrthogonalTiledMapRenderer {

    public GameMapRenderer(TiledMap map, float unitScale) {
        super(map, unitScale);
    }

    public void renderEntity(Entity entity) {
        beginRender();
        if (entity instanceof Player) {
            Player player = (Player) entity;
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
