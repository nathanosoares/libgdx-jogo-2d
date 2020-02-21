package dev.game.test.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FPSLogger {

    private final boolean consoleDebug;
    private final boolean titleDebug;

    private long startTime = TimeUtils.nanoTime();

    public void log() {
        if (TimeUtils.nanoTime() - startTime > 1000000000) {

            if (this.consoleDebug) {
                Gdx.app.log("FPSLogger", "fps: " + Gdx.graphics.getFramesPerSecond());
            }

            if (this.titleDebug) {
                Gdx.graphics.setTitle("FPS: " + Gdx.graphics.getFramesPerSecond());
            }

            this.startTime = TimeUtils.nanoTime();
        }
    }
}
