package com.moppletop.dsb.db.repository;

import com.moppletop.dsb.db.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Integer> {
}
