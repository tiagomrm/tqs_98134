package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.cache.CovidDataCacheManager;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.data.CovidAPIInterface;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.data.CovidReport;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class CovidDataService {

    @Autowired
    private CovidDataCacheManager cacheManager;
    @Autowired
    private LinkedList<CovidAPIInterface> sources;

    private List<String> regionsList;

    public Optional<CovidReport> getGlobalReportForDate(Date date) {
        return Optional.empty();
    }

    public Optional<CovidReport> getReportForCountryOnDate(String country, Date date) {
        return Optional.empty();
    }

    public LinkedList<CovidAPIInterface> getSources() {
        return sources;
    }

    @PostConstruct
    public boolean updateRegionsListFromAPI() {
        return false;
    }

    public List<String> getRegionsList() {
        return regionsList;
    }

}
