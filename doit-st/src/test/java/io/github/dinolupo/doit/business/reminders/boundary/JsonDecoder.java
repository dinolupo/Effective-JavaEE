package io.github.dinolupo.doit.business.reminders.boundary;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;
import java.io.Reader;

/**
 * Created by dinolupo.github.io on 13/08/16.
 */
public class JsonDecoder implements Decoder.TextStream<JsonObject> {

    @Override
    public void init(EndpointConfig endpointConfig) {
    }

    @Override
    public JsonObject decode(Reader reader) throws DecodeException, IOException {
        try (JsonReader jsonReader = Json.createReader(reader)) {
            return jsonReader.readObject();
        }
    }

    @Override
    public void destroy() {
    }
}
