package com.blekione.fsb.service;

import com.blekione.fsb.exception.GameException;
import com.blekione.fsb.model.GameStatus;
import com.blekione.fsb.model.entity.Game;
import com.blekione.fsb.repository.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Service
public class GameService {
    private static final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository repository;
    private final Clock clock;

    private final Object lock = new Object();

    @Autowired
    public GameService(GameRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    private static boolean validateName(String name) {
        boolean isInvalid = name == null || name.trim().isEmpty();
        if (isInvalid) {
            log.error("The given name [{}] is not a valid name game name", name);
        }
        return !isInvalid;
    }

    /**
     * Creates and persists a new game with the given name and {@link GameStatus#ACTIVE}.
     * <p>
     * Thread safe.
     * </p>
     *
     * @param gameName - the game name (Required)
     * @return a new game with the given name
     * @throws GameException when game name is not set or is not valid
     *                       or when a game with the given name already exists
     */
    public Game createGame(String gameName) {
        if (!validateName(gameName)) {
            throw new GameException("Cannot create a new game because the given name is not set or has illegal value. "
                    + "The given name is [" + gameName + "].");
        }
        if (repository.findByName(gameName).isPresent()) {
            log.error("Cannot create a new game because a game with name [{}] already exists.", gameName);
            throw new GameException("Cannot create a new game because a game with name [" + gameName + "] already exists.");
        }

        synchronized (lock) {
            var game = Game.create(gameName, LocalDateTime.now(clock), GameStatus.ACTIVE);
            var savedGame = repository.save(game);
            log.info("Successfully saved {} game", gameName);
            return savedGame;
        }
    }

    /**
     * Finds a game with the given name.
     *
     * @param gameName - a name of the game to be found
     * @return a game with the given name or nothing if a game with the given name cannot be found
     * @throws GameException when gameName is not valid
     */
    public Optional<Game> find(String gameName) {
        if (!validateName(gameName)) {
            throw new GameException("The given name [" + gameName + "] is invalid.");
        }
        return repository.findByName(gameName).map(game -> {
            log.info("Found a game with name {}", gameName);
            return game;
        }).or(() -> {
            log.info("Cannot find a game with name {}", gameName);
            return Optional.empty();
        });
    }

    /**
     * Changes a status of a game with the given name to the given status.
     * <p>
     * Thread safe
     * </p>
     *
     * @param gameName  - the name of the game to be changed (Required)
     * @param newStatus - the new game status (Required)
     * @return a gema with the given name with updated status
     * @throws GameException when the given game name is invalid or the game
     *                       with that name cannot be found or when the given status
     *                       cannot be recognised
     */
    public Game changeStatus(String gameName, String newStatus) {
        if (!validateName(gameName)) {
            throw new GameException("Cannot change a game status because the given name is not set or has illegal value. "
                    + "The given name is [" + gameName + "].");
        }
        if (GameStatus.UNRECOGNIZED == GameStatus.from(newStatus)) {
            throw new GameException("Cannot change a game status because the given status is not recognised. "
                    + "The given status is [" + newStatus + "].");
        }

        synchronized (lock) {
            return repository.findByName(gameName).map(savedGame -> {
                var updatedGame = Game.create(savedGame.name(), savedGame.createdOn(), GameStatus.from(newStatus));
                return repository.save(updatedGame);
            }).orElseThrow(
                    () -> new GameException("Cannot change the status because a game with name [" + gameName + "] doesn't exists.")
            );
        }
    }

    public Collection<Game> getAll() {
        return repository.findAll();
    }
}
