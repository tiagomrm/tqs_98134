package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.api;

import org.springframework.stereotype.Component;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.data.CovidAPIInterface;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.data.CovidReport;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.service.Key;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class CovidAPIByAPISPORTS implements CovidAPIInterface {

    @Override
    public Optional<CovidReport> getReport(Key key) {
        return Optional.empty();
    }

    @Override
    public List<String> getAllRegions() {
        return null;
    }
}
