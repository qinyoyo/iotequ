import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class GsonBooleanTypeAdapter extends TypeAdapter<Boolean> {
    static public boolean boolValue(Object obj) {
        if (obj == null) return false;
        else if (obj instanceof Boolean) return (Boolean) obj;
        else if (obj instanceof Integer) return (Integer) obj != 0;
        else if (obj instanceof Short) return (Short) obj != 0;
        else if (obj instanceof Byte) return (Byte) obj != 0;
        else if (obj instanceof Long) return (Long) obj != 0;
        else {
            String s = obj.toString().trim().toLowerCase();
            if (s.equals("1") || s.equals("true") || s.equals("yes") || s.equals("t") || s.equals("y") || s.equals(".t."))
                return true;
            else
                return false;
        }
    }
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
                return boolValue(in.nextString());
            default:
                return null;
        }
	}
}
