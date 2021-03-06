package com.moppletop.dsb.controller.api;

import com.moppletop.dsb.domain.character.PlayerCharacter;
import com.moppletop.dsb.controller.api.model.CharacterResponse;
import com.moppletop.dsb.controller.api.model.CreateCharacterRequest;
import com.moppletop.dsb.system.player.PlayerCharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/character")
@RequiredArgsConstructor
public class CreateCharacterController {

    private final PlayerCharacterService playerCharacterService;

    @PostMapping
    public ResponseEntity<CharacterResponse> create(@RequestBody CreateCharacterRequest request) {
        PlayerCharacter character = playerCharacterService.createCharacter(request.getUserId());
        return ResponseEntity.ok(new CharacterResponse(character.getId()));
    }

}
