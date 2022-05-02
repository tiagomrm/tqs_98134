package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class CovidReportDeserializerTest {

    @Test
    void testDeserialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        CovidReport report = CovidReport.getRegionalCovidReport("Portugal", LocalDate.parse("2022-04-26", DateTimeFormatter.ISO_DATE), 23864, 472, 903, 23);

        assertThat(report, equalTo(mapper.readValue(mapper.writeValueAsString(report), CovidReport.class)));
    }
}