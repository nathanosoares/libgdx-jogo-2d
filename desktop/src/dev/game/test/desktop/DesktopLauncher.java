package dev.game.test.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import dev.game.test.client.ClientApplication;

public class DesktopLauncher {

    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration clientConfig = new Lwjgl3ApplicationConfiguration();
        clientConfig.setMaximized(false);
        clientConfig.useVsync(true);

        new Lwjgl3Application(new ClientApplication(), clientConfig);
    }
}
