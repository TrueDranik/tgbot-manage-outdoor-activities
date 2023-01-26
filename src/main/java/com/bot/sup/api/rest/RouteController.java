package com.bot.sup.api.rest;

import com.bot.sup.model.dto.RouteDto;
import com.bot.sup.service.route.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/route")
@RequiredArgsConstructor
@Tag(name = "Маршрут")
public class RouteController {
    private final RouteService routeService;

    @GetMapping
    @Operation(summary = "Получить список всех маршрутов")
    public List<RouteDto> getAllRoute() {
        return routeService.getAllRoute();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить маршрут по id")
    public RouteDto getRouteById(@PathVariable(name = "id") Long id) {
        return routeService.getRouteById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать новый маршрут")
    public RouteDto createRoute(@RequestBody RouteDto routeCreateDto) {
        return routeService.createRoute(routeCreateDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменить существующий маршрут")
    public RouteDto updateRoute(@PathVariable(name = "id") Long id, @RequestBody RouteDto routeCreateDto) {
        return routeService.updateRoute(id, routeCreateDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить маршрут по id")
    public void deleteRoute(@PathVariable(name = "id") Long id) {
        routeService.deleteRoute(id);
    }
}
