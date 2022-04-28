package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.cache;

import org.springframework.stereotype.Component;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.data.CovidReport;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class CovidDataCacheManager {

    public static final int TIME_TO_LIVE = 10;
    LocalDateTime dateOfInitialization;

    Map<Key, CovidDataCacheEntry> cache;

    private int hits;
    private int misses;
    private int calls;

    public CovidDataCacheManager() {
        this.dateOfInitialization = LocalDateTime.now();
        cache = new LinkedHashMap<>();
        hits = 0;
        misses = 0;
        calls = 0;
    }

    public Optional<CovidReport> getGlobalReportForDate(Date date) {return Optional.empty();}

    public Optional<CovidReport> getReportForCountryOnDate(String country, Date date) {return Optional.empty();}

    public boolean saveToCache(CovidReport report) {
        return false;
    }

    public void emptyCache() {
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

    static class CovidDataCacheEntry {

        private final CovidReport report;
        private int ttl;

        public CovidDataCacheEntry(CovidReport report) {
            this.report = report;
            this.ttl = TIME_TO_LIVE;
        }

        public CovidReport getReport() {
            return null;
        }

        public boolean hasReachedEndOfTTL() {
            return false;
        }

        public void decrementTTL() {
            this.ttl--;
        }

        public void resetTTL() {}
    }

    static class Key {
        private final String country;
        private final Date date;

        Key(String country, Date date) {
            this.country = country;
            this.date = date;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return country.equals(key.country) && date.equals(key.date);
        }

        @Override
        public int hashCode() {
            return Objects.hash(country, date);
        }
    }
}
