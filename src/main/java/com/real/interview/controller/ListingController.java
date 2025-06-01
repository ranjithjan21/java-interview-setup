package com.real.interview.controller;

import com.real.interview.dto.ListingDto;
import com.real.interview.service.ListingService;
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
@RequestMapping("/api/v1/listings")
public class ListingController {
    private final ListingService listingService;

    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    @Operation(summary = "Get all listings", description = "Returns a list of all listings.")
    @GetMapping
    public List<ListingDto> getAll() {
        return listingService.getAll();
    }

    @Operation(summary = "Get listing by ID", description = "Returns a listing by its ID.")
    @ApiResponse(responseCode = "200", description = "Listing found",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ListingDto.class)))
    @ApiResponse(responseCode = "404", description = "Listing not found")
    @GetMapping("/{id}")
    public ResponseEntity<ListingDto> getById(
            @Parameter(description = "ID of the listing", example = "1") @PathVariable Long id) {
        return listingService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new listing", description = "Creates a new listing.")
    @ApiResponse(responseCode = "201", description = "Listing created",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ListingDto.class),
            examples = @ExampleObject(value = "{\"title\":\"Sample Listing\",\"price\":100000,\"address\":\"123 Main St\"}")))
    @PostMapping
    public ResponseEntity<ListingDto> create(@RequestBody ListingDto listingDto) {
        ListingDto created = listingService.create(listingDto);
        return ResponseEntity.status(201).body(created);
    }

    @Operation(summary = "Update a listing", description = "Updates an existing listing by ID.")
    @ApiResponse(responseCode = "200", description = "Listing updated",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ListingDto.class)))
    @ApiResponse(responseCode = "404", description = "Listing not found")
    @PutMapping("/{id}")
    public ResponseEntity<ListingDto> update(
            @Parameter(description = "ID of the listing", example = "1") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Updated listing data",
                required = true,
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ListingDto.class),
                    examples = @ExampleObject(value = "{\"title\":\"Updated Listing\",\"price\":120000,\"address\":\"456 Main St\"}")))
            @RequestBody ListingDto updatedDto) {
        return listingService.update(id, updatedDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a listing", description = "Deletes a listing by ID.")
    @ApiResponse(responseCode = "204", description = "Listing deleted")
    @ApiResponse(responseCode = "404", description = "Listing not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID of the listing", example = "1") @PathVariable Long id) {
        if (listingService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Assign agent to listing", description = "Assigns an agent to a listing.")
    @ApiResponse(responseCode = "200", description = "Agent assigned",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ListingDto.class),
            examples = @ExampleObject(value = "{\"id\":1,\"title\":\"Sample Listing\",\"agentId\":10}")))
    @ApiResponse(responseCode = "404", description = "Listing or agent not found")
    @PutMapping("/{listingId}/assign-agent/{agentId}")
    public ResponseEntity<ListingDto> assignAgent(
            @Parameter(description = "ID of the listing", example = "1") @PathVariable Long listingId,
            @Parameter(description = "ID of the agent", example = "10") @PathVariable Long agentId) {
        return listingService.assignAgent(listingId, agentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Add interested client", description = "Adds a client as interested in a listing.")
    @ApiResponse(responseCode = "200", description = "Client added",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ListingDto.class),
            examples = @ExampleObject(value = "{\"id\":1,\"title\":\"Sample Listing\",\"interestedClients\":[100]}")))
    @ApiResponse(responseCode = "404", description = "Listing or client not found")
    @PostMapping("/{listingId}/interest/{clientId}")
    public ResponseEntity<ListingDto> addInterestedClient(
            @Parameter(description = "ID of the listing", example = "1") @PathVariable Long listingId,
            @Parameter(description = "ID of the client", example = "100") @PathVariable Long clientId) {
        return listingService.addInterestedClient(listingId, clientId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
