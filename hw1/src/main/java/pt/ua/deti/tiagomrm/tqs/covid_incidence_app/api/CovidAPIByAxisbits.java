package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.api;

import org.springframework.stereotype.Component;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.data.CovidAPIInterface;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.data.CovidReport;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class CovidAPIByAxisbits implements CovidAPIInterface {
    @Override
    public Optional<CovidReport> getDataForCountryOnDate(String string, Date date) {
        return Optional.empty();
    }

    @Override
    public Optional<CovidReport> getGlobalDataForDate(Date date) {
        return Optional.empty();
    }

    @Override
    public List<String> getAllRegions() {
        return null;
    }


}
