package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.cache;

import org.junit.jupiter.api.*;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.data.CovidReport;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.service.Key;

import java.text.ParseException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static pt.ua.deti.tiagomrm.tqs.covid_incidence_app.Hw1ApplicationTests.parseDate;

class CovidDataCacheManagerTest {

    private CovidDataCacheManager cacheManager;

    private static CovidReport countryReportA;
    private static CovidReport countryReportB;
    private static CovidReport countryReportC;
    private static CovidReport globalReportA;
    private static CovidReport globalReportB;
    private static CovidReport globalReportC;

    @BeforeAll
    static void setup() throws ParseException {
        countryReportA = CovidReport.getRegionalCovidReport("Portugal", parseDate("2022/04/26"), 23864, 472, 903, 23);
        countryReportB = CovidReport.getRegionalCovidReport("Portugal", parseDate("2022/04/25"), 23392, 595, 880, 26);
        countryReportC = CovidReport.getRegionalCovidReport("Portugal", parseDate("2022/04/24"), 22797, 444, 854, 34);
        globalReportA = CovidReport.getGlobalCovidReport(parseDate("2022/04/26"), 2971475, 74729, 206544, 3698);
        globalReportB = CovidReport.getGlobalCovidReport(parseDate("2022/04/25"), 2896746, 85553, 202846, 5687);
        globalReportC = CovidReport.getGlobalCovidReport(parseDate("2022/04/24"), 2811193, 103135, 197159, 6317);
    }


    @BeforeEach
    void init() {
        cacheManager = new CovidDataCacheManager();

        cacheManager.saveToCache(countryReportA);
        cacheManager.saveToCache(countryReportB);
        cacheManager.saveToCache(countryReportC);
        cacheManager.saveToCache(globalReportA);
        cacheManager.saveToCache(globalReportB);
        cacheManager.saveToCache(globalReportC);
    }

    @AfterEach
    void tearDown() {
        cacheManager.emptyCache();
        cacheManager = null;
    }

    @Test
    void testSuccessfulQueryForGlobal() throws ParseException {
        Optional<CovidReport> cachedReport = cacheManager.getCachedCovidReport(Key.getGlobalKey(parseDate("2022/04/26")));
        assertTrue(cachedReport.isPresent());
        assertThat(cachedReport.get(), equalTo(globalReportA));
    }

    @Test
    void testIncrementHits() throws ParseException {
        assertThat(cacheManager.getHits(), equalTo(0));
        Optional<CovidReport> cachedReport = cacheManager.getCachedCovidReport(Key.getGlobalKey(parseDate("2022/04/26")));
        assertTrue(cachedReport.isPresent());
        assertThat(cachedReport.get(), equalTo(globalReportA));
        assertThat(cacheManager.getHits(), equalTo(1));
    }

    @Test
    void testUnsuccessfulQueryForGlobal() throws ParseException {
        Optional<CovidReport> cachedReport = cacheManager.getCachedCovidReport(Key.getGlobalKey(parseDate("2022/04/27")));
        assertTrue(cachedReport.isEmpty());
    }

    @Test
    void testIncrementMisses() throws ParseException {
        assertThat(cacheManager.getHits(), equalTo(0));
        Optional<CovidReport> cachedReport = cacheManager.getCachedCovidReport(Key.getGlobalKey(parseDate("2022/04/27")));
        assertTrue(cachedReport.isEmpty());
        assertThat(cacheManager.getMisses(), equalTo(1));
    }

    @Test
    void testIncrementCalls() throws ParseException {
        assertThat(cacheManager.getCalls(), equalTo(0));
        cacheManager.getCachedCovidReport(Key.getGlobalKey(parseDate("2022/04/27")));
        cacheManager.getCachedCovidReport(Key.getRegionalKey("Portugal", parseDate("2022/04/26")));
        assertThat(cacheManager.getCalls(), equalTo(2));
    }

    @Test
    void testSuccessfulQueryForCountry() throws ParseException {
        Optional<CovidReport> cachedReport = cacheManager.getCachedCovidReport(Key.getRegionalKey("Portugal", parseDate("2022/04/26")));
        assertTrue(cachedReport.isPresent());
        assertThat(cachedReport.get(), equalTo(countryReportA));
    }

    @Test
    void testUnsuccessfulQueryForCountry() throws ParseException {
        Optional<CovidReport> cachedReport = cacheManager.getCachedCovidReport(Key.getRegionalKey("Portugal", parseDate("2022/04/27")));
        assertTrue(cachedReport.isEmpty());
    }

    @Test
    void testCacheIsRemovedWhenReachesEndOfTTL() throws ParseException {
        for (int i = 0; i < CovidDataCacheManager.TIME_TO_LIVE; i++) {
            cacheManager.getCachedCovidReport(Key.getGlobalKey(parseDate("2022/04/27")));
        }

        Optional<CovidReport> globalReport = cacheManager.getCachedCovidReport(Key.getGlobalKey(parseDate("2022/04/26")));
        Optional<CovidReport> countryReport = cacheManager.getCachedCovidReport(Key.getRegionalKey("Portugal", parseDate("2022/04/26")));

        assertAll( () -> {
            assertTrue(globalReport.isEmpty());
            assertTrue(countryReport.isEmpty());
        });
    }

}