package dev.game.test.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import dev.game.test.GameApplication;
import dev.game.test.screens.GameScreen;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.forceExit = false;
		config.width = GameScreen.WIDTH;
		config.height = GameScreen.HEIGHT;
		new LwjglApplication(new GameApplication(), config);
	}
}
