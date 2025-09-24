package com.hop.drivesharing.hopapplication.data.item;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, String> {
    @NonNull List<Item> findAll();

    @NonNull Optional<Item> findById(@NonNull String id);

    Item save(Item item);
}
