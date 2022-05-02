package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.service;

import java.time.LocalDate;
import java.util.Objects;

public class Key {
    private final String region;
    private final LocalDate date;

    private Key(String region, LocalDate date) {
        this.region = region;
        this.date = date;
    }

    public static Key getRegionalKey(String region, LocalDate date) {
        return new Key(region, date);
    }

    public static Key getGlobalKey(LocalDate date) {
        return new Key("Global", date);
    }

    public boolean isGlobal() {
        return this.region.equalsIgnoreCase("global");
    }

    public String getRegion() {
        return region;
    }

    public LocalDate getDate() {
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

    @Override
    public String toString() {
        return "Key{" +
                "region='" + region + '\'' +
                ", date=" + date +
                '}';
    }
}