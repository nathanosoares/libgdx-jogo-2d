package dev.game.test.server;

import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;

public class ServerLauncher {

    public static void main(String[] arg) {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();

        new HeadlessApplication(new ServerApplication(), config);
    }
}
