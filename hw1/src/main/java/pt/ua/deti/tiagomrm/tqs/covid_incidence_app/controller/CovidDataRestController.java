package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.data.CovidReport;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.service.CovidDataService;

import java.util.Date;

@RestController
public class CovidDataRestController {

    @Autowired
    private CovidDataService service;

    @GetMapping("/api/{date}")
    CovidReport getGlobalReportForDate(@PathVariable Date date) {
        return null;
    }

    @GetMapping("/api/{country}/{date}")
    CovidReport getCountryReportForDate(@PathVariable String country, @PathVariable Date date) {
        return null;
    }

}
