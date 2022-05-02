package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.data;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CovidReportDeserializer extends StdDeserializer<CovidReport> {
    public CovidReportDeserializer() {
        this(null);
    }

    public CovidReportDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public CovidReport deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        String region = node.get("region").asText();
        LocalDate date = LocalDate.parse(node.get("date").asText(), DateTimeFormatter.ISO_DATE);
        int totalCases = node.get("totalCases").asInt();
        int newCases = node.get("newCases").asInt();
        int totalDeaths = node.get("totalDeaths").asInt();
        int newDeaths = node.get("newDeaths").asInt();

        return CovidReport.getRegionalCovidReport(region, date, totalCases, newCases, totalDeaths, newDeaths);
    }
}
