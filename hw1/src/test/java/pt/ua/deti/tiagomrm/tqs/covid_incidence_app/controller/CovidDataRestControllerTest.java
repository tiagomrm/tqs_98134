package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
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

import javax.swing.text.html.parser.Entity;
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
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CovidDataRestControllerTest {

    @InjectMocks
    private CovidDataRestController restController = new CovidDataRestController();

    @Mock
    private CovidDataService service;

    @Mock
    private ObjectMapper mapper;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @AfterEach
    void tearDown(){
        reset(service, mapper);
    }

    @Test
    void testGetSingleRegionalReportIsSuccessful_thenReturnOptionalWrappedCovidReport() throws JsonProcessingException {
        LocalDate date = LocalDate.parse("26/04/2022", formatter);

        CovidReport report = CovidReport.getRegionalCovidReport(
                "Portugal", date, 23864, 472, 903, 23
                );

        String serializedReport = "{\"region\":\"Portugal\",\"date\":\"2022-04-26\",\"totalCases\":23864,\"newCases\":472,\"totalDeaths\":903,\"newDeaths\":23}";

        when(service.getReport(Key.getRegionalKey("Portugal", date))).thenReturn(Optional.of(report));
        when(mapper.writeValueAsString(report)).thenReturn(serializedReport);

        ResponseEntity<String> response = restController.getReportForDate(Optional.of("Portugal"), date);

        assertThat(response, equalTo(ResponseEntity.ok(serializedReport)));
    }

    @Test
    void testGetSingleGlobalReportIsSuccessful_thenReturnOptionalWrappedCovidReport() throws JsonProcessingException {
        LocalDate date = LocalDate.parse("26/04/2022", formatter);

        CovidReport report = CovidReport.getGlobalCovidReport(
                date, 2971475, 74729, 206544, 3698);

        String serializedReport = "{\"region\":\"Global\",\"date\":\"2022-04-26\",\"totalCases\":2971475,\"newCases\":74729,\"totalDeaths\":206544,\"newDeaths\":3698}";

        when(service.getReport(Key.getGlobalKey(date))).thenReturn(Optional.of(report));
        when(mapper.writeValueAsString(report)).thenReturn(serializedReport);

        ResponseEntity<String> response = restController.getReportForDate(Optional.empty(), date);

        assertThat(response, equalTo(ResponseEntity.ok(serializedReport)));
    }

    @Test
    void testMultipleRegionalReportsIsSuccessful_thenReturnListOfCovidReports() throws JsonProcessingException {
        LocalDate startDate = LocalDate.parse("24/04/2022", formatter);
        LocalDate endDate = LocalDate.parse("26/04/2022", formatter);

        CovidReport regionalReportA = CovidReport.getRegionalCovidReport("Portugal", endDate, 23864, 472, 903, 23);
        CovidReport regionalReportB = CovidReport.getRegionalCovidReport("Portugal", LocalDate.parse("25/04/2022", formatter), 23392, 595, 880, 26);
        CovidReport regionalReportC = CovidReport.getRegionalCovidReport("Portugal", startDate, 22797, 444, 854, 34);


        List<CovidReport> reportList = Arrays.asList(regionalReportC, regionalReportB, regionalReportA);
        String serializedList = "[{\"region\":\"Portugal\",\"date\":\"2022-04-24\",\"totalCases\":22797,\"newCases\":444,\"totalDeaths\":854,\"newDeaths\":34},{\"region\":\"Portugal\",\"date\":\"2022-04-25\",\"totalCases\":23392,\"newCases\":595,\"totalDeaths\":880,\"newDeaths\":26},{\"region\":\"Portugal\",\"date\":\"2022-04-26\",\"totalCases\":23864,\"newCases\":472,\"totalDeaths\":903,\"newDeaths\":23}]";

        when(service
                .getCovidRegionalReportsFromDateToDate("Portugal", startDate, endDate))
                .thenReturn(reportList);

        when(mapper.writeValueAsString(reportList)).thenReturn(serializedList);

        assertThat(restController.getReportsForRangeOfDates(Optional.of("Portugal"), startDate, endDate), equalTo(ResponseEntity.ok(serializedList)));
    }

    @Test
    void testMultipleGlobalReportsIsSuccessful_thenReturnListOfCovidReports() throws JsonProcessingException {
        LocalDate startDate = LocalDate.parse("24/04/2022", formatter);
        LocalDate endDate = LocalDate.parse("26/04/2022", formatter);

        CovidReport globalReportA = CovidReport.getGlobalCovidReport(endDate, 23864, 472, 903, 23);
        CovidReport globalReportB = CovidReport.getGlobalCovidReport(LocalDate.parse("25/04/2022", formatter), 23392, 595, 880, 26);
        CovidReport globalReportC = CovidReport.getGlobalCovidReport(startDate, 22797, 444, 854, 34);

        List<CovidReport> reportList = Arrays.asList(globalReportC, globalReportB, globalReportA);
        String serializedList = "[{\"region\":\"Global\",\"date\":\"2022-04-24\",\"totalCases\":22797,\"newCases\":444,\"totalDeaths\":854,\"newDeaths\":34},{\"region\":\"Global\",\"date\":\"2022-04-25\",\"totalCases\":23392,\"newCases\":595,\"totalDeaths\":880,\"newDeaths\":26},{\"region\":\"Global\",\"date\":\"2022-04-26\",\"totalCases\":23864,\"newCases\":472,\"totalDeaths\":903,\"newDeaths\":23}]";

        when(service
                .getCovidGlobalReportsFromDateToDate(startDate, endDate))
                .thenReturn(Arrays.asList(globalReportC, globalReportB, globalReportA));

        when(mapper.writeValueAsString(reportList)).thenReturn(serializedList);

        assertThat(restController.getReportsForRangeOfDates(Optional.empty(), startDate, endDate), equalTo(ResponseEntity.ok(serializedList)));
    }

    @Test
    void testGetSingleRegionalReportIsUnsuccessful_thenReturnNoContentResponse() {
        LocalDate date = LocalDate.parse("26/04/2022", formatter);

        when(service.getReport(Key.getRegionalKey("Portugal", date))).thenReturn(Optional.empty());

        assertThat(restController.getReportForDate(Optional.of("Portugal"), date),
                equalTo(ResponseEntity.noContent().build()));
    }

    @Test
    void testGetSingleGlobalReportIsUnsuccessful_thenReturnNoContentResponse() {
        LocalDate date = LocalDate.parse("26/04/2022", formatter);

        when(service.getGlobalReportForDate(date)).thenReturn(Optional.empty());

        assertThat(restController.getReportForDate(Optional.empty(), date),
                equalTo(ResponseEntity.noContent().build()));
    }

    @Test
    void testGetSingleReportThrowingJsonProcessingException_returnsBadRequestResponse() throws JsonProcessingException {
        LocalDate date = LocalDate.parse("26/04/2022", formatter);

        CovidReport report = CovidReport.getGlobalCovidReport(
                date, 2971475, 74729, 206544, 3698);

        when(service.getReport(Key.getGlobalKey(date))).thenReturn(Optional.of(report));
        when(mapper.writeValueAsString(report)).thenThrow(JsonProcessingException.class);

        assertThat(restController.getReportForDate(Optional.empty(), date), equalTo(ResponseEntity.badRequest().build()));
    }

    @Test
    void testGetMultipleReportsThrowingJsonProcessingException_returnsBadRequestResponse() throws JsonProcessingException {
        LocalDate startDate = LocalDate.parse("24/04/2022", formatter);
        LocalDate endDate = LocalDate.parse("26/04/2022", formatter);

        CovidReport globalReportA = CovidReport.getGlobalCovidReport(endDate, 23864, 472, 903, 23);
        CovidReport globalReportB = CovidReport.getGlobalCovidReport(LocalDate.parse("25/04/2022", formatter), 23392, 595, 880, 26);
        CovidReport globalReportC = CovidReport.getGlobalCovidReport(startDate, 22797, 444, 854, 34);

        List<CovidReport> reportList = Arrays.asList(globalReportC, globalReportB, globalReportA);

        when(service
                .getCovidGlobalReportsFromDateToDate(startDate, endDate))
                .thenReturn(Arrays.asList(globalReportC, globalReportB, globalReportA));

        when(mapper.writeValueAsString(reportList)).thenThrow(JsonProcessingException.class);

        assertThat(restController.getReportsForRangeOfDates(Optional.empty(), startDate, endDate), equalTo(ResponseEntity.badRequest().build()));
    }

    @Test
    void testGetAllRegions_ReturnsListOfStrings() throws JsonProcessingException {
        List<String> regions = Arrays.asList("Portugal", "Italy", "France");
        String serializedRegions = "[Portugal, Italy, France]";

        when(service.getRegionsList()).thenReturn(regions);
        when(mapper.writeValueAsString(regions)).thenReturn(serializedRegions);

        assertThat(restController.getAllRegions(), equalTo(ResponseEntity.ok(serializedRegions)));
    }

    @Test
    void testGetAllRegionsThrowingJsonProcessingException_ReturnsBadRequestResponse() throws JsonProcessingException {
        List<String> regions = Arrays.asList("Portugal", "Italy", "France");

        when(service.getRegionsList()).thenReturn(regions);
        when(mapper.writeValueAsString(regions)).thenThrow(JsonProcessingException.class);

        assertThat(restController.getAllRegions(), equalTo(ResponseEntity.badRequest().build()));
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