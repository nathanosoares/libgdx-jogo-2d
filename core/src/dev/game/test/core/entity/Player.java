package dev.game.test.core.entity;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import dev.game.test.api.entity.IPlayer;
import dev.game.test.api.keybind.Keybind;
import dev.game.test.api.net.packet.Packet;
import dev.game.test.api.util.EnumFacing;
import dev.game.test.api.world.IWorld;
import dev.game.test.core.entity.components.*;
import dev.game.test.core.entity.state.PlayerState;

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

        DefaultStateMachine<Entity, PlayerState> defaultStateMachine = new DefaultStateMachine<>(this, PlayerState.WALK);

        this.add(new StateComponent<>(defaultStateMachine));

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

    /*
        Keybinds
     */

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

    /*
        Networking
     */

    @Override
    public void sendPacket(Packet packet) {
        if(NetworkComponent.MAPPER.has(this)) {
            NetworkComponent.MAPPER.get(this).packetHandler.sendPacket(packet);
        }
    }
}
