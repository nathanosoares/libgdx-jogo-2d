package dev.game.test.core.entity;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import dev.game.test.api.entity.IPlayer;
import dev.game.test.api.keybind.Keybind;
import dev.game.test.api.util.EnumFacing;
import dev.game.test.core.entity.components.*;
import dev.game.test.core.entity.player.componenets.MovementComponent;
import dev.game.test.core.entity.player.PlayerState;

import java.util.UUID;

public class Player extends Entity implements IPlayer {

    public Player(UUID uuid, String name) {
        super(uuid);

        this.add(new NamedComponent(name));
    }

    @Override
    protected void setupDefaultComponents() {
        super.setupDefaultComponents();

        this.add(new CollisiveComponent(24f / 16f, 24f / 16f));
        this.add(new MovementComponent());
        this.add(new FacingComponent(EnumFacing.EAST));
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
}
