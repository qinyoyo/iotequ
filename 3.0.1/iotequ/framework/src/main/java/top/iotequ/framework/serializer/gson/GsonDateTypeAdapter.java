package top.iotequ.framework.serializer.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import top.iotequ.framework.exception.IotequIOException;
import top.iotequ.framework.exception.IotequThrowable;
import top.iotequ.framework.util.DateUtil;

import java.io.IOException;
import java.util.Date;

public class GsonDateTypeAdapter extends GsonDatetimeTypeAdapter {
	@Override
	public void write(JsonWriter out, Date value) throws IOException {
		if (value == null) {
            out.nullValue();
        } else {
            out.value(DateUtil.date2String(value,"yyyy-MM-dd"));
        }		
	}
}