package dev.game.test.api;

public interface IApplication<G extends IGame> {

    G getGame();

}
