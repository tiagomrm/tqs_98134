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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CovidDataServiceTest {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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


    private static LocalDate date;
    private static String country;
    private static CovidReport regionalReportA;
    private static CovidReport regionalReportB;
    private static CovidReport regionalReportC;
    private static CovidReport globalReportA;
    private static CovidReport globalReportB;
    private static CovidReport globalReportC;


    @BeforeAll
    static void setup() {
        country = "Portugal";
        date = LocalDate.parse("26/04/2022", formatter);

        regionalReportA = CovidReport.getRegionalCovidReport("Portugal", date , 23864, 472, 903, 23);
        regionalReportB = CovidReport.getRegionalCovidReport("Portugal", LocalDate.parse("25/04/2022", formatter), 23392, 595, 880, 26);
        regionalReportC = CovidReport.getRegionalCovidReport("Portugal", LocalDate.parse("24/04/2022", formatter), 22797, 444, 854, 34);
        globalReportA = CovidReport.getGlobalCovidReport(date , 2971475, 74729, 206544, 3698);
        globalReportB = CovidReport.getGlobalCovidReport(LocalDate.parse("25/04/2022", formatter), 2896746, 85553, 202846, 5687);
        globalReportC = CovidReport.getGlobalCovidReport(LocalDate.parse("24/04/2022", formatter), 2811193, 103135, 197159, 6317);
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
    void testGetRegionalReportForMultipleDatesIsSuccessful_thenReturnListOfReports() {
        lenient().when(mockedCacheManager.getCachedCovidReport(Key.getRegionalKey("Portugal", date))).thenReturn(Optional.ofNullable(regionalReportA));
        lenient().when(mockedAPIA.getReport(Key.getRegionalKey("Portugal", LocalDate.parse("25/04/2022", formatter)))).thenReturn(Optional.ofNullable(regionalReportB));
        lenient().when(mockedAPIB.getReport(Key.getRegionalKey("Portugal", LocalDate.parse("24/04/2022", formatter)))).thenReturn(Optional.ofNullable(regionalReportC));

        assertThat(
                service.getCovidRegionalReportsFromDateToDate("Portugal", LocalDate.parse("24/04/2022", formatter), date),
                equalTo(Arrays.asList(regionalReportC, regionalReportB, regionalReportA))
        );
    }

    @Test
    void testGetRegionalReportForMultipleDatesIsUnsuccessful_thenReturnEmptyList() {
        assertThat(service.getCovidRegionalReportsFromDateToDate("Portugal", LocalDate.parse("24/04/2022", formatter), date), empty());
    }

    @Test
    void testGetGlobalReportForMultipleDatesIsSuccessful_thenReturnListOfReports() {
        lenient().when(mockedCacheManager.getCachedCovidReport(Key.getGlobalKey(date))).thenReturn(Optional.ofNullable(globalReportA));
        lenient().when(mockedAPIA.getReport(Key.getGlobalKey(LocalDate.parse("25/04/2022", formatter)))).thenReturn(Optional.ofNullable(globalReportB));
        lenient().when(mockedAPIB.getReport(Key.getGlobalKey(LocalDate.parse("24/04/2022", formatter)))).thenReturn(Optional.ofNullable(globalReportC));

        assertThat(
                service.getCovidGlobalReportsFromDateToDate(LocalDate.parse("24/04/2022", formatter), date),
                equalTo(Arrays.asList(globalReportC, globalReportB, globalReportA))
        );
    }

    @Test
    void testGetGlobalReportForMultipleDatesIsUnsuccessful_thenReturnEmptyList() {
        assertThat(service.getCovidGlobalReportsFromDateToDate(LocalDate.parse("24/04/2022", formatter), date), empty());
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