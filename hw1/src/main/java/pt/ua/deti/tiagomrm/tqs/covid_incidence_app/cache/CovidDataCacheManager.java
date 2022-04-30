package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.cache;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.data.CovidReport;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.service.Key;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CovidDataCacheManager {

    public static final int TIME_TO_LIVE = 10;
    LocalDateTime dateOfInitialization;

    Map<Key, CovidDataCacheEntry> cache;

    private final Logger logger = LogManager.getLogger();

    private int hits;
    private int misses;

    private int calls;

    public CovidDataCacheManager() {
        this.dateOfInitialization = LocalDateTime.now();
        cache = new ConcurrentHashMap<>();
        hits = 0;
        misses = 0;
        calls = 0;
    }

    public Optional<CovidReport> getCachedCovidReport(Key key) {
        logger.info("Getting {} report for date {}", key.getRegion(), key.getDate());
        calls++;

        CovidDataCacheEntry entry = cache.get(key);

        CovidReport report = null;

        if(entry != null) {
            logger.info("Found cached entry for report");
            report = entry.getReport();
            hits++;
        } else {
            logger.debug("No report found by cache manager [{} entries in cache]", cache.size());
            misses++;
        }

        updateCacheTTL();

        return Optional.ofNullable(report);
    }

    public void updateCacheTTL() {
        logger.info("Starting update of cache entries' TTL");
        for (Map.Entry<Key, CovidDataCacheEntry> cachedEntry: cache.entrySet()) {
            if (cachedEntry.getValue().isDyingAfterCall())
                cache.remove(cachedEntry.getKey());
            else
                cachedEntry.getValue().decrementTTL();
        }
        logger.info("Finished TTL update");
    }

    public boolean saveToCache(Key key, CovidReport report) {
        logger.info("Saving report to cache... ");

        CovidDataCacheEntry entry = new CovidDataCacheEntry(report);

        cache.put(key, entry);
        logger.info("Finished saving to cache ");

        return true;
    }

    public boolean saveToCache(CovidReport report) {
        return saveToCache(Key.getRegionalKey(report.getRegion(), report.getDate()), report);
    }

    public void emptyCache() {
        cache = new ConcurrentHashMap<>();
    }

    public int getHits() {
        return hits;
    }

    public int getMisses() {
        return misses;
    }

    public int getCalls() {
        return calls;
    }

    @Override
    public String toString() {
        return String.format("CovidDataCacheManager [%d entries]", cache.size());
    }

    static class CovidDataCacheEntry {

        private final CovidReport report;
        private int ttl;

        public CovidDataCacheEntry(CovidReport report) {
            this.report = report;
            this.ttl = TIME_TO_LIVE;
        }

        public CovidReport getReport() {
            return report;
        }

        public boolean isDyingAfterCall() {
            return ttl <= 1;
        }

        public void decrementTTL() {
            this.ttl--;
        }

        public void resetTTL() {
            this.ttl = TIME_TO_LIVE;
        }
    }
}
