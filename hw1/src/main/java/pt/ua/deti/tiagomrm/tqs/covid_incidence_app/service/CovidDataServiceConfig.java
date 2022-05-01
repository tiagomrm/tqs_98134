package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.api.CovidAPIByAPISPORTS;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.api.CovidAPIByAxisbits;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.cache.CovidDataCacheManager;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.api.CovidAPIInterface;

import java.util.Arrays;
import java.util.LinkedList;

@Configuration
public class CovidDataServiceConfig {

    @Bean
    public CovidDataService getService() {
        return new CovidDataService();
    }

    @Bean
    public LinkedList<CovidAPIInterface> sources() {
        return new LinkedList<>(Arrays.asList(new CovidAPIByAPISPORTS(), new CovidAPIByAxisbits()));
    }

    @Bean
    public CovidDataCacheManager cacheManager() {
        return new CovidDataCacheManager();
    }
}
