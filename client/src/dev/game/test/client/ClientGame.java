package dev.game.test.client;

import dev.game.test.api.IClientGame;
import dev.game.test.api.registry.IRegistryManager;
import dev.game.test.client.registry.RegistryBlocks;
import dev.game.test.client.screens.ScreenManager;
import dev.game.test.core.Game;
import dev.game.test.core.block.Block;
import lombok.Getter;

public class ClientGame extends Game implements IClientGame {

    @Getter
    private final ScreenManager screenManager;

    //

    @Getter
    private final ClientManager clientManager;

    public ClientGame() {
        this.screenManager = new ScreenManager();
        this.clientManager = new ClientManager();
    }

    @Override
    public void setupRegistries(IRegistryManager registryManager) {
        registryManager.addRegistry(Block.class, new RegistryBlocks());
    }

}
