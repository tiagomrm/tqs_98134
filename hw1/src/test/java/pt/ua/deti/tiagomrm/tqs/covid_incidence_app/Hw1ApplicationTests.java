package pt.ua.deti.tiagomrm.tqs.covid_incidence_app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

@SpringBootTest
public
class Hw1ApplicationTests {

    @Test
    void contextLoads() {
    }

    public static Date parseDate(String date) throws ParseException {
        return DateFormat.getDateInstance().parse(date);
    }

}
