package com.moppletop.dsb.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerCharacterRepository extends JpaRepository<PlayerCharacterEntity, Integer> {

    List<PlayerCharacterEntity> findByUserId(Integer userId);

}
