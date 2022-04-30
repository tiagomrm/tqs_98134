package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.data;

import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;
import static pt.ua.deti.tiagomrm.tqs.covid_incidence_app.Hw1ApplicationTests.parseDate;

class CovidReportTest {

    @Test
    void testCovidReportsIsEqualToItself() throws ParseException {
        Date date = parseDate("23/07/2021");
        CovidReport reportA = CovidReport.getRegionalCovidReport("A", date, 1, 2, 3,4);

        assertThat(reportA, equalTo(reportA));
    }

    @Test
    void testTwoEqualCovidReportsAreEqual() throws ParseException {
        Date date = parseDate("23/07/2021");
        CovidReport reportA = CovidReport.getRegionalCovidReport("A", date, 1, 2, 3,4);
        CovidReport reportB = CovidReport.getRegionalCovidReport("A", date, 1, 2, 3,4);

        assertThat(reportA, equalTo(reportB));
    }

    @Test
    void testTwoDifferentCovidReportsAreNotEqual() throws ParseException {
        Date dateA = parseDate("23/07/2021");
        Date dateB = parseDate("24/07/2021");
        CovidReport reportA = CovidReport.getRegionalCovidReport("A", dateA, 1, 2, 3,4);
        CovidReport reportB = CovidReport.getRegionalCovidReport("B", dateB, 0, 3, 5,6);

        assertThat(reportA, not(equalTo(reportB)));
    }
}