package dev.game.test.client;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.common.collect.Maps;
import dev.game.test.client.entity.components.AnimateComponent;
import dev.game.test.client.entity.components.VisualComponent;
import dev.game.test.core.PlayerUtils;
import dev.game.test.core.entity.Player;
import dev.game.test.core.entity.components.StateComponent;
import dev.game.test.core.entity.player.PlayerState;
import dev.game.test.core.entity.components.DirectionComponent;

import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public class GameUtils {

    public static void clearScreen(int red, int green, int blue, int alpha) {
        Gdx.gl.glClearColor(red / 255f, green / 255f, blue / 255f, alpha / 255f);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }


    public static Player buildClientPlayer(UUID uuid, String username) {

        Player player = PlayerUtils.buildLocalPlayer(uuid, username);

        player.add(new VisualComponent());

        // Animate
        {
            AnimateComponent animateComponent = new AnimateComponent();


            Map<String, Predicate<Entity>> textures = Maps.newHashMap();

            textures.put("char_front_idle.png", entity -> {
                if (StateComponent.MAPPER.get(entity).machine.getCurrentState() != PlayerState.IDLE) {
                    return false;
                }

                return Math.abs(DirectionComponent.MAPPER.get(entity).degrees) >= 90;
            });

            textures.put("char_front_walk.png", entity -> {
                if (StateComponent.MAPPER.get(entity).machine.getCurrentState() != PlayerState.WALK) {
                    return false;
                }

                return Math.abs(DirectionComponent.MAPPER.get(entity).degrees) >= 90;
            });

            textures.put("char_back_idle.png", entity -> {
                if (StateComponent.MAPPER.get(entity).machine.getCurrentState() != PlayerState.IDLE) {
                    return false;
                }

                return Math.abs(DirectionComponent.MAPPER.get(entity).degrees) < 90;
            });

            textures.put("char_back_walk.png", entity -> {
                if (StateComponent.MAPPER.get(entity).machine.getCurrentState() != PlayerState.WALK) {
                    return false;
                }

                return Math.abs(DirectionComponent.MAPPER.get(entity).degrees) < 90;
            });

            for (Map.Entry<String, Predicate<Entity>> entry : textures.entrySet()) {
                Texture texture = new Texture(entry.getKey());

                TextureRegion[] frames = TextureRegion.split(texture, texture.getWidth() / 2, texture.getHeight())[0];

                Animation<TextureRegion> animation = new Animation<>(0.135f, frames);

                animateComponent.animations.put(entry.getValue(), animation);
            }


            player.add(animateComponent);
        }

        return player;
    }
}
