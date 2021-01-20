package top.iotequ.framework.serializer.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import top.iotequ.util.DateUtil;
import top.iotequ.util.Util;


import java.io.IOException;
import java.util.Date;

public class DateDeserializer extends JsonDeserializer<Date> {
	@Override
	public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		if(p!=null && !Util.isEmpty(p.getText())){
			return DateUtil.string2Date(p.getText());
		} else {
			return null;
		}
	}
}
