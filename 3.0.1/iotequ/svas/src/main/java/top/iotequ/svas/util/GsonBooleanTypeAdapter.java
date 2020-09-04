package top.iotequ.svas.util;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class GsonBooleanTypeAdapter extends TypeAdapter<Boolean> {
    @Override
    public void write(JsonWriter out, Boolean value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value);
        }
    }
    @Override
    public Boolean read(JsonReader in) throws IOException {
        JsonToken peek = in.peek();
        switch (peek) {
            case BOOLEAN:
                return in.nextBoolean();
            case NULL:
                in.nextNull();
                return null;
            case NUMBER:
                return in.nextInt() != 0;
            case STRING:
                String s=in.nextString();
                if (s==null || s.isEmpty())
                    return false;
                else if (s.equals("1") || s.toLowerCase().startsWith("t") || s.toLowerCase().startsWith("y") || s.toLowerCase().startsWith(".t"))
                    return true;
                else
                    return false;
            default:
                throw new JsonParseException("Expected BOOLEAN or NUMBER but was " + peek);
        }
    }
}
