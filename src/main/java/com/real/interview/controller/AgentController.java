package com.real.interview.controller;

import com.real.interview.entity.Agent;
import com.real.interview.repository.AgentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/agents")
public class AgentController {
    private final AgentRepository agentRepo;

    public AgentController(AgentRepository agentRepo) {
        this.agentRepo = agentRepo;
    }

    @Operation(summary = "Get all agents", description = "Returns a list of all agents.")
    @ApiResponse(responseCode = "200", description = "List of agents",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Agent.class),
            examples = @ExampleObject(value = "[{\"id\":1,\"name\":\"John Doe\",\"email\":\"john@example.com\",\"phone\":\"1234567890\"}]")))
    @GetMapping
    public List<Agent> getAll() {
        return agentRepo.findAll();
    }

    @Operation(summary = "Get agent by ID", description = "Returns an agent by its ID.")
    @ApiResponse(responseCode = "200", description = "Agent found",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Agent.class),
            examples = @ExampleObject(value = "{\"id\":1,\"name\":\"John Doe\",\"email\":\"john@example.com\",\"phone\":\"1234567890\"}")))
    @ApiResponse(responseCode = "404", description = "Agent not found")
    @GetMapping("/{id}")
    public ResponseEntity<Agent> getById(
            @Parameter(description = "ID of the agent", example = "1") @PathVariable Long id) {
        return agentRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new agent", description = "Creates a new agent.")
    @ApiResponse(responseCode = "200", description = "Agent created",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Agent.class),
            examples = @ExampleObject(value = "{\"name\":\"John Doe\",\"email\":\"john@example.com\",\"phone\":\"1234567890\"}")))
    @PostMapping
    public Agent create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Agent to create",
                required = true,
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Agent.class),
                    examples = @ExampleObject(value = "{\"name\":\"John Doe\",\"email\":\"john@example.com\",\"phone\":\"1234567890\"}")))
            @RequestBody Agent agent) {
        return agentRepo.save(agent);
    }

    @Operation(summary = "Update an agent", description = "Updates an existing agent by ID.")
    @ApiResponse(responseCode = "200", description = "Agent updated",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Agent.class),
            examples = @ExampleObject(value = "{\"id\":1,\"name\":\"Jane Smith\",\"email\":\"jane@example.com\",\"phone\":\"9876543210\"}")))
    @ApiResponse(responseCode = "404", description = "Agent not found")
    @PutMapping("/{id}")
    public ResponseEntity<Agent> update(
            @Parameter(description = "ID of the agent", example = "1") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Updated agent data",
                required = true,
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Agent.class),
                    examples = @ExampleObject(value = "{\"name\":\"Jane Smith\",\"email\":\"jane@example.com\",\"phone\":\"9876543210\"}")))
            @RequestBody Agent updated) {
        return agentRepo.findById(id)
                .map(agent -> {
                    agent.setName(updated.getName());
                    agent.setEmail(updated.getEmail());
                    agent.setPhone(updated.getPhone());
                    return ResponseEntity.ok(agentRepo.save(agent));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete an agent", description = "Deletes an agent by ID.")
    @ApiResponse(responseCode = "204", description = "Agent deleted")
    @ApiResponse(responseCode = "404", description = "Agent not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID of the agent", example = "1") @PathVariable Long id) {
        if (agentRepo.existsById(id)) {
            agentRepo.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

