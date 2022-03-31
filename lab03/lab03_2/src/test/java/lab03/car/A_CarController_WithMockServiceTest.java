package lab03.car;

import lab03.controllers.CarController;
import lab03.model.Car;
import lab03.services.CarManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarController.class)
public class A_CarController_WithMockServiceTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CarManagerService service;


    @BeforeEach
    public void setUp() {
    }


    @Test
    void whenPostCar_thenCreateCar( ) throws Exception {
        Car corolla = new Car("Toyota", "Corolla");

        when( service.save(Mockito.any()) ).thenReturn( corolla );

        mvc.perform(
                        post("/api/cars").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(corolla)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.maker", is("Toyota")))
                .andExpect(jsonPath("$.model", is("Corolla")));

        verify(service, times(1)).save(Mockito.any());

    }


    @Test
    void givenManyCars_whenGetCars_thenReturnJsonArray( ) throws Exception {
        Car corolla = new Car("Toyota", "Corolla");
        Car i8 = new Car("BMW", "i8");
        Car tesla = new Car("Tesla", "X");

        List<Car> allCars = Arrays.asList(corolla, i8, tesla);

        when( service.getAllCars()).thenReturn(allCars);

        mvc.perform(
                get("/api/cars").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].maker", is("Toyota")))
                .andExpect(jsonPath("$[0].model", is("Corolla")))
                .andExpect(jsonPath("$[1].maker", is("BMW")))
                .andExpect(jsonPath("$[1].model", is("i8")))
                .andExpect(jsonPath("$[2].maker", is("Tesla")))
                .andExpect(jsonPath("$[2].model", is("X")));

        verify(service, times(1)).getAllCars();

    }


    @Test
    void givenManyCars_whenGetCarById_thenReturnJson( ) throws Exception {

        Car i8 = new Car("BMW", "i8");

        when( service.getCarDetails(2L)).thenReturn(java.util.Optional.of(i8));

        mvc.perform(
                get("/api/cars/2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.maker", is("BMW")))
                .andExpect(jsonPath("$.model", is("i8")));

        verify(service, times(1)).getCarDetails(Mockito.any());

    }
}
