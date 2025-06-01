package com.real.interview.controller;

import com.real.interview.entity.Client;
import com.real.interview.repository.ClientRepository;
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
@RequestMapping("/api/v1/clients")
public class ClientController {
    private final ClientRepository clientRepo;

    public ClientController(ClientRepository clientRepo) {
        this.clientRepo = clientRepo;
    }

    @Operation(summary = "Get all clients", description = "Returns a list of all clients.")
    @GetMapping
    public List<Client> getAll() {
        return clientRepo.findAll();
    }

    @Operation(summary = "Get client by ID", description = "Returns a client by its ID.")
    @ApiResponse(responseCode = "200", description = "Client found",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class)))
    @ApiResponse(responseCode = "404", description = "Client not found")
    @GetMapping("/{id}")
    public ResponseEntity<Client> getById(
            @Parameter(description = "ID of the client", example = "1") @PathVariable Long id) {
        return clientRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new client", description = "Creates a new client.")
    @ApiResponse(responseCode = "200", description = "Client created",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class),
            examples = @ExampleObject(value = "{\"name\":\"Alice Smith\",\"email\":\"alice@example.com\",\"phone\":\"5551234567\"}")))
    @PostMapping
    public Client create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Client to create",
                required = true,
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Client.class),
                    examples = @ExampleObject(value = "{\"name\":\"Alice Smith\",\"email\":\"alice@example.com\",\"phone\":\"5551234567\"}")))
            @RequestBody Client client) {
        return clientRepo.save(client);
    }

    @Operation(summary = "Update a client", description = "Updates an existing client by ID.")
    @ApiResponse(responseCode = "200", description = "Client updated",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class)))
    @ApiResponse(responseCode = "404", description = "Client not found")
    @PutMapping("/{id}")
    public ResponseEntity<Client> update(
            @Parameter(description = "ID of the client", example = "1") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Updated client data",
                required = true,
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Client.class),
                    examples = @ExampleObject(value = "{\"name\":\"Bob Brown\",\"email\":\"bob@example.com\",\"phone\":\"5559876543\"}")))
            @RequestBody Client updated) {
        return clientRepo.findById(id)
                .map(client -> {
                    client.setName(updated.getName());
                    client.setEmail(updated.getEmail());
                    client.setPhone(updated.getPhone());
                    return ResponseEntity.ok(clientRepo.save(client));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a client", description = "Deletes a client by ID.")
    @ApiResponse(responseCode = "204", description = "Client deleted")
    @ApiResponse(responseCode = "404", description = "Client not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID of the client", example = "1") @PathVariable Long id) {
        if (clientRepo.existsById(id)) {
            clientRepo.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

