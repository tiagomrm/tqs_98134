package pt.ua.deti.tiagomrm.tqs.covid_incidence_app.cache;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class CovidDataCacheManagerSerializer extends StdSerializer<CovidDataCacheManager> {

    public CovidDataCacheManagerSerializer() {
        this(null);
    }

    public CovidDataCacheManagerSerializer(Class<CovidDataCacheManager> t) {
        super(t);
    }

    @Override
    public void serialize(CovidDataCacheManager covidDataCacheManager, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("initialization_date", covidDataCacheManager.getDateOfInitialization().format(DateTimeFormatter.ISO_DATE_TIME));
        jsonGenerator.writeNumberField("calls", covidDataCacheManager.getCalls());
        jsonGenerator.writeNumberField("hits", covidDataCacheManager.getHits());
        jsonGenerator.writeNumberField("misses", covidDataCacheManager.getMisses());
    }
}
