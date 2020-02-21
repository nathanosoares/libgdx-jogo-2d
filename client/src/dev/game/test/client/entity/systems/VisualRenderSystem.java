package dev.game.test.client.entity.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import dev.game.test.client.entity.components.VisualComponent;
import dev.game.test.core.entity.components.NamedComponent;
import dev.game.test.core.entity.components.PositionComponent;

public class VisualRenderSystem extends EntitySystem {

    private final BitmapFont font = new BitmapFont();

    private ImmutableArray<Entity> entities;

    private ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<VisualComponent> visualMapper = ComponentMapper.getFor(VisualComponent.class);
    private ComponentMapper<NamedComponent> namedMapper = ComponentMapper.getFor(NamedComponent.class);

    private final Batch batch;

    public VisualRenderSystem(Batch batch) {
        this.batch = batch;
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(
                PositionComponent.class,
                VisualComponent.class
        ).get());
    }

    @Override
    public void update(float deltaTime) {
        PositionComponent position;
        VisualComponent visual;

        this.batch.begin();

        for (int i = 0; i < entities.size(); ++i) {
            Entity entity = entities.get(i);

            position = positionMapper.get(entity);
            visual = visualMapper.get(entity);

            float nameOffsetY = 0;
            if (visual.region != null) {
                this.batch.draw(
                        visual.region,
                        position.x,
                        position.y,
                        0,
                        0,
                        visual.region.getRegionWidth(),
                        visual.region.getRegionHeight(),
                        1 / 16f,
                        1 / 16f,
                        0
                );

                nameOffsetY = visual.region.getRegionHeight() / 16f;
            }

            if (namedMapper.has(entity)) {
                this.font.getData().setScale(1f / 32f);
//                this.font.draw(
//                        this.batch,
//                        namedMapper.get(entity).name,
//                        position.x, position.y + nameOffsetY + 1
//                );
            }
        }

        this.batch.end();
    }
}
