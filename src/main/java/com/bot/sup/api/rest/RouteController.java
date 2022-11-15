package com.bot.sup.api.rest;

import com.bot.sup.model.dto.RouteCreateDto;
import com.bot.sup.model.entity.Route;
import com.bot.sup.service.route.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/route")
@RequiredArgsConstructor
@Tag(name = "Маршрут")
public class RouteController {
    private final RouteService routeService;

    @GetMapping
    @Operation(summary = "Получить список всех маршрутов")
    public ResponseEntity<List<Route>> getAllRoute(@RequestParam(required = false) Long id) {
        return new ResponseEntity<>(routeService.getAllRoute(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить маршрут по id")
    public ResponseEntity<Route> getRouteById(@PathVariable(name = "id") Long id) {

        Optional<Route> route = Optional.ofNullable(routeService.getRouteById(id));

        return route.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @PostMapping
    @Operation(summary = "Создать новый маршрут")
    public ResponseEntity<Route> createRoute(@RequestBody RouteCreateDto routeCreateDto) {
        return new ResponseEntity<>(routeService.createRoute(routeCreateDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменить существующий маршрут")
    public ResponseEntity<Route> updateRoute(@PathVariable(name = "id") Long id, @RequestBody RouteCreateDto routeCreateDto) {
        return new ResponseEntity<>(routeService.updateRoute(id, routeCreateDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить маршрут по id")
    public ResponseEntity<Route> deleteRoute(@PathVariable(name = "id") Long id) {
        routeService.deleteRoute(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
