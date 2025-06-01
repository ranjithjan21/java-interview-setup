package com.real.interview.repository;

import com.real.interview.entity.Client;
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
class ClientRepositoryTest {
    @Autowired
    private ClientRepository clientRepository;

    @Test
    @DisplayName("Save and find Client by ID")
    void testSaveAndFindById() {
        Client client = new Client();
        client.setName("Alice Smith");
        client.setEmail("alice@example.com");
        Client saved = clientRepository.save(client);
        Optional<Client> found = clientRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Alice Smith", found.get().getName());
    }

    @Test
    @DisplayName("Find all Clients")
    void testFindAll() {
        Client client = new Client();
        client.setName("Bob Brown");
        client.setEmail("bob@example.com");
        clientRepository.save(client);
        assertFalse(clientRepository.findAll().isEmpty());
    }

    @Test
    @DisplayName("Delete Client")
    void testDelete() {
        Client client = new Client();
        client.setName("Delete Me");
        client.setEmail("delete@example.com");
        Client saved = clientRepository.save(client);
        clientRepository.deleteById(saved.getId());
        assertFalse(clientRepository.findById(saved.getId()).isPresent());
    }

    @Test
    @DisplayName("Find by non-existent ID returns empty")
    void testFindByIdNotFound() {
        Optional<Client> found = clientRepository.findById(-1L);
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Find by email returns Client if exists")
    void testFindByEmail() {
        Client client = new Client();
        client.setName("Email Test");
        client.setEmail("emailtest@example.com");
        clientRepository.save(client);
        Optional<Client> found = clientRepository.findAll().stream()
            .filter(c -> "emailtest@example.com".equals(c.getEmail()))
            .findFirst();
        assertTrue(found.isPresent());
        assertEquals("Email Test", found.get().getName());
    }
}
