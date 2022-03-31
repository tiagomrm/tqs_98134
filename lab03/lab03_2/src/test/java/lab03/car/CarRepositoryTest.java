package lab03.car;

import lab03.model.Car;
import lab03.repositories.CarRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CarRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CarRepository carRepository;

    @Test
    void whenFindCarByExistingId_thenReturnCar() {
        Car car = new Car("test", "test1");
        entityManager.persistAndFlush(car);

        Car fromDb = carRepository.findById(car.getCarId()).orElse(null);
        assertThat(fromDb).isNotNull();
        assertThat(fromDb.getMaker()).isEqualTo( car.getMaker());
    }

    @Test
    void whenInvalidId_thenReturnEmpty() {
        Optional<Car> fromDb = carRepository.findById(-111L);
        assertThat(fromDb).isEmpty();
    }

    @Test
    void givenSetOfCars_whenFindAll_thenReturnAllCars() {
        Car corolla = new Car("Toyota", "Corolla");
        Car i8 = new Car("BMW", "i8");
        Car tesla = new Car("Tesla", "X");

        entityManager.persist(corolla);
        entityManager.persist(i8);
        entityManager.persist(tesla);
        entityManager.flush();

        List<Car> allCars = carRepository.findAll();

        assertThat(allCars).hasSize(3).extracting(Car::getMaker).containsOnly(corolla.getMaker(), i8.getMaker(), tesla.getMaker());
    }
}
