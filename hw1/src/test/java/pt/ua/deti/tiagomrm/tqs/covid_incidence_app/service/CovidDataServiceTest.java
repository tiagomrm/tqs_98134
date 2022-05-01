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
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.api.CovidAPIInterface;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.data.CovidReport;

import java.text.ParseException;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
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
    private static CovidReport regionalReportA;
    private static CovidReport regionalReportB;
    private static CovidReport regionalReportC;
    private static CovidReport globalReportA;
    private static CovidReport globalReportB;
    private static CovidReport globalReportC;


    @BeforeAll
    static void setup() throws ParseException {
        country = "Portugal";
        date = parseDate("2022/04/26");

        regionalReportA = CovidReport.getRegionalCovidReport("Portugal", date , 23864, 472, 903, 23);
        regionalReportB = CovidReport.getRegionalCovidReport("Portugal", parseDate("2022/04/25"), 23392, 595, 880, 26);
        regionalReportC = CovidReport.getRegionalCovidReport("Portugal", parseDate("2022/04/24"), 22797, 444, 854, 34);
        globalReportA = CovidReport.getGlobalCovidReport(date , 2971475, 74729, 206544, 3698);
        globalReportB = CovidReport.getGlobalCovidReport(parseDate("2022/04/25"), 2896746, 85553, 202846, 5687);
        globalReportC = CovidReport.getGlobalCovidReport(parseDate("2022/04/24"), 2811193, 103135, 197159, 6317);
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
    void testGetRegionalReportForMultipleDatesIsSuccessful_thenReturnListOfReports() throws ParseException {
        when(mockedCacheManager.getCachedCovidReport(Key.getRegionalKey("Portugal", date))).thenReturn(Optional.ofNullable(regionalReportA));
        when(mockedAPIA.getReport(Key.getRegionalKey("Portugal", parseDate("2022/04/25")))).thenReturn(Optional.ofNullable(regionalReportB));
        when(mockedAPIB.getReport(Key.getRegionalKey("Portugal", parseDate("2022/04/24")))).thenReturn(Optional.ofNullable(regionalReportC));

        assertThat(
                service.getCovidRegionalReportsFromDateToDate("Portugal", parseDate("2022/04/24"), date),
                equalTo(Arrays.asList(regionalReportA, regionalReportB, regionalReportC))
        );
    }

    @Test
    void testGetRegionalReportForMultipleDatesIsUnsuccessful_thenReturnEmptyList() throws ParseException {
        assertThat(service.getCovidRegionalReportsFromDateToDate("Portugal", parseDate("2022/04/24"), date), empty());
    }

    @Test
    void testGetGlobalReportForMultipleDatesIsSuccessful_thenReturnListOfReports() throws ParseException {
        when(mockedCacheManager.getCachedCovidReport(Key.getGlobalKey(date))).thenReturn(Optional.ofNullable(globalReportA));
        when(mockedAPIA.getReport(Key.getGlobalKey(parseDate("2022/04/25")))).thenReturn(Optional.ofNullable(globalReportB));
        when(mockedAPIB.getReport(Key.getGlobalKey(parseDate("2022/04/24")))).thenReturn(Optional.ofNullable(globalReportC));

        assertThat(
                service.getCovidGlobalReportsFromDateToDate(parseDate("2022/04/24"), date),
                equalTo(Arrays.asList(globalReportA, globalReportB, globalReportC))
        );
    }

    @Test
    void testGetGlobalReportForMultipleDatesIsUnsuccessful_thenReturnEmptyList() throws ParseException {
        assertThat(service.getCovidGlobalReportsFromDateToDate(parseDate("2022/04/24"), date), empty());
    }

    @Test
    void testUpdateRegionsListFromPrimaryAPI() {
        List<String> countries = Arrays.asList("Portugal", "Italy");

        when(mockedAPIA.getAllRegions()).thenReturn(countries);

        service.updateRegionsListFromAPI();
        assertThat(service.getRegionsList(), equalTo(countries));

        verifyNoInteractions(mockedAPIB);
    }

    @Test
    void testUpdateRegionsListFromSecondaryAPI() {
        List<String> countries = Arrays.asList("Portugal", "Italy");

        when(mockedAPIB.getAllRegions()).thenReturn(countries);

        service.updateRegionsListFromAPI();
        assertThat(service.getRegionsList(), equalTo(countries));

        verify(mockedAPIA, times(1)).getAllRegions();
        verify(mockedAPIB, times(1)).getAllRegions();
    }

    @Test
    void testCacheHasInformation_thenReturnThatReport() {
        when(mockedCacheManager.getCachedCovidReport(Key.getGlobalKey(date))).thenReturn(Optional.ofNullable(globalReportA));
        when(mockedCacheManager.getCachedCovidReport(Key.getRegionalKey(country, date))).thenReturn(Optional.ofNullable(regionalReportA));

        assertServiceGetsRightGlobalAndRegionalReports();

        verifyNoInteractions(mockedAPIA);
        verifyNoInteractions(mockedAPIB);
    }

    @Test
    void testReportNotCachedButInFirstAPISource_thenReturnThatReport() {

        when(mockedAPIA.getReport(Key.getRegionalKey(country, date))).thenReturn(Optional.of(regionalReportA));
        when(mockedAPIA.getReport(Key.getGlobalKey(date))).thenReturn(Optional.of(globalReportA));

        assertServiceGetsRightGlobalAndRegionalReports();


        verify(mockedCacheManager, times(2)).getCachedCovidReport(any());

        verify(mockedAPIA, times(2)).getReport(any());

        verifyNoInteractions(mockedAPIB);
    }

    @Test
    void testReportNotCachedAndNotInFirstAPISourceButInSecondAPISource_thenReturnThatReportAndMakePrimarySource() {

        when(mockedAPIB.getReport(Key.getRegionalKey(country, date))).thenReturn(Optional.of(regionalReportA));
        when(mockedAPIB.getReport(Key.getGlobalKey(date))).thenReturn(Optional.of(globalReportA));

        assertServiceGetsRightGlobalAndRegionalReports();


        verify(mockedCacheManager, times(2)).getCachedCovidReport(any());

        verify(mockedAPIA, times(1)).getReport(any());
        verifyNoMoreInteractions(mockedAPIA);

        verify(mockedAPIB, times(2)).getReport(any());
    }

    @Test
    void testDataFromExternalAPIShouldBeSavedInCache_thenReturnThatReport() {
        when(mockedAPIB.getReport(Key.getRegionalKey(country, date))).thenReturn(Optional.of(regionalReportA));
        when(mockedAPIB.getReport(Key.getGlobalKey(date))).thenReturn(Optional.of(globalReportA));
        when(mockedCacheManager.saveToCache(any(), any())).thenReturn(true);

        assertServiceGetsRightGlobalAndRegionalReports();

        verify(mockedCacheManager, times(2)).saveToCache(any(), any());
    }

    @Test
    void testDataIsNotFound_thenReturnEmptyOptional() {

        Optional<CovidReport> sourcedGlobalReport = service.getGlobalReportForDate(date);
        Optional<CovidReport> sourcedRegionalReport = service.getReportForCountryOnDate(country, date);

        assertAll(() -> {
            assertTrue(sourcedGlobalReport.isEmpty());
            assertTrue(sourcedRegionalReport.isEmpty());
        });

        verify(mockedCacheManager, times(2)).getCachedCovidReport(any());

        verify(mockedAPIA, times(2)).getReport(any());

        verify(mockedAPIB, times(2)).getReport(any());
    }

    private void assertServiceGetsRightGlobalAndRegionalReports() {
        Optional<CovidReport> sourcedGlobalReport = service.getGlobalReportForDate(date);
        Optional<CovidReport> sourcedRegionalReport = service.getReportForCountryOnDate(country, date);

        assertAll(() -> {
            assertTrue(sourcedGlobalReport.isPresent());
            assertTrue(sourcedRegionalReport.isPresent());

            assertThat(sourcedGlobalReport.get(), equalTo(globalReportA));
            assertThat(sourcedRegionalReport.get(), equalTo(regionalReportA));
        });

    }

}