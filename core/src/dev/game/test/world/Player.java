package dev.game.test.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import lombok.Getter;

public class Player {

    private Texture texture;
    //    Animation walk
    private TextureAtlas atlas;
    private TextureRegion lastTextRegion;
    private Animation<TextureAtlas.AtlasRegion> walkingSouthWestAnimation;
    private Animation<TextureAtlas.AtlasRegion> walkingSouthEastAnimation;

    private float elapsedTime = 0f;

    @Getter
    private Vector3 location = new Vector3(0, 1, 0);
    private Vector3 oldLocation = new Vector3(0, 1, 0);


    public Player() {
        this.texture = new Texture(
                Gdx.files.internal("man/man.png")
        );

        this.atlas = new TextureAtlas(Gdx.files.internal("man/man.atlas"));

        Array<TextureAtlas.AtlasRegion> walkingSouthWest = atlas.findRegions("walking_south_west");
        this.walkingSouthWestAnimation = new Animation<>(1 / 6f, walkingSouthWest);

        Array<TextureAtlas.AtlasRegion> walkingSouthEast = atlas.findRegions("walking_south_east");
        this.walkingSouthEastAnimation = new Animation<>(1 / 6f, walkingSouthEast);
    }

    public void draw(Batch batch) {
//        Vector2 newLocation = GameUtils.cartesianToIsometric(location);
        Vector3 newLocation = location;

        elapsedTime += Gdx.graphics.getDeltaTime();

        TextureRegion textureRegion = null;

        if (!this.oldLocation.equals(this.location)) {
            Animation<TextureAtlas.AtlasRegion> animation = null;

            if (this.oldLocation.x - this.location.x > 0) {
                animation =   this.walkingSouthWestAnimation;
            } else if (this.oldLocation.x - this.location.x < 0) {
                animation =   this.walkingSouthEastAnimation;
            }

            if (animation != null) {
                textureRegion = animation.getKeyFrame(elapsedTime, true);

                this.lastTextRegion = animation.getKeyFrames()[1];
            }
        }

        if (textureRegion == null) {
            textureRegion = this.lastTextRegion == null
                    ? new TextureRegion(this.texture, 0, 0, 32, 48)
                    : this.lastTextRegion;

            this.lastTextRegion = textureRegion;
        }


        batch.draw(
                textureRegion,
                newLocation.x, newLocation.y, 32 * 2, 48 * 2
        );

    }

    public void move(Vector3 to) {
        this.oldLocation = location.cpy();
        this.location.add(to);
    }

    public void teleport(Vector2 to) {

    }
}
