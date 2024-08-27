package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.CategoryRequestDto;
import com.pragma.powerup.application.handler.ICategoryHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryRestController {

    private final ICategoryHandler categoryHandler;

    @Operation(summary = "Add a new category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created", content = @Content),
            @ApiResponse(responseCode = "409", description = "Category already exists", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<Void> saveCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        categoryHandler.saveCategory(categoryRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Get all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All categories returned", content = @Content),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity.ok(categoryHandler.getAllCategories());
    }
}