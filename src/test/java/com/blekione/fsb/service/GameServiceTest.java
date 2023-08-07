package com.blekione.fsb.service;

import com.blekione.fsb.exception.GameException;
import com.blekione.fsb.model.GameStatus;
import com.blekione.fsb.model.entity.Game;
import com.blekione.fsb.repository.GameRepository;
import com.blekione.fsb.test.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {
    private final String gameName = "ARSvsMCI";

    @Mock
    private GameRepository repository;

    private GameService service;

    @BeforeEach
    void setUp() {
        service = new GameService(repository, TestUtils.getFixedClock());
    }

    @Test
    void shouldCreateANewGameFromTheGivenDto() throws Exception {
        // given
        when(repository.findByName(gameName)).thenReturn(Optional.empty());
        var game = Game.create(gameName, TestUtils.getFixedNowTime(), GameStatus.ACTIVE);
        when(repository.save(game)).thenReturn(game);

        // when
        var actualGame = service.createGame(gameName);

        // then
        assertThat(actualGame).isEqualTo(game);

        verify(repository, times(1)).save(game);
    }

    @Test
    void shouldThrowErrorWhenTheGameWithGivenNameAlreadyExists() throws Exception {
        // given
        var game = Game.create(gameName, TestUtils.getFixedNowTime(), GameStatus.ACTIVE);
        when(repository.findByName(gameName)).thenReturn(Optional.of(game));

        // then
        assertThatThrownBy(() -> service.createGame(gameName))
                .isInstanceOf(GameException.class)
                .hasMessage("Cannot create a new game because a game with name [ARSvsMCI] already exists.");
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {" ", "    ", "\t", "\n"})
    void shouldThrowErrorWhenTheGameNameIsNotSet(String name) throws Exception {
        // then
        assertThatThrownBy(() -> service.createGame(name))
                .isInstanceOf(GameException.class)
                .hasMessageContaining("Cannot create a new game because the given name is not set or has illegal value." +
                        " The given name is [");
    }

    @Test
    void shouldReturnGameWhenExist() throws Exception {
        // given
        var game = Game.create(gameName, TestUtils.getFixedNowTime(), GameStatus.ACTIVE);
        when(repository.findByName(gameName)).thenReturn(Optional.of(game));

        // when
        Optional<Game> actualGame = service.find(gameName);

        // then
        assertThat(actualGame).isNotEmpty();
        assertThat(actualGame.get()).isEqualTo(game);
    }

    @Test
    void shouldReturnEmptyWhenGameNotFoundForTheGivenName() throws Exception {
        // given
        when(repository.findByName(gameName)).thenReturn(Optional.empty());

        // when
        Optional<Game> actualGame = service.find(gameName);

        // then
        assertThat(actualGame).isEmpty();
    }



     @Test
     void shouldThrowErrorWhenTheGameNameIsInvalid() throws Exception {
         // given
         var illGameName = "";

         // then
         assertThatThrownBy(() -> service.find(illGameName))
                 .isInstanceOf(GameException.class)
                 .hasMessage("The given name [] is invalid.");     }

    @Test
    void shouldChangeTheGameStatusToTheGivenStatus() throws Exception {
        // given
        var initialGame = Game.create(gameName, TestUtils.getFixedNowTime(), GameStatus.ACTIVE);
        when(repository.findByName(gameName)).thenReturn(Optional.of(initialGame));

        var updatedGame = Game.create(gameName, TestUtils.getFixedNowTime(), GameStatus.INACTIVE);
        when(repository.save(updatedGame)).thenReturn(updatedGame);

        // when
        var actualGame = service.changeStatus(gameName, "INACTIVE");

        // then
        assertThat(actualGame).isEqualTo(updatedGame);
    }

    @Test
    void shouldThrowErrorWhenTheGameNameIsNotSet() throws Exception {
        // given
        var illGameName = "";

        // then
        assertThatThrownBy(() -> service.changeStatus(illGameName, "INACTIVE"))
                .isInstanceOf(GameException.class)
                .hasMessage("Cannot change a game status because the given name is not set or has illegal value." +
                        " The given name is [].");
    }

    @Test
    void shouldThrowErrorWhenNewStatusIsUnrecognised() throws Exception {
        // given
        String unrecognisedStatus = "BLAH";

        // then
        assertThatThrownBy(() -> service.changeStatus(gameName, unrecognisedStatus))
                .isInstanceOf(GameException.class)
                .hasMessage("Cannot change a game status because the given status is not recognised." +
                        " The given status is [BLAH].");
    }


    @Test
    void shouldThrowErrorWhenCannotFindAGameWithTheGivenName() throws Exception {
        // given
        when(repository.findByName(gameName)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> service.changeStatus(gameName, "INACTIVE"))
                .isInstanceOf(GameException.class)
                .hasMessage("Cannot change the status because a game with name [ARSvsMCI] doesn't exists.");
    }

    @Test
    void shouldReturnAll() throws Exception {
        // given
        var gameA = Game.create(gameName, TestUtils.getFixedNowTime(), GameStatus.ACTIVE);
        String gameBName = "LEGvsLCH";
        var gameB = Game.create(gameBName, TestUtils.getFixedNowTime(), GameStatus.ACTIVE);
        String gameCName = "CHEvsMUN";
        var gameC = Game.create(gameCName, TestUtils.getFixedNowTime(), GameStatus.ACTIVE);
        Set<Game> games = Set.of(gameA, gameB, gameC);
        when(repository.findAll()).thenReturn(games);

        // when
        Collection<Game> actualGames = service.getAll();

        // then
        assertThat(actualGames).containsExactlyInAnyOrderElementsOf(games);
    }
}