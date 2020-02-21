package dev.game.test.client.entity.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.Batch;
import dev.game.test.client.entity.components.VisualComponent;
import dev.game.test.core.entity.components.PositionComponent;

public class VisualRenderSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;

    private ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<VisualComponent> visualMapper = ComponentMapper.getFor(VisualComponent.class);

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

            this.batch.draw(
                    visual.region,
                    position.x,
                    position.y
            );
        }

        this.batch.end();
    }
}
