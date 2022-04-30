package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.service;

import java.util.Date;
import java.util.Objects;

public class Key {
    private final String region;
    private final Date date;

    private Key(String region, Date date) {
        this.region = region;
        this.date = date;
    }

    public static Key getRegionalKey(String region, Date date) {
        return new Key(region, date);
    }

    public static Key getGlobalKey(Date date) {
        return new Key("Global", date);
    }

    public boolean isGlobal() {
        return this.region.equalsIgnoreCase("global");
    }

    public String getRegion() {
        return region;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Key key = (Key) o;
        return region.equals(key.region) && date.equals(key.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, date);
    }
}