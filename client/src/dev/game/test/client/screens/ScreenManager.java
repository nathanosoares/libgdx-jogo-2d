package dev.game.test.client.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Disposable;
import dev.game.test.api.IClientGame;
import dev.game.test.api.client.screens.IScreenManager;
import dev.game.test.client.ClientApplication;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScreenManager implements IScreenManager, Disposable {

    private final IClientGame clientGame;

    @Getter
    private Screen currentScreen, lastScreen;

    public void setCurrentScreen(Screen newScreen) {
        if (this.currentScreen != null) {
            this.currentScreen.hide();
        }

        if (this.lastScreen != null) {
            this.lastScreen.dispose();
        }

        this.lastScreen = this.currentScreen;

        this.currentScreen = newScreen;
        this.currentScreen.show();
    }

    public void rollbackScreen() {
        if (this.currentScreen != null) {
            this.currentScreen.hide();
        }

        Screen temp = this.lastScreen;

        this.lastScreen = this.currentScreen;
        this.currentScreen = temp;
        this.currentScreen.resume();
    }

    @Override
    public void dispose() {
        if (this.currentScreen != null) {
            this.currentScreen.dispose();
        }

        if (this.lastScreen != null) {
            this.lastScreen.dispose();
        }
    }
}
