package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.api.CovidAPIByAPISPORTS;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.api.CovidAPIByAxisbits;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.cache.CovidDataCacheManager;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.data.CovidAPIInterface;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.data.CovidReport;

import java.text.ParseException;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pt.ua.deti.tiagomrm.tqs.covid_incidence_app.Hw1ApplicationTests.parseDate;

@ExtendWith(MockitoExtension.class)
class CovidDataServiceTest {

    @InjectMocks
    private CovidDataService service = new CovidDataService();

    @Mock
    private CovidDataCacheManager mockedCacheManager;

    @Spy
    private LinkedList<CovidAPIInterface> mockedAPIs;

    @Mock
    private CovidAPIByAPISPORTS mockedAPIA;
    @Mock
    private CovidAPIByAxisbits mockedAPIB;


    private static Date date;
    private static String country;
    private static CovidReport countryReport;
    private static CovidReport globalReport;


    @BeforeAll
    static void setup() throws ParseException {
        country = "Portugal";
        date = parseDate("2022/04/26");

        countryReport = new CovidReport(country, parseDate("2022/04/26"), 23864, 472, 903, 23);
        globalReport = new CovidReport("Global", parseDate("2022/04/26"), 2971475, 74729, 206544, 3698);
    }

    @BeforeEach
    void init() {
        this.mockedAPIs.add(mockedAPIA);
        this.mockedAPIs.add(mockedAPIB);
    }

    @AfterEach
    void tearDown(){
        reset(mockedAPIA, mockedAPIB, mockedCacheManager);
    }

    @Test
    void testUpdateRegionsListFromPrimaryAPI() {
        List<String> countries = Arrays.asList("Portugal", "Italy");

        when(mockedAPIA.getAllRegions()).thenReturn(countries);

        service.updateRegionsListFromAPI();
        assertThat(service.getRegionsList(), equalTo(countries));

        verifyNoInteractions(mockedAPIA);
    }

    @Test
    void testUpdateRegionsListFromSecondaryAPI() {
        List<String> countries = Arrays.asList("Portugal", "Italy");

        when(mockedAPIB.getAllRegions()).thenReturn(countries);

        service.updateRegionsListFromAPI();
        assertThat(service.getRegionsList(), equalTo(countries));

        verify(mockedAPIA.getAllRegions(), times(1));
        verify(mockedAPIB.getAllRegions(), times(1));
    }

    @Test
    void testCacheHasInformation_thenReturnThatReport() {
        when(mockedCacheManager.getGlobalReportForDate(date)).thenReturn(Optional.ofNullable(globalReport));
        when(mockedCacheManager.getReportForCountryOnDate(country, date)).thenReturn(Optional.ofNullable(countryReport));

        assertServiceGetsRightGlobalAndRegionalReports();

        verifyNoInteractions(mockedAPIA);
        verifyNoInteractions(mockedAPIB);
    }

    @Test
    void testReportNotCachedButInFirstAPISource_thenReturnThatReport() {

        when(mockedAPIA.getDataForCountryOnDate(country, date)).thenReturn(Optional.of(countryReport));
        when(mockedAPIA.getGlobalDataForDate(date)).thenReturn(Optional.of(globalReport));

        assertServiceGetsRightGlobalAndRegionalReports();


        verify(mockedCacheManager, times(1)).getGlobalReportForDate(any());
        verify(mockedCacheManager, times(1)).getReportForCountryOnDate(any(), any());

        verify(mockedAPIA, times(1)).getGlobalDataForDate(any());
        verify(mockedAPIA, times(1)).getDataForCountryOnDate(any(), any());

        verifyNoInteractions(mockedAPIB);
    }

    @Test
    void testReportNotCachedAndNotInFirstAPISourceButInSecondAPISource_thenReturnThatReportAndMakePrimarySource() {

        when(mockedAPIB.getDataForCountryOnDate(country, date)).thenReturn(Optional.of(countryReport));
        when(mockedAPIB.getGlobalDataForDate(date)).thenReturn(Optional.of(globalReport));

        assertServiceGetsRightGlobalAndRegionalReports();


        verify(mockedCacheManager, times(1)).getGlobalReportForDate(any());
        verify(mockedCacheManager, times(1)).getReportForCountryOnDate(any(), any());

        verify(mockedAPIA, times(1)).getDataForCountryOnDate(any(), any());
        verifyNoMoreInteractions(mockedAPIA);

        verify(mockedAPIB, times(1)).getGlobalDataForDate(any());
        verify(mockedAPIB, times(1)).getDataForCountryOnDate(any(), any());
    }

    @Test
    void testDataFromExternalAPIShouldBeSavedInCache_thenReturnThatReport() {
        when(mockedCacheManager.saveToCache(any())).thenReturn(true);

        assertServiceGetsRightGlobalAndRegionalReports();

        verify(mockedCacheManager.saveToCache(any()), times(2));
    }

    @Test
    void testDataIsNotFound_thenReturnEmptyOptional() {

        Optional<CovidReport> sourcedGlobalReport = service.getGlobalReportForDate(date);
        Optional<CovidReport> sourcedRegionalReport = service.getReportForCountryOnDate(country, date);

        assertAll(() -> {
            assertTrue(sourcedGlobalReport.isEmpty());
            assertTrue(sourcedRegionalReport.isEmpty());
        });

        verify(mockedCacheManager, times(1)).getGlobalReportForDate(any());
        verify(mockedCacheManager, times(1)).getReportForCountryOnDate(any(), any());

        verify(mockedAPIA, times(1)).getGlobalDataForDate(any());
        verify(mockedAPIA, times(1)).getDataForCountryOnDate(any(), any());

        verify(mockedAPIB, times(1)).getGlobalDataForDate(any());
        verify(mockedAPIB, times(1)).getDataForCountryOnDate(any(), any());
    }

    private void assertServiceGetsRightGlobalAndRegionalReports() {
        Optional<CovidReport> sourcedGlobalReport = service.getGlobalReportForDate(date);
        Optional<CovidReport> sourcedRegionalReport = service.getReportForCountryOnDate(country, date);

        assertAll(() -> {
            assertTrue(sourcedGlobalReport.isPresent());
            assertTrue(sourcedRegionalReport.isPresent());

            assertThat(sourcedGlobalReport.get(), equalTo(globalReport));
            assertThat(sourcedRegionalReport.get(), equalTo(countryReport));
        });

    }

}