package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.api;

import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.data.CovidReport;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.service.Key;

import java.util.List;
import java.util.Optional;

public interface CovidAPIInterface {
    Optional<CovidReport> getReport(Key key);

    List<String> getAllRegions();
}
