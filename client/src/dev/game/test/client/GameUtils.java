package dev.game.test.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.game.test.client.entity.components.AnimateStateComponent;
import dev.game.test.client.entity.components.FacingVisualFlipComponent;
import dev.game.test.client.entity.components.VisualComponent;
import dev.game.test.core.PlayerUtils;
import dev.game.test.core.entity.Player;
import dev.game.test.core.entity.state.PlayerState;

import java.util.Random;
import java.util.UUID;

public class GameUtils {

    public static void clearScreen(int red, int green, int blue, int alpha) {
        Gdx.gl.glClearColor(red / 255f, green / 255f, blue / 255f, alpha / 255f);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }


    public static Player buildLocalPlayer() {

        Texture texture = new Texture("rpg-pack/chars/gabe/gabe-idle-run.png");
        String username;

        if (System.getProperty("username") != null) {
            username = System.getProperty("username");
        } else {
            username = String.format("Player%d", new Random().nextInt(1000));
        }

        Player player = PlayerUtils.buildLocalPlayer(UUID.randomUUID(), username);

        player.add(new VisualComponent());
        player.add(new FacingVisualFlipComponent());

        // Animate
        {
            TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / 7, texture.getHeight());

            TextureRegion[] idleFrames = new TextureRegion[1];
            TextureRegion[] walkFrames = new TextureRegion[6];

            int index = 0;
            for (int i = 0; i < 7; i++) {
                if (i == 0) {
                    idleFrames[0] = tmp[0][i];
                    continue;
                }
                walkFrames[index++] = tmp[0][i];
            }

            Animation<TextureRegion> idleAnimation = new Animation<>(0.135f, idleFrames);
            Animation<TextureRegion> walkAnimation = new Animation<>(0.135f, walkFrames);
            Animation<TextureRegion> runningAnimation = new Animation<>(0.105f, walkFrames);


            AnimateStateComponent animateStateComponent = new AnimateStateComponent();

            animateStateComponent.animations.put(PlayerState.IDLE, idleAnimation);
            animateStateComponent.animations.put(PlayerState.WALK, walkAnimation);
            animateStateComponent.animations.put(PlayerState.RUNNING, runningAnimation);

            player.add(animateStateComponent);
        }

        return player;
    }
}
