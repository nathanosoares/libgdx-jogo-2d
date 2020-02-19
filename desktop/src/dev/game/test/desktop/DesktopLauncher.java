package dev.game.test.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import dev.game.test.client.ClientApplication;

public class DesktopLauncher {

    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setMaximized(false);
        config.useVsync(false);

        new Lwjgl3Application(new ClientApplication(arg), config);
    }
}
