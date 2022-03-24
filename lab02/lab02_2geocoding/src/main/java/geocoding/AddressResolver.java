package geocoding;

import java.util.Optional;
import org.json.JSONObject;

public class AddressResolver {

    private ISimpleHttpClient httpClient;

    AddressResolver(ISimpleHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    Optional<Address> findAddressForLocation(double lat, double lng) {
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
