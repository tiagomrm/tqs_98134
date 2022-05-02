package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.api;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.data.CovidReport;
import pt.ua.deti.tiagomrm.tqs.covid_incidence_app.service.Key;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CovidAPIByAxisbits implements CovidAPIInterface {

    BasicHttpClient httpClient;

    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final Logger logger = LogManager.getLogger();

    @Autowired
    public CovidAPIByAxisbits() {
        httpClient = new BasicHttpClient("covid-19-statistics.p.rapidapi.com", "f4e5f56284msh79ce055a0bad368p1be5d8jsncc4511b3c4eb");
    }

    @Override
    public Optional<CovidReport> getReport(Key key) {

        String url = String.format(
                "https://covid-19-statistics.p.rapidapi.com/reports%s?%sdate=%s",
                key.isGlobal() ? "/total" : "",
                key.isGlobal() ? "" : "region_name=" + key.getRegion().replace(" ", "%20") + "&",
                dateFormat.format(key.getDate())
        );

        Optional<CovidReport> returningReport = Optional.empty();

        HttpResponse<JsonNode> response = httpClient.getHttpResponse(url);

        if (responseIsOkAndNotEmpty(response)) {

            JSONObject responseReport =
                    key.isGlobal() ?
                            response.getBody().getObject().getJSONObject("data")
                            : response.getBody().getObject().getJSONArray("data").getJSONObject(0);

            getCovidReportFromJSON(key, responseReport);
            returningReport = getCovidReportFromJSON(key, responseReport);
        }
        return returningReport;
    }

    private Optional<CovidReport> getCovidReportFromJSON(Key key, JSONObject responseReport) {

        int totalCases = responseReport.getInt("confirmed");
        int newCases = responseReport.getInt("confirmed_diff");
        int totalDeaths = responseReport.getInt("deaths");
        int newDeaths = responseReport.getInt("deaths_diff");

        return Optional.of(
                CovidReport.getRegionalCovidReport(
                        key.getRegion(),
                        key.getDate(),
                        totalCases,
                        newCases,
                        totalDeaths,
                        newDeaths
                )
        );
    }

    @Override
    public List<String> getAllRegions() {
        String url = "https://covid-19-statistics.p.rapidapi.com/regions";
        HttpResponse<JsonNode> response = httpClient.getHttpResponse(url);

        ArrayList<String> regionsList = new ArrayList<>();

        if (responseIsOkAndNotEmpty(response)) {

            JSONArray jsonArray = response.getBody().getObject().getJSONArray("data");

            for (int i=0; i < jsonArray.length(); i++)
                regionsList.add(jsonArray.getJSONObject(i).getString("name"));
        }

        return regionsList;
    }

    private boolean responseIsOkAndNotEmpty(HttpResponse<JsonNode> response) {
        return response.getStatus() == 200
                && response.getBody().getObject().has("data")
                && (
                        !(response.getBody().getObject().get("data") instanceof JSONArray)
                                || (response.getBody().getObject().getJSONArray("data").length() > 0
                        )
        );
    }
}
