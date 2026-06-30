package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.DishRequestDto;
import com.pragma.powerup.application.dto.request.DishStatusRequestDto;
import com.pragma.powerup.application.dto.request.DishUpdateRequestDto;
import com.pragma.powerup.application.dto.response.DishListResponseDto;
import com.pragma.powerup.application.dto.response.PageResponseDto;
import com.pragma.powerup.application.handler.IDishHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/dishes")
@RequiredArgsConstructor
public class DishRestController {

    private final IDishHandler dishHandler;

    @Operation(summary = "Add a new dish")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dish created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data or not the restaurant owner", content = @Content),
            @ApiResponse(responseCode = "404", description = "Restaurant not found", content = @Content)
    })
    @PreAuthorize("hasRole('PROPIETARIO')")
    @PostMapping
    public ResponseEntity<Void> saveDish(@Valid @RequestBody DishRequestDto dishRequestDto) {
        dishHandler.saveDish(dishRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Update price and description of a dish")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dish updated", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data or not the restaurant owner", content = @Content),
            @ApiResponse(responseCode = "404", description = "Dish not found", content = @Content)
    })
    @PreAuthorize("hasRole('PROPIETARIO')")
    @PatchMapping("/{dishId}")
    public ResponseEntity<Void> updateDish(@PathVariable Long dishId, @Valid @RequestBody DishUpdateRequestDto dishUpdateRequestDto) {
        dishHandler.updateDish(dishId, dishUpdateRequestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Enable or disable a dish")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dish status updated", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data or not the restaurant owner", content = @Content),
            @ApiResponse(responseCode = "404", description = "Dish not found", content = @Content)
    })
    @PreAuthorize("hasRole('PROPIETARIO')")
    @PatchMapping("/{dishId}/status")
    public ResponseEntity<Void> changeDishStatus(@PathVariable Long dishId, @Valid @RequestBody DishStatusRequestDto dishStatusRequestDto) {
        dishHandler.changeDishStatus(dishId, dishStatusRequestDto.getActivo());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "List active dishes of a restaurant (paginated, filterable by category)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paginated dishes returned", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters", content = @Content),
            @ApiResponse(responseCode = "404", description = "Restaurant not found", content = @Content)
    })
    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<PageResponseDto<DishListResponseDto>> listDishesByRestaurant(
            @PathVariable Long restaurantId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(dishHandler.listDishesByRestaurant(restaurantId, categoryId, page, size));
    }
}
