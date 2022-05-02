package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.controller.CovidDataRestController;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.data.CovidReport;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.service.CovidDataService;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.service.Key;

import java.lang.reflect.Array;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@WebMvcTest(SpringExtension.class)
class CovidDataRestControllerTest {

    @InjectMocks
    private CovidDataRestController restController = new CovidDataRestController();

    @Mock
    private CovidDataService service;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @AfterEach
    void tearDown(){
        reset(service);
    }

    @Test
    void testSingleRegionalReportIsSuccessful_thenReturnOptionalWrappedCovidReport() throws JsonProcessingException {
        LocalDate date = LocalDate.parse("26/04/2022", formatter);

        Optional<CovidReport> optionalReport =
                Optional.of(CovidReport.getRegionalCovidReport(
                        "Portugal", date, 23864, 472, 903, 23
                ));

        when(service.getReport(Key.getRegionalKey("Portugal", date))).thenReturn(optionalReport);

        ResponseEntity<String> response = restController.getReportForDate(Optional.of("Portugal"), date);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        assertThat(mapper.readValue(response.getBody(), CovidReport.class), equalTo(optionalReport.get()));
    }

    @Test
    void testMultipleRegionalReportsIsSuccessful_thenReturnListOfCovidReports() throws JsonProcessingException {
        LocalDate startDate = LocalDate.parse("24/04/2022", formatter);
        LocalDate endDate = LocalDate.parse("26/04/2022", formatter);

        CovidReport regionalReportA = CovidReport.getRegionalCovidReport("Portugal", endDate, 23864, 472, 903, 23);
        CovidReport regionalReportB = CovidReport.getRegionalCovidReport("Portugal", LocalDate.parse("25/04/2022", formatter), 23392, 595, 880, 26);
        CovidReport regionalReportC = CovidReport.getRegionalCovidReport("Portugal", startDate, 22797, 444, 854, 34);

        when(service
                .getCovidRegionalReportsFromDateToDate("Portugal", startDate, endDate))
                .thenReturn(Arrays.asList(regionalReportC, regionalReportB, regionalReportA));

        ObjectMapper mapper = new ObjectMapper();

        ResponseEntity<String> response = restController.getReportsForRangeOfDates(Optional.of("Portugal"), startDate, endDate);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        List<CovidReport> responseRegions = mapper.readValue(response.getBody(), new TypeReference<List<CovidReport>>() {});

        assertThat(responseRegions, equalTo(Arrays.asList(regionalReportC, regionalReportB, regionalReportA)));
    }

    @Test
    void testMultipleGlobalReportsIsSuccessful_thenReturnListOfCovidReports() throws JsonProcessingException {
        LocalDate startDate = LocalDate.parse("24/04/2022", formatter);
        LocalDate endDate = LocalDate.parse("26/04/2022", formatter);

        CovidReport globalReportA = CovidReport.getGlobalCovidReport(endDate, 23864, 472, 903, 23);
        CovidReport globalReportB = CovidReport.getGlobalCovidReport(LocalDate.parse("25/04/2022", formatter), 23392, 595, 880, 26);
        CovidReport globalReportC = CovidReport.getGlobalCovidReport(startDate, 22797, 444, 854, 34);

        when(service
                .getCovidGlobalReportsFromDateToDate(startDate, endDate))
                .thenReturn(Arrays.asList(globalReportC, globalReportB, globalReportA));

        ObjectMapper mapper = new ObjectMapper();

        ResponseEntity<String> response = restController.getReportsForRangeOfDates(Optional.empty(), startDate, endDate);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        List<CovidReport> responseRegions = mapper.readValue(response.getBody(), new TypeReference<List<CovidReport>>() {});

        assertThat(responseRegions, equalTo(Arrays.asList(globalReportC, globalReportB, globalReportA)));
    }

    @Test
    void testSingleRegionalReportIsUnsuccessful_thenReturnEmptyOptional() {
        LocalDate date = LocalDate.parse("26/04/2022", formatter);

        when(service.getReport(Key.getRegionalKey("Portugal", date))).thenReturn(Optional.empty());

        assertThat(restController.getReportForDate(Optional.of("Portugal"), date).getStatusCode(), not(equalTo(200)));
    }

    @Test
    void testSingleGlobalReportIsSuccessful_thenReturnOptionalWrappedCovidReport() throws JsonProcessingException {
        LocalDate date = LocalDate.parse("26/04/2022", formatter);

        Optional<CovidReport> optionalReport =
                Optional.of(CovidReport.getGlobalCovidReport(
                        date, 2971475, 74729, 206544, 3698)
                );

        when(service.getReport(Key.getGlobalKey(date))).thenReturn(optionalReport);

        ResponseEntity<String> response = restController.getReportForDate(Optional.empty(), date);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        assertThat(mapper.readValue(response.getBody(), CovidReport.class), equalTo(optionalReport.get()));
    }

    @Test
    void testSingleGlobalReportIsUnsuccessful_thenReturnEmptyList() {
        LocalDate date = LocalDate.parse("26/04/2022", formatter);

        when(service.getGlobalReportForDate(date)).thenReturn(Optional.empty());

        assertThat(restController.getReportForDate(Optional.empty(), date).getStatusCode(), not(equalTo(200)));
    }

    @Test
    void testGetAllRegions_ReturnsListOfStrings() throws JsonProcessingException {
        List<String> regions = Arrays.asList("Portugal", "Italy", "France");
        when(service.getRegionsList()).thenReturn(regions);

        ObjectMapper mapper = new ObjectMapper();

        ResponseEntity<String> response = restController.getAllRegions();
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        List<String> responseRegions = mapper.readValue(response.getBody(), new TypeReference<List<String>>() {});

        assertThat(responseRegions, equalTo(regions));
    }

    @Test
    void testGetAllRegions_ReturnsEmptyListOfStrings() {
        when(service.getRegionsList()).thenReturn(Collections.emptyList());
        assertThat(restController.getAllRegions().getStatusCode(), not(equalTo(HttpStatus.OK)));
    }

    @Test
    void testGetCacheReport() throws JsonProcessingException {
        String report = "{\"initialization_date\":\"2022-05-02T13:58:45.9503375\",\"calls\":2,\"hits\":1,\"misses\":1}";
        when(service.getCacheReportJSON()).thenReturn(report);

        assertThat(restController.getCacheReport().getBody(), equalTo(report));
    }
}