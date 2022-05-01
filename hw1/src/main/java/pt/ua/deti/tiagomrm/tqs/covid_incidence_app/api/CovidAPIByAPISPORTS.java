package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.data.CovidAPIInterface;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.data.CovidReport;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.service.Key;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class CovidAPIByAPISPORTS implements CovidAPIInterface {

    BasicHttpClient httpClient;

    @Autowired
    public CovidAPIByAPISPORTS() {
        httpClient = new BasicHttpClient("covid-19-statistics.p.rapidapi.com", "f4e5f56284msh79ce055a0bad368p1be5d8jsncc4511b3c4eb");
    }

    @Override
    public Optional<CovidReport> getReport(Key key) {
        return Optional.empty();
    }

    @Override
    public List<String> getAllRegions() {
        httpClient.getHttpResponse("https://covid-193.p.rapidapi.com/countries").getStatus();
        return Collections.emptyList();
    }
}
