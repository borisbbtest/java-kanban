package org.yapr.sprint4.task.service.handler;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.JsonToken; // Import the correct JsonToken class

import java.io.IOException;
import java.time.Duration;

public class DurationAdapter extends TypeAdapter<Duration> {
    @Override
    public void write(JsonWriter out, Duration duration) throws IOException {
        if (duration == null) {
            out.nullValue(); // Handle null duration
        } else {
            out.value(duration.getSeconds());
        }
    }

    @Override
    public Duration read(JsonReader in) throws IOException {
        // Check if the next token is null
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null; // Handle null value when reading
        }
        long seconds = in.nextLong();
        return Duration.ofSeconds(seconds);
    }
}
