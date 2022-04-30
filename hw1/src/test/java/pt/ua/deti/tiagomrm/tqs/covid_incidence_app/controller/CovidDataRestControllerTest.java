package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.controller.CovidDataRestController;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.service.CovidDataService;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest
class CovidDataRestControllerTest {

    @InjectMocks
    private CovidDataRestController restController;

    @MockBean
    private CovidDataService service;

    @Test
    void getGlobalReportForDate() {
    }

    @Test
    void getCountryReportForDate() {
    }
}