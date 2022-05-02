package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.data.CovidReport;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.service.Key;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class CovidDataCacheManagerTest {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private CovidDataCacheManager cacheManager;

    private static CovidReport countryReportA;
    private static CovidReport countryReportB;
    private static CovidReport countryReportC;
    private static CovidReport globalReportA;
    private static CovidReport globalReportB;
    private static CovidReport globalReportC;

    @BeforeAll
    static void setup() {
        countryReportA = CovidReport.getRegionalCovidReport("Portugal", LocalDate.parse("26/04/2022", formatter), 23864, 472, 903, 23);
        countryReportB = CovidReport.getRegionalCovidReport("Portugal", LocalDate.parse("25/05/2022", formatter), 23392, 595, 880, 26);
        countryReportC = CovidReport.getRegionalCovidReport("Portugal", LocalDate.parse("24/04/2022", formatter), 22797, 444, 854, 34);
        globalReportA = CovidReport.getGlobalCovidReport(LocalDate.parse("26/04/2022", formatter), 2971475, 74729, 206544, 3698);
        globalReportB = CovidReport.getGlobalCovidReport(LocalDate.parse("25/05/2022", formatter), 2896746, 85553, 202846, 5687);
        globalReportC = CovidReport.getGlobalCovidReport(LocalDate.parse("24/04/2022", formatter), 2811193, 103135, 197159, 6317);
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
    void testSuccessfulQueryForGlobal() {
        Optional<CovidReport> cachedReport = cacheManager.getCachedCovidReport(Key.getGlobalKey(LocalDate.parse("26/04/2022", formatter)));
        assertTrue(cachedReport.isPresent());
        assertThat(cachedReport.get(), equalTo(globalReportA));
    }

    @Test
    void testIncrementHits() {
        assertThat(cacheManager.getHits(), equalTo(0));
        Optional<CovidReport> cachedReport = cacheManager.getCachedCovidReport(Key.getGlobalKey(LocalDate.parse("26/04/2022", formatter)));
        assertTrue(cachedReport.isPresent());
        assertThat(cachedReport.get(), equalTo(globalReportA));
        assertThat(cacheManager.getHits(), equalTo(1));
    }

    @Test
    void testUnsuccessfulQueryForGlobal() {
        Optional<CovidReport> cachedReport = cacheManager.getCachedCovidReport(Key.getGlobalKey(LocalDate.parse("27/04/2022", formatter)));
        assertTrue(cachedReport.isEmpty());
    }

    @Test
    void testIncrementMisses() {
        assertThat(cacheManager.getHits(), equalTo(0));
        Optional<CovidReport> cachedReport = cacheManager.getCachedCovidReport(Key.getGlobalKey(LocalDate.parse("27/04/2022", formatter)));
        assertTrue(cachedReport.isEmpty());
        assertThat(cacheManager.getMisses(), equalTo(1));
    }

    @Test
    void testIncrementCalls() {
        assertThat(cacheManager.getCalls(), equalTo(0));
        cacheManager.getCachedCovidReport(Key.getGlobalKey(LocalDate.parse("27/04/2022", formatter)));
        cacheManager.getCachedCovidReport(Key.getRegionalKey("Portugal", LocalDate.parse("26/04/2022", formatter)));
        assertThat(cacheManager.getCalls(), equalTo(2));
    }

    @Test
    void testSuccessfulQueryForCountry() {
        Optional<CovidReport> cachedReport = cacheManager.getCachedCovidReport(Key.getRegionalKey("Portugal", LocalDate.parse("26/04/2022", formatter)));
        assertTrue(cachedReport.isPresent());
        assertThat(cachedReport.get(), equalTo(countryReportA));
    }

    @Test
    void testUnsuccessfulQueryForCountry() {
        Optional<CovidReport> cachedReport = cacheManager.getCachedCovidReport(Key.getRegionalKey("Portugal", LocalDate.parse("27/04/2022", formatter)));
        assertTrue(cachedReport.isEmpty());
    }

    @Test
    void testCacheIsRemovedWhenReachesEndOfTTL() {
        for (int i = 0; i < CovidDataCacheManager.TIME_TO_LIVE; i++) {
            cacheManager.getCachedCovidReport(Key.getGlobalKey(LocalDate.parse("27/04/2022", formatter)));
        }

        Optional<CovidReport> globalReport = cacheManager.getCachedCovidReport(Key.getGlobalKey(LocalDate.parse("26/04/2022", formatter)));
        Optional<CovidReport> countryReport = cacheManager.getCachedCovidReport(Key.getRegionalKey("Portugal", LocalDate.parse("26/04/2022", formatter)));

        assertAll( () -> {
            assertTrue(globalReport.isEmpty());
            assertTrue(countryReport.isEmpty());
        });
    }

    @Test
    void testSerializedReport() throws JsonProcessingException {
        Optional<CovidReport> hitReport = cacheManager.getCachedCovidReport(Key.getGlobalKey(LocalDate.parse("26/04/2022", formatter)));
        Optional<CovidReport> missReport = cacheManager.getCachedCovidReport(Key.getGlobalKey(LocalDate.parse("27/04/2022", formatter)));

        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(cacheManager));

        assertThat(mapper.writeValueAsString(cacheManager),
                allOf(
                        containsString("\"calls\":2"),
                        containsString("\"hits\":1"),
                        containsString("\"misses\":1")));
    }

}