package com.blekione.fsb.repository;

import com.blekione.fsb.model.GameStatus;
import com.blekione.fsb.model.entity.Game;
import com.blekione.fsb.test.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GameRepositoryTest {
    private final String gameName = "ARSvsMCI";

    private GameRepository repository;

    @BeforeEach
    void setUp() {
        repository = new GameRepository();
    }

    @Test
    void shouldAddGameAndFindIt() throws Exception {
        // given
        var game = Game.create(gameName, TestUtils.getFixedNowTime(), GameStatus.ACTIVE);

        // when
        repository.save(game);
        Optional<Game> actualGame = repository.findByName(gameName);

        // then
        assertThat(actualGame).isNotEmpty();
        assertThat(actualGame.get()).isEqualTo(game);
    }

    @Test
    void shouldThrowNPEWhenGameIsNull() throws Exception {
        // given
        Game nullGame = null;

        // then
        assertThatThrownBy(() -> repository.save(nullGame))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("The given game cannot be null.");
    }

    @Test
    void shouldReplaceOldValueWithNewWithSameName() throws Exception {
        // given
        var initiaGame = Game.create(gameName, TestUtils.getFixedNowTime(), GameStatus.ACTIVE);
        repository.save(initiaGame);
        var newGame = Game.create(gameName, TestUtils.getFixedNowTime(), GameStatus.INACTIVE);

        // when
        repository.save(newGame);
        Optional<Game> actualGame = repository.findByName(gameName);

        // then
        assertThat(actualGame.get()).isEqualTo(newGame);
    }



     @Test
     void shouldFindAll() throws Exception {
         // given
         var gameA = Game.create(gameName, TestUtils.getFixedNowTime(), GameStatus.ACTIVE);
         repository.save(gameA);
         String gameBName = "LEGvsLCH";
         var gameB = Game.create(gameBName, TestUtils.getFixedNowTime(), GameStatus.ACTIVE);
         repository.save(gameB);
         String gameCName = "CHEvsMUN";
         var gameC = Game.create(gameCName, TestUtils.getFixedNowTime(), GameStatus.ACTIVE);
         repository.save(gameC);

         // when
         Collection<Game> actualGames = repository.findAll();

         // then
         var expectedGames = Set.of(gameA, gameB, gameC);
         assertThat(actualGames).containsExactlyInAnyOrderElementsOf(expectedGames);
     }
}