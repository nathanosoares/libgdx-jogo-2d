package dev.game.test.api.client.screens;

import com.badlogic.gdx.Screen;

public interface IScreenManager {

    void setCurrentScreen(Screen newScreen);

    void rollbackScreen();

}
