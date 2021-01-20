package top.iotequ.framework.serializer.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import top.iotequ.util.DateUtil;

import java.io.IOException;
import java.util.Date;

public class DatetimeSerializer extends JsonSerializer<Date> {
    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(DateUtil.date2String(value,null));
    }
}
