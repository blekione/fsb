package com.blekione.fsb.repository;

import com.blekione.fsb.model.entity.Game;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class GameRepository {
    private final static int INITIAL_MAP_CAPACITY = 1_000;
    private final Map<String, Game> games = new ConcurrentHashMap<>(INITIAL_MAP_CAPACITY);

    public Game save(Game game) {
        Objects.requireNonNull(game, "The given game cannot be null.");
        games.put(game.name(), game);
        return game;
    }

    public Optional<Game> findByName(String gameName) {
        return Optional.ofNullable(games.get(gameName));
    }

    public Collection<Game> findAll() {
        return games.values();
    }
}
