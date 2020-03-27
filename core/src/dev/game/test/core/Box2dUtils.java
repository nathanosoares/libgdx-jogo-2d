package dev.game.test.core;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Box2dUtils {

    /** for internal, temporary usage */
    private static final Vector2 vec2_0 = new Vector2(), vec2_1 = new Vector2();

    public static ChainShape createEllipse(float width, float height, int steps) {
        ChainShape ellipse = new ChainShape();

        ellipse.createLoop(createEllipseVerts(width, height, steps));

        return ellipse;
    }

    public static int checkpoint(int h, int k, int x, int y, int a, int b) {
        return ((int) Math.pow((x - h), 2) / (int) Math.pow(a, 2))
                + ((int) Math.pow((y - k), 2) / (int) Math.pow(b, 2));
    }

    public static Vector2[] createEllipseVerts(float width, float height, int steps) {
        Vector2[] verts = new Vector2[steps];

        for (int i = 0; i < steps; i++) {
            float t = (float) (i * 2 * Math.PI) / steps;


            verts[i] = new Vector2(width * (float) Math.cos(t), height * (float) Math.sin(t));
        }

        return verts;
    }

    // clone

    public static Body clone(Body body) {
        return clone(body, false);
    }

    public static Body clone(Body body, boolean shapes) {
        Body clone = body.getWorld().createBody(createDef(body));
        clone.setUserData(body.getUserData());
        for(Fixture fixture : body.getFixtureList())
            clone(fixture, clone, shapes);
        return clone;
    }

    public static Fixture clone(Fixture fixture, Body body) {
        return clone(fixture, body, false);
    }

    public static Fixture clone(Fixture fixture, Body body, boolean shape) {
        FixtureDef fixtureDef = createDef(fixture);
        if(shape)
            fixtureDef.shape = clone(fixture.getShape());
        Fixture clone = body.createFixture(fixtureDef);
        clone.setUserData(clone.getUserData());
        return clone;
    }

    public static <T extends Shape> T clone(T shape) {
        T clone;
        switch(shape.getType()) {
            case Circle:
                @SuppressWarnings("unchecked")
                CircleShape circleClone = (CircleShape) (clone = (T) new CircleShape());
                circleClone.setPosition(((CircleShape) shape).getPosition());
                break;
            case Polygon:
                @SuppressWarnings("unchecked")
                PolygonShape polyClone = (PolygonShape) (clone = (T) new PolygonShape()),
                        poly = (PolygonShape) shape;
                float[] vertices = new float[poly.getVertexCount()];
                for(int i = 0; i < vertices.length; i++) {
                    poly.getVertex(i, vec2_0);
                    vertices[i++] = vec2_0.x;
                    vertices[i] = vec2_0.y;
                }
                polyClone.set(vertices);
                break;
            case Edge:
                @SuppressWarnings("unchecked")
                EdgeShape edgeClone = (EdgeShape) (clone = (T) new EdgeShape()),
                        edge = (EdgeShape) shape;
                edge.getVertex1(vec2_0);
                edge.getVertex2(vec2_1);
                edgeClone.set(vec2_0, vec2_1);
                break;
            default:
                return shape;
        }
        clone.setRadius(shape.getRadius());
        return clone;
    }

    /** @param body the body for which to setup a new {@link BodyDef}
     *  @return a new {@link BodyDef} instance that can be used to clone the given body */
    public static BodyDef createDef(Body body) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.active = body.isActive();
        bodyDef.allowSleep = body.isSleepingAllowed();
        bodyDef.angle = body.getAngle();
        bodyDef.angularDamping = body.getAngularDamping();
        bodyDef.angularVelocity = body.getAngularVelocity();
        bodyDef.awake = body.isAwake();
        bodyDef.bullet = body.isBullet();
        bodyDef.fixedRotation = body.isFixedRotation();
        bodyDef.gravityScale = body.getGravityScale();
        bodyDef.linearDamping = body.getLinearDamping();
        bodyDef.linearVelocity.set(body.getLinearVelocity());
        bodyDef.position.set(body.getPosition());
        bodyDef.type = body.getType();
        return bodyDef;
    }

    public static FixtureDef createDef(Fixture fixture) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = fixture.getDensity();
        Filter filter = fixture.getFilterData();
        fixtureDef.filter.categoryBits = filter.categoryBits;
        fixtureDef.filter.groupIndex = filter.groupIndex;
        fixtureDef.filter.maskBits = filter.maskBits;
        fixtureDef.friction = fixture.getFriction();
        fixtureDef.isSensor = fixture.isSensor();
        fixtureDef.restitution = fixture.getRestitution();
        fixtureDef.shape = fixture.getShape();
        return fixtureDef;
    }
}
