package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private final Logger logger = LogManager.getLogger();

    private Optional<CovidReport> getReportFromExternalAPIs(Key key){
        logger.info("Searching for data in external sources");
        logger.debug("Checking " + sources.size() + "sources");

        Optional<CovidReport> foundReport;

        for (int i = 0; i < sources.size(); i++) {
            CovidAPIInterface source = sources.get(i);
            foundReport = source.getReport(key);

            if(foundReport.isEmpty())
                continue;

            cacheManager.saveToCache(key, foundReport.get());

            if (i > 0) {
                CovidAPIInterface newPrimarySource = sources.remove(i);
                sources.addFirst(newPrimarySource);
            }

            logger.info("Search completed successfully");
            return foundReport;
        }

        logger.info("Search completed unsuccessfully");
        return Optional.empty();
    }

    public Optional<CovidReport> getGlobalReportForDate(Date date) {
        return getReport(Key.getGlobalKey(date));
    }

    public Optional<CovidReport> getReportForCountryOnDate(String region, Date date) {
        return getReport(Key.getRegionalKey(region, date));
    }

    public Optional<CovidReport> getReport(Key key) {
        logger.info("Getting " + key.getRegion() +" report for date " + key.getDate() + "...");

        Optional<CovidReport> optionalCachedReport = cacheManager.getCachedCovidReport(key);

        if (optionalCachedReport.isPresent()) {
            logger.info("Report found in cache");
            return optionalCachedReport;
        }

        return getReportFromExternalAPIs(key);
    }

    public LinkedList<CovidAPIInterface> getSources() {
        logger.info("Getting sources from service");
        return sources;
    }

    @PostConstruct
    public boolean updateRegionsListFromAPI() {
        logger.info("Updating service's regions list from APIs");

        for (int i = 0; i < sources.size(); i++) {
            CovidAPIInterface source = sources.get(i);
            List<String> regions = source.getAllRegions();

            if(regions == null || regions.isEmpty())
                continue;

            if (i > 0) {
                CovidAPIInterface newPrimarySource = sources.remove(i);
                sources.addFirst(newPrimarySource);

            }

            this.regionsList = regions;
            logger.info("Regions list updated successfully");
            return true;
        }
        logger.info("Failed update of regions list");
        return false;
    }

    public List<String> getRegionsList() {
        return regionsList;
    }

}
