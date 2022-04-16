package com.moppletop.dsb.repository;

import com.moppletop.dsb.domain.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Integer> {
}
