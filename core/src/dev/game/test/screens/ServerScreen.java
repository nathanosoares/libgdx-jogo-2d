package dev.game.test.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import dev.game.test.GameUtils;
import dev.game.test.inputs.GameInputAdapter;
import dev.game.test.world.Player;
import lombok.Getter;

public class ServerScreen extends ScreenAdapter {

    public static final int WIDTH = 320 * 4;
    public static final int HEIGHT = 180 * 4;

    public ServerScreen() {
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        GameUtils.clearScreen(255, 255, 255, 100);
    }

    @Override
    public void dispose() {

    }
}
