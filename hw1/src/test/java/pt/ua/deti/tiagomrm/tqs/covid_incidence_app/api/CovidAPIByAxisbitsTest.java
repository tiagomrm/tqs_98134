package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.api;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.data.CovidReport;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.service.Key;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static pt.ua.deti.tiagomrm.tqs.covid_incidence_app.Hw1ApplicationTests.parseDate;


@ExtendWith(MockitoExtension.class)
class CovidAPIByAxisbitsTest {

    @InjectMocks
    CovidAPIByAxisbits api = new CovidAPIByAxisbits();

    @Mock
    BasicHttpClient httpClient;

    @Mock
    HttpResponse<JsonNode> mockHttpResponse;
    JsonNode mockedRegionsJsonNode = new JsonNode(
            "{\"data\":[" +
                    "{\"iso\":\"CHN\"," +
                    "\"name\":\"China\"}," +
                    "{\"iso\":\"TWN\"," +
                    "\"name\":\"Taipei and environs\"}," +
                    "{\"iso\":\"USA\"," +
                    "\"name\":\"US\"}," +
                    "{\"iso\":\"JPN\"," +
                    "\"name\":\"Japan\"}]}"
    );

    JsonNode mockedRegionalReportJsonNode = new JsonNode(
            "{\"data\":[" +
                    "{\"date\":\"2022-04-30\"," +
                    "\"confirmed\":3853800," +
                    "\"deaths\":22280," +
                    "\"recovered\":0," +
                    "\"confirmed_diff\":0," +
                    "\"deaths_diff\":0," +
                    "\"recovered_diff\":0," +
                    "\"last_update\":\"2022-05-01 04:20:57\"," +
                    "\"active\":3831520," +
                    "\"active_diff\":0," +
                    "\"fatality_rate\":0.0058," +
                    "\"region\":{" +
                        "\"iso\":\"PRT\"," +
                        "\"name\":\"Portugal\"," +
                        "\"province\":\"\"," +
                        "\"lat\":\"39.3999\"," +
                        "\"long\":\"-8.2245\"," +
                        "\"cities\":[]" +
                    "}}]}"
    );

    JsonNode mockedGlobalReportJsonNode = new JsonNode(
            "{\"data\":" +
                    "{" +
                        "\"date\":\"2022-04-30\"," +
                        "\"last_update\":\"2022-05-01 04:20:57\"," +
                        "\"confirmed\":513480987," +
                        "\"confirmed_diff\":398732," +
                        "\"deaths\":6235415," +
                        "\"deaths_diff\":1386," +
                        "\"recovered\":0," +
                        "\"recovered_diff\":0," +
                        "\"active\":507245572," +
                        "\"active_diff\":397346," +
                        "\"fatality_rate\":0.0121" +
                    "}}\n"
    );


    JsonNode mockedEmptyJsonArray = new JsonNode("{\"data\":[]}");

    @BeforeEach
    void init(){
    }

    @AfterEach
    void tearDown() {
        reset(mockHttpResponse, httpClient);
    }


    @Test
    void testGettingSuccessfulRegionalReport_thenReturnThatCovidReportWrappedByOptional() throws ParseException {
        when(mockHttpResponse.getStatus()).thenReturn(200);
        when(mockHttpResponse.getBody()).thenReturn(mockedRegionalReportJsonNode);
        when(httpClient.getHttpResponse("https://covid-19-statistics.p.rapidapi.com/reports?region_name=Portugal&date=2022-04-30")).thenReturn(mockHttpResponse);

        CovidReport regionalReport = CovidReport.getRegionalCovidReport(
                "Portugal", parseDate("30/04/2022"), 3853800, 0, 22280, 0
        );

        Key key = Key.getRegionalKey("Portugal", parseDate("2022-04-30", new SimpleDateFormat("yyyy-MM-dd")));

        Optional<CovidReport> optionalReport = api.getReport(key);

        assertAll( ()-> {
            assertTrue(optionalReport.isPresent());
            assertThat(api.getReport(key).get(), equalTo(regionalReport));
        });
    }


    @Test
    void testGettingSuccessfulGlobalReport_thenReturnThatCovidReportWrappedByOptional() throws ParseException {
        when(mockHttpResponse.getStatus()).thenReturn(200);
        when(mockHttpResponse.getBody()).thenReturn(mockedGlobalReportJsonNode);
        when(httpClient.getHttpResponse("https://covid-19-statistics.p.rapidapi.com/reports/total?date=2022-04-30")).thenReturn(mockHttpResponse);

        CovidReport globalReport = CovidReport.getGlobalCovidReport(
                parseDate("30/04/2022"), 513480987, 398732, 6235415, 1386
        );

        Key key = Key.getGlobalKey(parseDate("2022-04-30", new SimpleDateFormat("yyyy-MM-dd")));

        Optional<CovidReport> optionalReport = api.getReport(key);

        assertAll( ()-> {
            assertTrue(optionalReport.isPresent());
            assertThat(api.getReport(key).get(), equalTo(globalReport));
        });
    }

    @Test
    void testGettingUnsuccessfulRegionalReport_thenReturnAnEmptyOptional() throws ParseException {
        when(mockHttpResponse.getStatus()).thenReturn(200);
        when(mockHttpResponse.getBody()).thenReturn(mockedEmptyJsonArray);
        when(httpClient.getHttpResponse("https://covid-19-statistics.p.rapidapi.com/reports?region_name=Portugal&date=2022-04-30")).thenReturn(mockHttpResponse);

        Key key = Key.getRegionalKey("Portugal", parseDate("2022-04-30", new SimpleDateFormat("yyyy-MM-dd")));

        assertTrue(api.getReport(key).isEmpty());
    }

    @Test
    void testGettingUnsuccessfulGlobalReport_thenReturnAnEmptyOptional() throws ParseException {
        when(mockHttpResponse.getStatus()).thenReturn(200);
        when(mockHttpResponse.getBody()).thenReturn(mockedEmptyJsonArray);
        when(httpClient.getHttpResponse("https://covid-19-statistics.p.rapidapi.com/reports/total?date=2022-04-30")).thenReturn(mockHttpResponse);

        Key key = Key.getGlobalKey(parseDate("2022-04-30", new SimpleDateFormat("yyyy-MM-dd")));

        assertTrue(api.getReport(key).isEmpty());
    }


    @Test
    void testGettingRegionsReturnsOK_thenReturnListOfRegions() {
        when(mockHttpResponse.getStatus()).thenReturn(200);
        when(mockHttpResponse.getBody()).thenReturn(mockedRegionsJsonNode);
        when(httpClient.getHttpResponse("https://covid-19-statistics.p.rapidapi.com/regions")).thenReturn(mockHttpResponse);

        assertThat(api.getAllRegions(), equalTo(Arrays.asList("China", "Taipei and environs", "US", "Japan")));
    }

    @Test
    void testGettingRegionsReturnsNotOK_thenReturnEmptyList() {
        when(mockHttpResponse.getStatus()).thenReturn(400);

        when(httpClient.getHttpResponse("https://covid-19-statistics.p.rapidapi.com/regions")).thenReturn(mockHttpResponse);

        assertThat(api.getAllRegions(), empty());
    }
}