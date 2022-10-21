package com.bot.sup.api.rest;

import com.bot.sup.mapper.RouteMapper;
import com.bot.sup.model.dto.RouteCreateDto;
import com.bot.sup.model.dto.RouteDto;
import com.bot.sup.model.entity.Route;
import com.bot.sup.repository.RouteRepository;
import com.bot.sup.service.route.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/route")
@RequiredArgsConstructor
public class RouteController {
    private final RouteRepository routeRepository;
    private final RouteService routeService;
    private final RouteMapper routeMapper;


    @GetMapping("/all")
    public ResponseEntity<List<Route>> getAllRoute(@RequestParam(required = false) Long id) {
        try {
            List<Route> routes = new ArrayList<>();

            if (id == null) {
                routes.addAll(routeRepository.findAll());
            } else {
                routes.addAll(routeRepository.findAllById(Collections.singleton(id)));
            }

            if (routes.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(routes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Route> getRouteById(@PathVariable("id") Long id) {
        Optional<Route> route = Optional.ofNullable(routeService.getRouteById(id));

        if (route.isPresent()) {
            return new ResponseEntity<>(route.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<Route> createRoute(@RequestBody RouteCreateDto routeCreateDto) {
       try {
           return new ResponseEntity<>(routeService.createRoute(routeCreateDto), HttpStatus.CREATED);
       } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

}
