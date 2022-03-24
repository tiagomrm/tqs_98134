package geocoding;

import java.util.Optional;
import org.json.JSONObject;

public class AddressResolver {

    private final ISimpleHttpClient httpClient;

    public AddressResolver(ISimpleHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public Optional<Address> findAddressForLocation(double lat, double lng) {
        if (Math.abs(lat) > 90 || Math.abs(lng) > 180)
            throw new IllegalArgumentException("Invalid input coordinates (" + lat + "," + lng + ")");

        JSONObject locations = new JSONObject(
                httpClient.doHttpGet(lat + ", " + lng))
                .getJSONArray("results")
                .getJSONObject(0)
                .getJSONArray("locations")
                .getJSONObject(0);

        return Optional.of(
                new Address(
                        locations.getString("street"),
                        locations.getString("adminArea5"),
                        locations.getString("postalCode"),
                        ""
                ));
//        return Optional.empty();
    }
}
