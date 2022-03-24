package geocodingIT;

import geocoding.Address;
import geocoding.AddressResolver;
import geocoding.TqsBasicHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AddressResolverIT {
    TqsBasicHttpClient tqsBasicHttpClient;

    AddressResolver addressResolver;

    @BeforeEach
    public void setUp() {
        tqsBasicHttpClient = new TqsBasicHttpClient();
        addressResolver = new AddressResolver(tqsBasicHttpClient);
    }

    @Test
    void findAddressByLocationTest() {
        String jsonStr = "{\"info\":{\"statuscode\":0,\"copyright\":{\"text\":\"\\u00A9 2022 MapQuest, Inc.\",\"imageUrl\":\"http://api.mqcdn.com/res/mqlogo.gif\",\"imageAltText\":\"\\u00A9 2022 MapQuest, Inc.\"},\"messages\":[]},\"options\":{\"maxResults\":1,\"thumbMaps\":true,\"ignoreLatLngInput\":false},\"results\":[{\"providedLocation\":{\"latLng\":{\"lat\":40.6318,\"lng\":-8.658}},\"locations\":[{\"street\":\"Parque Estacionamento da Reitoria - Univerisdade de Aveiro\",\"adminArea6\":\"\",\"adminArea6Type\":\"Neighborhood\",\"adminArea5\":\"Glória e Vera Cruz\",\"adminArea5Type\":\"City\",\"adminArea4\":\"\",\"adminArea4Type\":\"County\",\"adminArea3\":\"Centro\",\"adminArea3Type\":\"State\",\"adminArea1\":\"PT\",\"adminArea1Type\":\"Country\",\"postalCode\":\"3810-193\",\"geocodeQualityCode\":\"P1AAA\",\"geocodeQuality\":\"POINT\",\"dragPoint\":false,\"sideOfStreet\":\"N\",\"linkId\":\"0\",\"unknownInput\":\"\",\"type\":\"s\",\"latLng\":{\"lat\":40.631803,\"lng\":-8.657881},\"displayLatLng\":{\"lat\":40.631803,\"lng\":-8.657881},\"mapUrl\":\"http://open.mapquestapi.com/staticmap/v5/map?key=uXSAVwYWbf9tJmsjEGHKKAo0gOjZfBLQ&type=map&size=225,160&locations=40.6318025,-8.657881|marker-sm-50318A-1&scalebar=true&zoom=15&rand=917063764\",\"roadMetadata\":null}]}]}";


        assertEquals (
                addressResolver.findAddressForLocation(40.6318, -8.658)
                        .orElseThrow() ,
                new Address("Parque Estacionamento da Reitoria - Univerisdade de Aveiro", "Glória e Vera Cruz", "3810-193", "")
        );

    }
}