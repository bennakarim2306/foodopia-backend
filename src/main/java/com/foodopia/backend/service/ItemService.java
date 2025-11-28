package com.foodopia.backend.service;

import com.foodopia.backend.data.item.Item;
import com.foodopia.backend.data.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service für die Geschäftslogik rund um Items.
 * Bietet Methoden zum Hinzufügen, Aktualisieren, Löschen und Filtern von Items.
 * Unterstützt Bildspeicherung als Base64-String.
 */
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    /**
     * Gibt alle Items zurück.
     */
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    /**
     * Fügt ein Item mit Bild hinzu.
     */
    public Item addItemWithImage(Item item, MultipartFile imageFile) {
        try {
            String base64Image = Base64.getEncoder().encodeToString(imageFile.getBytes());
            item.setImageUrl("data:" + imageFile.getContentType() + ";base64," + base64Image);
        } catch (IOException e) {
            throw new RuntimeException("Bild konnte nicht verarbeitet werden", e);
        }
        return itemRepository.save(item);
    }

    /**
     * Fügt ein Item ohne Bild hinzu.
     */
    public Item addItem(Item item) {
        return itemRepository.save(item);
    }

    /**
     * Aktualisiert ein Item inkl. Bild.
     */
    public Item updateItemWithImage(String id, Item updatedItem, MultipartFile imageFile) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item nicht gefunden"));
        try {
            String base64Image = Base64.getEncoder().encodeToString(imageFile.getBytes());
            updatedItem.setImageUrl("data:" + imageFile.getContentType() + ";base64," + base64Image);
        } catch (IOException e) {
            throw new RuntimeException("Bild konnte nicht verarbeitet werden", e);
        }
        updatedItem.setId(id);
        return itemRepository.save(updatedItem);
    }

    /**
     * Aktualisiert ein Item ohne Bild.
     */
    public Item updateItem(String id, Item updatedItem) {
        if (!itemRepository.existsById(id)) {
            throw new RuntimeException("Item nicht gefunden");
        }
        updatedItem.setId(id);
        return itemRepository.save(updatedItem);
    }

    /**
     * Löscht ein Item anhand der ID.
     */
    public void deleteItem(String id) {
        itemRepository.deleteById(id);
    }

    /**
     * Gibt gefilterte Items zurück (Name, Typ, Preis, Geoposition, Distanz).
     */
    public List<Item> getFilteredItems(
            Optional<String> name,
            Optional<String> type,
            Optional<Double> minPrice,
            Optional<Double> maxPrice,
            Optional<Double> lat,
            Optional<Double> lng,
            Optional<Double> distanceKm
    ) {
        List<Item> items = itemRepository.findAll();
        return items.stream()
                .filter(item -> name.map(n -> item.getName().toLowerCase().contains(n.toLowerCase())).orElse(true))
                .filter(item -> type.map(t -> item.getType().equalsIgnoreCase(t)).orElse(true))
                .filter(item -> minPrice.map(min -> item.getPrice() >= min).orElse(true))
                .filter(item -> maxPrice.map(max -> item.getPrice() <= max).orElse(true))
                .filter(item -> {
                    if (lat.isPresent() && lng.isPresent() && distanceKm.isPresent()
                            && item.getAddress() != null) {
                        double d = distance(lat.get(), lng.get(), item.getAddress().getLat(), item.getAddress().getLng());
                        return d <= distanceKm.get();
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    /**
     * Berechnet die Entfernung zwischen zwei Punkten (Haversine-Formel).
     */
    private double distance(double lat1, double lng1, double lat2, double lng2) {
        final int R = 6371; // Radius der Erde in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lng2 - lng1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}

