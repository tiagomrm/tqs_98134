package pt.ua.deti.tiagomrm.tqs.covid_incidence_app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
public
class Hw1ApplicationTests {

    @Test
    void contextLoads() {
    }

    public static Date parseDate(String date, SimpleDateFormat format) throws ParseException {
        return format.parse(date);
    }

    public static Date parseDate(String date) throws ParseException {
        return parseDate(date, new SimpleDateFormat("dd/MM/yyyy"));
    }

}
