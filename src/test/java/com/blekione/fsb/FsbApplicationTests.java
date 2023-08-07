package com.blekione.fsb;

import com.blekione.fsb.model.GameStatus;
import com.blekione.fsb.model.dto.GameDto;
import com.blekione.fsb.model.dto.GameRequestDto;
import com.blekione.fsb.model.dto.GameStatusDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// The test depends on each other and as such they cannot be run independently.
// If I had more time to spend on it, I would clean up the state of the service
// by deleting all games, injecting repository and adding remove() method to it.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FsbApplicationTests {
    private String gameName = "ARSvsMCI";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Order(1)
    void shouldCreateGame() {
        // when
        ResponseEntity<GameDto> actualGame = restTemplate.postForEntity("/games/", new GameRequestDto(gameName), GameDto.class);

        // then
        assertThat(actualGame.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(actualGame.getHeaders().getLocation().toString()).isEqualTo("games/" + gameName);

        assertThat(actualGame.getBody().getName()).isEqualTo(gameName);
        assertThat(actualGame.getBody().getStatus()).isEqualTo(GameStatus.ACTIVE);
    }



     @Test
     @Order(2)
     void shouldReturnGameByName() throws Exception {
         // when
         ResponseEntity<GameDto> actualGame = restTemplate.getForEntity("/games/" + gameName, GameDto.class);

         // then
         assertThat(actualGame.getStatusCode()).isEqualTo(HttpStatus.OK);

         assertThat(actualGame.getBody().getName()).isEqualTo(gameName);
         assertThat(actualGame.getBody().getStatus()).isEqualTo(GameStatus.ACTIVE);
     }

    @Test
    @Order(3)
    void shouldChangeGameStatus() throws Exception {
        // when
        restTemplate.put("/games/" + gameName, new GameStatusDto("INACTIVE"));
        ResponseEntity<GameDto> actualGame = restTemplate.getForEntity("/games/" + gameName, GameDto.class);


        // then
        assertThat(actualGame.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(actualGame.getBody().getName()).isEqualTo(gameName);
        assertThat(actualGame.getBody().getStatus()).isEqualTo(GameStatus.INACTIVE);
    }

    @Test
    @Order(4)
    void shouldReturnAll() throws Exception {
        // when
        ResponseEntity<List<GameDto>> actualGames = restTemplate.exchange("/games/", HttpMethod.GET, null, new ParameterizedTypeReference<List<GameDto>>() {
        });

        // then
        assertThat(actualGames.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(actualGames.getBody().size()).isEqualTo(1);
        assertThat(actualGames.getBody().get(0).getName()).isEqualTo(gameName);
    }
}
