package com.moppletop.dsb.controller.api;

import com.moppletop.dsb.domain.dto.character.NewPosition;
import com.moppletop.dsb.domain.dto.character.PlayerCharacter;
import com.moppletop.dsb.domain.dto.external.AddItemRequest;
import com.moppletop.dsb.domain.dto.external.AddSoulsRequest;
import com.moppletop.dsb.domain.dto.external.CharacterResponse;
import com.moppletop.dsb.domain.dto.external.ItemResponse;
import com.moppletop.dsb.domain.dto.external.LevelUpResponse;
import com.moppletop.dsb.domain.dto.external.ModifyClassFeaturesRequest;
import com.moppletop.dsb.domain.dto.external.PositionRequest;
import com.moppletop.dsb.domain.dto.external.RestRequest;
import com.moppletop.dsb.domain.dto.external.SetClassRequest;
import com.moppletop.dsb.domain.dto.external.SetItemAmountRequest;
import com.moppletop.dsb.domain.dto.external.SetNameRequest;
import com.moppletop.dsb.domain.dto.external.SetOriginRequest;
import com.moppletop.dsb.domain.dto.external.SoulsResponse;
import com.moppletop.dsb.domain.dto.external.SpellCastsRequest;
import com.moppletop.dsb.domain.dto.external.SpellRequest;
import com.moppletop.dsb.domain.dto.external.SpentUseRequest;
import com.moppletop.dsb.domain.dto.user.User;
import com.moppletop.dsb.exception.PlayerCharacterInvalidException;
import com.moppletop.dsb.service.PlayerCharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/api/character/{characterId}")
@RequiredArgsConstructor
@Transactional
public class CharacterController {

    private final PlayerCharacterService playerCharacterService;

