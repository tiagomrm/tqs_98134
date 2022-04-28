package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.data;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CovidAPIInterface {
    Optional<CovidReport> getDataForCountryOnDate(String string, Date date);
    Optional<CovidReport> getGlobalDataForDate(Date date);

    List<String> getAllRegions();
}
