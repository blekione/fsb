package com.blekione.fsb.controler;

import com.blekione.fsb.model.dto.GameDto;
import com.blekione.fsb.model.dto.GameRequestDto;
import com.blekione.fsb.model.dto.GameStatusDto;
import com.blekione.fsb.model.entity.Game;
import com.blekione.fsb.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/games")
public class GameControler {

    private GameService service;

    @Autowired
    public GameControler(GameService service) {
        this.service = service;
    }

    @PostMapping(path = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameDto> createGame(@RequestBody GameRequestDto requestedGame) {
        Game createdGame = service.createGame(requestedGame.getName());
        return ResponseEntity
                .created(URI.create("games/" + createdGame.name()))
                .body(GameDto.from(createdGame));
    }

    @GetMapping(path = "/{gameName}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameDto> getGame(@PathVariable("gameName") String gameName) {
        return service.find(gameName).map(game -> ResponseEntity.ok(GameDto.from(game)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(path = "/{gameName}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameDto> changeStatus(@PathVariable("gameName") String gameName,
                                                @RequestBody GameStatusDto newStatus) {
        Game updatedGame = service.changeStatus(gameName, newStatus.getNewStatus());
        return ResponseEntity.ok(GameDto.from(updatedGame));
    }

    // When working with a real database I would use a pagination here.
    // But because games are stored in memory in a map, which is unordered
    // and this is not a code which would be deployed to production when there would be
    // a risk of returning large number of records I've decided to return all records.
    @GetMapping(path = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GameDto>> getGames() {
        return ResponseEntity.ok().body(
                service.getAll().stream().map(game -> GameDto.from(game)).toList());
    }
}
