package com.moppletop.dsb.repository;

import com.moppletop.dsb.domain.entity.PlayerCharacterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerCharacterRepository extends JpaRepository<PlayerCharacterEntity, Integer> {

    List<PlayerCharacterEntity> findByUserId(Integer userId);

}
