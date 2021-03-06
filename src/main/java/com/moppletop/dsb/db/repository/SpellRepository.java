package com.moppletop.dsb.db.repository;

import com.moppletop.dsb.db.entity.PlayerCharacterEntity;
import com.moppletop.dsb.db.entity.SpellEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpellRepository extends JpaRepository<SpellEntity, Integer> {

    Optional<SpellEntity> findByCharacterIdAndSpell(Integer characterId, String spell);

    Optional<SpellEntity> findByCharacterAndSpell(PlayerCharacterEntity characterEntity, String spell);

}
