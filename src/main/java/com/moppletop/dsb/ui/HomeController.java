package com.moppletop.dsb.ui;

import com.moppletop.dsb.character.PlayerCharacter;
import com.moppletop.dsb.character.PlayerCharacterService;
import com.moppletop.dsb.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.transaction.Transactional;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Transactional
class HomeController {

    private final PlayerCharacterService playerCharacterService;

    @GetMapping
    public String home(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("title", "Characters");
        model.addAttribute("userId", user.getId());

        List<PlayerCharacter> characters = playerCharacterService.getCharacters(user.getId());

        model.addAttribute("characters", characters);

        return "characters/root";
    }

}