    @ModelAttribute
    public void authenticate(@PathVariable(required = false) Integer characterId, @AuthenticationPrincipal User user) {
        if (characterId == null) {
            return;
        }

        PlayerCharacter character = playerCharacterService.getCharacter(characterId);

        if (character == null) {
            return;
        }

        if (character.getUserId() != user.getId()) {
            throw new PlayerCharacterInvalidException("You do not own this character", HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping
    public ResponseEntity<PlayerCharacter> get(@PathVariable(required = false) Integer characterId) {
        PlayerCharacter character = playerCharacterService.getCharacter(characterId);
        return ResponseEntity.ok(character);
    }

    @DeleteMapping
    public ResponseEntity<CharacterResponse> delete(@PathVariable(required = false) Integer characterId) {
        playerCharacterService.deleteCharacter(characterId);
        return ResponseEntity.ok(new CharacterResponse(characterId));
    }

    @PostMapping("/name")
    public ResponseEntity<CharacterResponse> setName(
            @PathVariable(required = false) Integer characterId,
            @RequestBody SetNameRequest request
    ) {
        playerCharacterService.setName(characterId, request.getName());
        return ResponseEntity.ok(new CharacterResponse(characterId));
    }

    @PostMapping("/class")
    public ResponseEntity<CharacterResponse> setClass(
            @PathVariable(required = false) Integer characterId,
            @RequestBody SetClassRequest request
    ) {
        playerCharacterService.setClass(characterId, request.getClassId());
        return ResponseEntity.ok(new CharacterResponse(characterId));
    }

    @PostMapping("/origin")
    public ResponseEntity<CharacterResponse> setOrigin(
            @PathVariable(required = false) Integer characterId,
            @RequestBody SetOriginRequest request
    ) {
        playerCharacterService.setOrigin(characterId, request.getOriginId());
        return ResponseEntity.ok(new CharacterResponse(characterId));
    }

    @PostMapping("/level-up")
    public ResponseEntity<LevelUpResponse> levelUp(@PathVariable(required = false) Integer characterId) {
        boolean newFeatures = playerCharacterService.levelUp(characterId);
        return ResponseEntity.ok(new LevelUpResponse(newFeatures));
    }

    @PostMapping("/class-feature")
    public ResponseEntity<CharacterResponse> modifyClassFeatures(
            @PathVariable(required = false) Integer characterId,
            @RequestBody ModifyClassFeaturesRequest request
    ) {
        playerCharacterService.modifyClassFeatures(characterId, request.getValueMap());
        return ResponseEntity.ok(new CharacterResponse(characterId));
    }

    @PostMapping("/rest")
    public ResponseEntity<NewPosition> rest(
            @PathVariable(required = false) Integer characterId,
            @RequestBody RestRequest request
    ) {
        NewPosition newPosition;

        switch (request.getType()) {
            case SHORT:
                newPosition = playerCharacterService.shortRest(characterId, request.getPosition(), request.getPositionDiceSpent());
                break;
            case LONG:
                newPosition = playerCharacterService.longRest(characterId);
                break;
            default:
                throw new PlayerCharacterInvalidException("Invalid rest type");
        }

        return ResponseEntity.ok(newPosition);
    }

    @PostMapping("/position")
    public ResponseEntity<NewPosition> position(
            @PathVariable(required = false) Integer characterId,
            @RequestBody PositionRequest request
    ) {
        NewPosition newPosition;

        switch (request.getAction()) {
            case MOD:
                newPosition = playerCharacterService.modPosition(characterId, request.getPosition());
                break;
            case SET:
                newPosition = playerCharacterService.setPosition(characterId, request.getPosition());
                break;
            case START_COMBAT:
                newPosition = playerCharacterService.startCombat(characterId, request.getPosition());
                break;
            case STOP_COMBAT:
                newPosition = playerCharacterService.stopCombat(characterId);
                break;
            default:
                throw new PlayerCharacterInvalidException("Invalid position action");
        }

        return ResponseEntity.ok(newPosition);
    }

    @PostMapping("/die")
    public ResponseEntity<CharacterResponse> die(@PathVariable(required = false) Integer characterId) {
        playerCharacterService.die(characterId);
        return ResponseEntity.ok(new CharacterResponse(characterId));
    }

    @PostMapping("/souls/on-person")
    public ResponseEntity<SoulsResponse> addSoulsToPerson(
            @PathVariable(required = false) Integer characterId,
            @RequestBody AddSoulsRequest request
    ) {
        int newSouls = playerCharacterService.addSoulsOnPerson(characterId, request.getSouls());
        return ResponseEntity.ok(new SoulsResponse(newSouls));
    }

    @PostMapping("/souls/bank")
    public ResponseEntity<SoulsResponse> moveSoulsToBank(
            @PathVariable(required = false) Integer characterId,
            @RequestBody AddSoulsRequest request
    ) {
        int newSouls = playerCharacterService.moveSoulsToBank(characterId, request.getSouls());
        return ResponseEntity.ok(new SoulsResponse(newSouls));
    }

    @PostMapping("/inventory")
    public ResponseEntity<ItemResponse> addItem(
            @PathVariable(required = false) Integer characterId,
            @RequestBody AddItemRequest request
    ) {
        int itemInventoryId = playerCharacterService.addItem(characterId, request.getItem(), request.getAmount(), request.getSlot());
        return ResponseEntity.ok(new ItemResponse(itemInventoryId));
    }

    @DeleteMapping("/inventory/{itemInventoryId}")
    public ResponseEntity<ItemResponse> removeItem(
            @PathVariable(required = false) Integer itemInventoryId
    ) {
        playerCharacterService.removeItem(itemInventoryId);
        return ResponseEntity.ok(new ItemResponse(itemInventoryId));
    }

    @PostMapping("/inventory/{itemInventoryId}/amount")
    public ResponseEntity<ItemResponse> setAmount(
            @PathVariable(required = false) Integer itemInventoryId,
            @RequestBody SetItemAmountRequest request
    ) {
        playerCharacterService.setItemAmount(itemInventoryId, request.getAmount());
        return ResponseEntity.ok(new ItemResponse(itemInventoryId));
    }

    @PostMapping("/inventory/{itemInventoryId}/equip")
    public ResponseEntity<ItemResponse> equipItem(
            @PathVariable(required = false) Integer characterId,
            @PathVariable(required = false) Integer itemInventoryId,
            @RequestBody AddItemRequest request
    ) {
        playerCharacterService.equipItem(characterId, itemInventoryId, request.getSlot());
        return ResponseEntity.ok(new ItemResponse(itemInventoryId));
    }

    @PostMapping("/uses")
    public ResponseEntity<CharacterResponse> useItem(
            @PathVariable(required = false) Integer characterId,
            @RequestBody SpentUseRequest request
    ) {
        playerCharacterService.setSpentUses(characterId, request.getKey(), request.getUses());
        return ResponseEntity.ok(new CharacterResponse(characterId));
    }

    @PostMapping("/spell/learn")
    public ResponseEntity<CharacterResponse> learnSpell(
            @PathVariable(required = false) Integer characterId,
            @RequestBody SpellRequest request
    ) {
        playerCharacterService.learnSpell(characterId, request.getSpell(), false);
        return ResponseEntity.ok(new CharacterResponse(characterId));
    }

    @PostMapping("/spell/forget")
    public ResponseEntity<CharacterResponse> forgetSpell(
            @PathVariable(required = false) Integer characterId,
            @RequestBody SpellRequest request
    ) {
        playerCharacterService.forgetSpell(characterId, request.getSpell());
        return ResponseEntity.ok(new CharacterResponse(characterId));
    }

    @PostMapping("/spell/attune")
    public ResponseEntity<CharacterResponse> attuneSpell(
            @PathVariable(required = false) Integer characterId,
            @RequestBody SpellRequest request
    ) {
        playerCharacterService.attuneSpell(characterId, request.getSpell());
        return ResponseEntity.ok(new CharacterResponse(characterId));
    }

    @PostMapping("/spell/unattune")
    public ResponseEntity<CharacterResponse> unattuneSpell(
            @PathVariable(required = false) Integer characterId,
            @RequestBody SpellRequest request
    ) {
        playerCharacterService.unattuneSpell(characterId, request.getSpell());
        return ResponseEntity.ok(new CharacterResponse(characterId));
    }

    @PostMapping("/spell/casts")
    public ResponseEntity<CharacterResponse> setSpellCasts(
            @PathVariable(required = false) Integer characterId,
            @RequestBody SpellCastsRequest request
    ) {
        playerCharacterService.setSpellCasts(characterId, request.getSpell(), request.getCasts());
        return ResponseEntity.ok(new CharacterResponse(characterId));
    }
}
