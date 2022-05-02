package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.data.CovidReport;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.service.CovidDataService;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.service.Key;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class CovidDataRestController {

    private final Logger logger = LogManager.getLogger();

    @Autowired
    private CovidDataService service;

    private ObjectMapper mapper;

    @Autowired
    CovidDataRestController(){
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @GetMapping(value="/api/report", produces=MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getReportForDate(
            @RequestParam Optional<String> region,
            @RequestParam(value="date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        Optional<CovidReport> optionalReport =
                service.getReport(region.map(s -> Key.getRegionalKey(s, date)).orElseGet(() -> Key.getGlobalKey(date)));

        if (optionalReport.isEmpty())
            return ResponseEntity.noContent().build();

        String body;

        try {
            body = mapper.writeValueAsString( optionalReport.get() );
        } catch (JsonProcessingException e) {
            logger.error(e);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(body);
    }

    @GetMapping(value="/api/reports", produces=MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getReportsForRangeOfDates(
            @RequestParam Optional<String> region,
            @RequestParam(value="startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value="endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        String body;

        try {
            body = mapper.writeValueAsString(region.isPresent() ?
                    service.getCovidRegionalReportsFromDateToDate(region.get(), startDate, endDate) :
                    service.getCovidGlobalReportsFromDateToDate(startDate, endDate));
        } catch (JsonProcessingException e) {
            logger.error(e);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(body);
    }

    @GetMapping(value="/api/regions", produces=MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getAllRegions() {
        List<String> regionsList = service.getRegionsList();
        if (!regionsList.isEmpty())
            try {
                return ResponseEntity.ok().body(mapper.writeValueAsString(regionsList));
            } catch (JsonProcessingException e) {
                logger.error(e);
                return ResponseEntity.badRequest().build();
            }
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value="/api/cache", produces=MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getCacheReport() {
        try {
            return ResponseEntity.ok().body(service.getCacheReportJSON());
        } catch (JsonProcessingException e) {
            logger.error(e);
            return ResponseEntity.badRequest().build();
        }
    }
}
