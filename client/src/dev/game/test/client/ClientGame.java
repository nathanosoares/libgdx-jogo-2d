package dev.game.test.client;

import dev.game.test.api.IClientGame;
import dev.game.test.api.registry.IRegistryManager;
import dev.game.test.client.block.BlockClient;
import dev.game.test.client.registry.RegistryBlocks;
import dev.game.test.client.screens.ScreenManager;
import dev.game.test.core.Game;
import lombok.Getter;

public class ClientGame extends Game implements IClientGame {

    @Getter
    private final ScreenManager screenManager;

    //

    @Getter
    private final ClientManager gameManager;

    public ClientGame() {
        this.screenManager = new ScreenManager();
        this.gameManager = new ClientManager();
    }

    @Override
    public void setupRegistries(IRegistryManager registryManager) {
        registryManager.addRegistry(BlockClient.class, new RegistryBlocks());
    }

}
