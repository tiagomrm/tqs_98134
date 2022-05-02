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
import java.util.*;

@Component
public class CovidAPIByAPISPORTS implements CovidAPIInterface {

    BasicHttpClient httpClient;

    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final Logger logger = LogManager.getLogger();

    @Autowired
    public CovidAPIByAPISPORTS() {
        httpClient = new BasicHttpClient("covid-19-statistics.p.rapidapi.com", "f4e5f56284msh79ce055a0bad368p1be5d8jsncc4511b3c4eb");
    }

    @Override
    public Optional<CovidReport> getReport(Key key) {
        String url = String.format(
                "https://covid-193.p.rapidapi.com/history?country=%s&day=%s",
                    key.isGlobal() ? "All" : key.getRegion().replace(" ", "-"),
                    dateFormat.format(key.getDate())
        );

        Optional<CovidReport> returningReport = Optional.empty();

        HttpResponse<JsonNode> response = httpClient.getHttpResponse(url);
        if (responseIsOkAndNotEmpty(response)) {
            JSONObject responseReport = response.getBody().getObject().getJSONArray("response").getJSONObject(0);

            returningReport = getCovidReportFromJSON(key, responseReport);
        }
        return returningReport;
    }

    private Optional<CovidReport> getCovidReportFromJSON(Key key, JSONObject responseReport) {
        JSONObject cases = responseReport.getJSONObject("cases");
        JSONObject deaths = responseReport.getJSONObject("deaths");

        int totalCases = cases.getInt("total");
        int newCases = cases.get("new") == null ? 0 : parseIntFromAlphaNumericString(cases.getString("new"));

        int totalDeaths = deaths.getInt("total");
        int newDeaths = deaths.get("new") == null ? 0 : parseIntFromAlphaNumericString(deaths.getString("new"));

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
        String url ="https://covid-193.p.rapidapi.com/countries";
        HttpResponse<JsonNode> response = httpClient.getHttpResponse(url);

        ArrayList<String> regionsList = new ArrayList<>();

        if (responseIsOkAndNotEmpty(response)) {

            JSONArray jsonArray = response.getBody().getObject().getJSONArray("response");

            for (int i=0; i < jsonArray.length(); i++)
                regionsList.add(jsonArray.getString(i).replace("-", " ").trim());
        }

        return regionsList;
    }

    private boolean responseIsOkAndNotEmpty(HttpResponse<JsonNode> response) {
        return response.getStatus() == 200
                && response.getBody().getObject().has("results")
                && response.getBody().getObject().getInt("results") >= 0;
    }

    private int parseIntFromAlphaNumericString(String str) {
        return Integer.parseInt(str.replaceAll("\\D", ""));
    }
}
