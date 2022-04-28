package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.data;

import java.util.Date;

public class CovidReport {
    private final String region;
    private final Date date;
    private final int totalCases;
    private final int newCases;
    private final int totalDeaths;
    private final int newDeaths;

    public CovidReport(String region, Date date, int totalCases, int newCases, int totalDeaths, int newDeaths) {
        this.region = region;
        this.date = date;
        this.totalCases = totalCases;
        this.newCases = newCases;
        this.totalDeaths = totalDeaths;
        this.newDeaths = newDeaths;
    }

    public String getRegion() {
        return region;
    }

    public Date getDate() {
        return date;
    }

    public int getTotalCases() {
        return totalCases;
    }

    public int getNewCases() {
        return newCases;
    }

    public int getTotalDeaths() {
        return totalDeaths;
    }

    public int getNewDeaths() {
        return newDeaths;
    }
}
