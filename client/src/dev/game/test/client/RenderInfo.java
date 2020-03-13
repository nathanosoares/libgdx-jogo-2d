package dev.game.test.client;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class RenderInfo {

    public TextureRegion region;
    public float x;
    public float y;
    public float originX;
    public float originY;
    public float width;
    public float height;
    public float scaleX;
    public float scaleY;
    public float rotation;
}
