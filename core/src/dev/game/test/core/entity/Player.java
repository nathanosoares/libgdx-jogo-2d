package dev.game.test.core.entity;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import dev.game.test.api.entity.EnumEntityType;
import dev.game.test.api.entity.IPlayer;
import dev.game.test.api.keybind.Keybind;
import dev.game.test.core.entity.components.BodyComponent;
import dev.game.test.core.entity.components.KeybindComponent;
import dev.game.test.core.entity.components.NamedComponent;
import dev.game.test.core.entity.components.StateComponent;
import dev.game.test.core.entity.player.PlayerState;
import dev.game.test.core.entity.player.componenets.HitComponent;
import dev.game.test.core.entity.player.componenets.MovementComponent;

import java.util.UUID;

public class Player extends Entity implements IPlayer {

    public Player(UUID uuid, String name) {
        super(uuid);

        this.add(new NamedComponent(name));
        this.add(new MovementComponent());
        this.add(new HitComponent());
    }

    @Override
    protected void setupDefaultComponents() {
        super.setupDefaultComponents();

        this.add(new BodyComponent());
        this.add(new StateComponent<>(new DefaultStateMachine<>(this, PlayerState.IDLE)));
        this.add(new KeybindComponent());
    }

    @Override
    public String getName() {
        return NamedComponent.MAPPER.get(this).name;
    }

    @Override
    public void setName(String name) {
        NamedComponent.MAPPER.get(this).name = name;
    }

    @Override
    public void addActiveKeybind(Keybind keybind) {
        KeybindComponent.MAPPER.get(this).activeKeybinds.add(keybind);
    }

    @Override
    public void removeActiveKeybind(Keybind keybind) {
        KeybindComponent.MAPPER.get(this).activeKeybinds.remove(keybind);
    }

    @Override
    public boolean hasActiveKeybind(Keybind keybind) {
        return KeybindComponent.MAPPER.get(this).activeKeybinds.contains(keybind);
    }

    @Override
    public EnumEntityType getType() {
        return EnumEntityType.PLAYER;
    }

    @Override
    public Shape createShape() {
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(this.getWidth() / 2f, this.getHeight() / 2f);

        return polygonShape;
    }

    @Override
    public float getWidth() {
        return 1;
    }

    @Override
    public float getHeight() {
        return 1;
    }
}
