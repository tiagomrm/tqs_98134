package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.data;

import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.service.Key;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CovidAPIInterface {
    Optional<CovidReport> getReport(Key key);

    List<String> getAllRegions();
}
