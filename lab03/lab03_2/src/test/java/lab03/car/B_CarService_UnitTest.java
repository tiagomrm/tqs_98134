package lab03.car;

import lab03.model.Car;
import lab03.repositories.CarRepository;
import lab03.services.CarManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class B_CarService_UnitTest {

    @Mock( lenient = true)
    private CarRepository carRepository;

    @InjectMocks
    private CarManagerService carManagerService;


    @BeforeEach
    public void setUp() {

        Car corolla = new Car("Toyota", "Corolla");
        corolla.setCarId(24L);

        Car i8 = new Car("BMW", "i8");
        Car tesla = new Car("Tesla", "X");

        List<Car> allCars = Arrays.asList(corolla, i8, tesla);

        Mockito.when(carRepository.findByCarId(corolla.getCarId())).thenReturn(corolla);
        Mockito.when(carRepository.findAll()).thenReturn(allCars);
        Mockito.when(carRepository.findByCarId(-99L)).thenReturn(null);
    }

    @Test
    void whenValidId_thenCarShouldBeFound() {
        Car fromDb = carManagerService.getCarDetails(24L).orElse(null);

        assertThat(fromDb).isNotNull();

        assertThat(fromDb.getMaker()).isEqualTo("Toyota");
        assertThat(fromDb.getModel()).isEqualTo("Corolla");

        verifyFindByIdIsCalledOnce();
    }

    @Test
    void whenInValidId_thenCarShouldNotBeFound() {
        Optional<Car> fromDb = carManagerService.getCarDetails(-99L);

        verifyFindByIdIsCalledOnce();
        assertThat(fromDb).isEmpty();
    }

    @Test
    void given3Cars_whenGetAll_thenReturn3Records() {

        Car corolla = new Car("Toyota", "Corolla");
        Car i8 = new Car("BMW", "i8");
        Car tesla = new Car("Tesla", "X");

        List<Car> allCars = carManagerService.getAllCars();
        verifyFindAllCarsIsCalledOnce();

        assertThat(allCars).hasSize(3).extracting(Car::getMaker).contains(corolla.getMaker(), i8.getMaker(), tesla.getMaker());
    }


    private void verifyFindByIdIsCalledOnce() {
        Mockito.verify(carRepository, VerificationModeFactory.times(1)).findByCarId(Mockito.anyLong());
    }

    private void verifyFindAllCarsIsCalledOnce() {
        Mockito.verify(carRepository, VerificationModeFactory.times(1)).findAll();
    }

}
