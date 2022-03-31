package lab03.controllers;


import lab03.model.Car;
import lab03.repositories.CarRepository;
import lab03.services.CarManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CarController {

    private final CarManagerService carManagerService;

    public CarController(CarManagerService carManagerService) {
        this.carManagerService = carManagerService;
    }

    @PostMapping("/cars" )
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        return new ResponseEntity<>(carManagerService.save(car), HttpStatus.CREATED);
    }

    @GetMapping(path="/cars" )
    public List<Car> getAllCars() {
        return carManagerService.getAllCars();
    }

    @GetMapping(path="/cars/{id}" )
    public Optional<Car> getCarDetails(@PathVariable Long id) {
        return carManagerService.getCarDetails(id);
    }
}
