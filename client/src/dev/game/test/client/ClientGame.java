package dev.game.test.client;

import com.badlogic.ashley.core.Engine;
import dev.game.test.api.IClientGame;
import dev.game.test.api.registry.IRegistryManager;
import dev.game.test.client.net.handler.ClientConnectionHandler;
import dev.game.test.client.registry.RegistryBlocks;
import dev.game.test.client.screens.ScreenManager;
import dev.game.test.client.systems.ClientSystem;
import dev.game.test.core.Game;
import dev.game.test.core.block.Block;
import lombok.Getter;

public class ClientGame extends Game implements IClientGame {

    @Getter
    private final ScreenManager screenManager;

    @Getter
    private final ClientManager clientManager;

    @Getter
    private final ClientConnectionHandler connectionHandler;

    public ClientGame() {
        this.screenManager = new ScreenManager(this);
        this.clientManager = new ClientManager(GameUtils.buildLocalPlayer());
        this.connectionHandler = new ClientConnectionHandler(this);
    }

    @Override
    public void setupRegistries(IRegistryManager registryManager) {
        registryManager.addRegistry(Block.class, new RegistryBlocks());
    }

    @Override
    public void setupEngine(Engine engine) {
        engine.addSystem(new ClientSystem(this));

        super.setupEngine(engine);
    }

}
