package geocoding;

import java.util.Objects;

public class Address {
    private String road;
    private String city;
    private String zip;
    private String houseNumber;

    public Address(String road, String city, String zip, String houseNumber) {
        this.road = road;
        this.city = city;
        this.zip = zip;
        this.houseNumber = houseNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(road, address.road) && Objects.equals(city, address.city) && Objects.equals(zip, address.zip) && Objects.equals(houseNumber, address.houseNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(road, city, zip, houseNumber);
    }

    @Override
    public String toString() {
        return "Address{" +
                "road='" + road + '\'' +
                ", city='" + city + '\'' +
                ", zip='" + zip + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                '}';
    }
}
