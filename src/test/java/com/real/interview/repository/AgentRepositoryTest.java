package com.real.interview.repository;

import com.real.interview.entity.Agent;
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
class AgentRepositoryTest {
    @Autowired
    private AgentRepository agentRepository;

    @Test
    @DisplayName("Save and find Agent by ID")
    void testSaveAndFindById() {
        Agent agent = new Agent();
        agent.setName("John Doe");
        agent.setEmail("john@example.com");
        agent.setPhone("1234567890");
        Agent saved = agentRepository.save(agent);
        Optional<Agent> found = agentRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("John Doe", found.get().getName());
    }

    @Test
    @DisplayName("Find all Agents")
    void testFindAll() {
        Agent agent = new Agent();
        agent.setName("Jane Doe");
        agent.setEmail("jane@example.com");
        agent.setPhone("0987654321");
        agentRepository.save(agent);
        assertFalse(agentRepository.findAll().isEmpty());
    }

    @Test
    @DisplayName("Delete Agent")
    void testDelete() {
        Agent agent = new Agent();
        agent.setName("Delete Me");
        agent.setEmail("delete@example.com");
        agent.setPhone("0000000000");
        Agent saved = agentRepository.save(agent);
        agentRepository.deleteById(saved.getId());
        assertFalse(agentRepository.findById(saved.getId()).isPresent());
    }

    @Test
    @DisplayName("Find by email returns Agent if exists")
    void testFindByEmail() {
        Agent agent = new Agent();
        agent.setName("Email Test");
        agent.setEmail("emailtest@example.com");
        agent.setPhone("1111111111");
        agentRepository.save(agent);
        Optional<Agent> found = agentRepository.findAll().stream()
            .filter(a -> "emailtest@example.com".equals(a.getEmail()))
            .findFirst();
        assertTrue(found.isPresent());
        assertEquals("Email Test", found.get().getName());
    }

    @Test
    @DisplayName("Find by non-existent ID returns empty")
    void testFindByIdNotFound() {
        Optional<Agent> found = agentRepository.findById(-1L);
        assertFalse(found.isPresent());
    }
}
