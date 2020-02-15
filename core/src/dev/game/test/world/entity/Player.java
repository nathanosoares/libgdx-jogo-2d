package dev.game.test.world.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import dev.game.test.world.block.EnumFacing;
import lombok.Getter;
import lombok.Setter;

public class Player extends Entity implements EntityRunner {

    private Vector2 lastPosition = new Vector2();

    @Getter
    @Setter
    private boolean running = true;

    @Getter
    private float runningSpeed = 7f;

    private float stateTime;

    private final Animation<TextureRegion> walkAnimation;
    private final Animation<TextureRegion> runningAnimation;
    private EnumFacing lastDirection = EnumFacing.WEST;

    public Player() {
        super(new Texture(Gdx.files.internal("rpg-pack/chars/gabe/gabe-idle-run.png")));

        TextureRegion[][] tmp = TextureRegion.split(this.texture, this.texture.getWidth() / 7, this.texture.getHeight());

        TextureRegion[] walkFrames = new TextureRegion[6];

        int index = 0;
        for (int i = 0; i < 7; i++) {
            if (i == 0) {
                continue;
            }
            walkFrames[index++] = tmp[0][i];
        }

        walkAnimation = new Animation<>(0.135f, walkFrames);
        runningAnimation = new Animation<>(0.105f, walkFrames);
    }


    @Override
    public void draw(Batch batch) {

        TextureRegion textureRegion = new TextureRegion(this.texture, 0, 0, 24, 24);

        this.stateTime += Gdx.graphics.getDeltaTime();

        if (!this.lastPosition.equals(this.position)) {
            if (this.isRunning()) {
                textureRegion = runningAnimation.getKeyFrame(stateTime, true);
            } else {
                textureRegion = walkAnimation.getKeyFrame(stateTime, true);
            }
        }

        EnumFacing direction = this.lastDirection;

        if (this.position.x != this.lastPosition.x) {
            if (this.position.x < this.lastPosition.x) {
                direction = EnumFacing.EAST;
            } else if (this.position.x > this.lastPosition.x) {
                direction = EnumFacing.WEST;
            }
        }


        float width = 24 / 10f;
        float x = position.x;
        if (direction == EnumFacing.EAST) {
            width *= -1;
            x += 24 / 10f;
        }

        this.lastDirection = direction;

        batch.draw(textureRegion, x, position.y, width, 24 / 10f);
    }

    @Override
    public boolean hasCollision() {
        return false;
    }

    @Override
    public boolean canLeaveMap() {
        return false;
    }

    @Override
    public Vector2 move(Vector2 vector2) {
        this.lastPosition = this.position.cpy();

        return super.move(vector2);
    }
}
