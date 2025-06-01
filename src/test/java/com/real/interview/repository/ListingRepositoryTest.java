package com.real.interview.repository;

import com.real.interview.entity.Listing;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class ListingRepositoryTest {
    @Autowired
    private ListingRepository listingRepository;

    @Test
    @DisplayName("Save and find Listing by ID")
    void testSaveAndFindById() {
        Listing listing = new Listing();
        listing.setTitle("Test Listing");
        Listing saved = listingRepository.save(listing);
        Optional<Listing> found = listingRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Test Listing", found.get().getTitle());
    }

    @Test
    @DisplayName("Find all Listings")
    void testFindAll() {
        Listing listing = new Listing();
        listing.setTitle("Another Listing");
        listingRepository.save(listing);
        assertFalse(listingRepository.findAll().isEmpty());
    }

    @Test
    @DisplayName("Delete Listing")
    void testDelete() {
        Listing listing = new Listing();
        listing.setTitle("Delete Me");
        Listing saved = listingRepository.save(listing);
        listingRepository.deleteById(saved.getId());
        assertFalse(listingRepository.findById(saved.getId()).isPresent());
    }

    @Test
    @DisplayName("Find by non-existent ID returns empty")
    void testFindByIdNotFound() {
        Optional<Listing> found = listingRepository.findById(-1L);
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Find by title returns Listing if exists")
    void testFindByTitle() {
        Listing listing = new Listing();
        listing.setTitle("Unique Title");
        listingRepository.save(listing);
        Optional<Listing> found = listingRepository.findAll().stream()
            .filter(l -> "Unique Title".equals(l.getTitle()))
            .findFirst();
        assertTrue(found.isPresent());
        assertEquals("Unique Title", found.get().getTitle());
    }
}
