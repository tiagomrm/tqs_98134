package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.data;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

class CovidReportTest {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Test
    void testCovidReportsIsEqualToItself() {
        LocalDate date = LocalDate.parse("23/07/2021", formatter);
        CovidReport reportA = CovidReport.getRegionalCovidReport("A", date, 1, 2, 3,4);

        assertThat(reportA, equalTo(reportA));
    }

    @Test
    void testTwoEqualCovidReportsAreEqual() {
        LocalDate date = LocalDate.parse("23/07/2021", formatter);
        CovidReport reportA = CovidReport.getRegionalCovidReport("A", date, 1, 2, 3,4);
        CovidReport reportB = CovidReport.getRegionalCovidReport("A", date, 1, 2, 3,4);

        assertThat(reportA, equalTo(reportB));
    }

    @Test
    void testTwoDifferentCovidReportsAreNotEqual() {
        LocalDate dateA = LocalDate.parse("23/07/2021", formatter);
        LocalDate dateB = LocalDate.parse("24/07/2021", formatter);
        CovidReport reportA = CovidReport.getRegionalCovidReport("A", dateA, 1, 2, 3,4);
        CovidReport reportB = CovidReport.getRegionalCovidReport("B", dateB, 0, 3, 5,6);

        assertThat(reportA, not(equalTo(reportB)));
    }
}