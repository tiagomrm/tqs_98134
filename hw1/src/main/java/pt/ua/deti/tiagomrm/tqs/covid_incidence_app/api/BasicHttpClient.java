package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.api;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

public class BasicHttpClient {
    private String host;
    private String key;

    public BasicHttpClient(String host, String key) {
        this.host = host;
        this.key = key;
    }

    public HttpResponse<JsonNode> getHttpResponse(String url) {
        return Unirest.get(url)
                .header("X-RapidAPI-Host", host)
                .header("X-RapidAPI-Key", key)
                .asJson();
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setKey(String key) {
        this.key = key;
    }
}