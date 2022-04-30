package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.data;

import java.util.Date;

public class CovidReport {
    private final String region;
    private final Date date;
    private final int totalCases;
    private final int newCases;
    private final int totalDeaths;
    private final int newDeaths;

    private CovidReport(String region, Date date, int totalCases, int newCases, int totalDeaths, int newDeaths) {
        this.region = region;
        this.date = date;
        this.totalCases = totalCases;
        this.newCases = newCases;
        this.totalDeaths = totalDeaths;
        this.newDeaths = newDeaths;
    }

    public static CovidReport getRegionalCovidReport(String region, Date date, int totalCases, int newCases, int totalDeaths, int newDeaths) {
        return new CovidReport(region, date, totalCases, newCases, totalDeaths, newDeaths);
    }

    public static CovidReport getGlobalCovidReport(Date date, int totalCases, int newCases, int totalDeaths, int newDeaths) {
        return new CovidReport("Global", date, totalCases, newCases, totalDeaths, newDeaths);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CovidReport that = (CovidReport) o;
        return totalCases == that.totalCases && newCases == that.newCases && totalDeaths == that.totalDeaths && newDeaths == that.newDeaths && region.equals(that.region) && date.equals(that.date);
    }

    @Override
    public String toString() {
        return "CovidReport{" +
                "region='" + region + '\'' +
                ", date=" + date +
                ", totalCases=" + totalCases +
                ", newCases=" + newCases +
                ", totalDeaths=" + totalDeaths +
                ", newDeaths=" + newDeaths +
                '}';
    }
}
