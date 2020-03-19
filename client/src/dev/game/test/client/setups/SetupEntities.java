package dev.game.test.client.setups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.game.test.api.entity.EnumEntityType;
import dev.game.test.client.entity.components.VisualComponent;
import dev.game.test.core.entity.HitProjectile;
import dev.game.test.core.entity.components.CollisiveComponent;
import dev.game.test.core.setup.Setup;

public class SetupEntities implements Setup {

    @Override
    public void setup() {

        EnumEntityType.HIT_PROJECTILE.addPostCreateListener(entity -> {

            HitProjectile hit = (HitProjectile) entity;

            VisualComponent visual = new VisualComponent();
            visual.region = new TextureRegion(new Texture(Gdx.files.internal("hit.png")));
            hit.add(visual);

            hit.add(new CollisiveComponent(16f / 16f, 16f / 16f));
        });

    }
}
