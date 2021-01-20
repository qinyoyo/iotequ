package top.iotequ.framework.serializer;

import java.util.Date;

import org.springframework.core.convert.converter.Converter;

import top.iotequ.util.DateUtil;

public class RequestParameterDateConverter implements Converter<String,Date> {
	    @Override
	    public Date convert(String s) {
	        return DateUtil.string2Date(s);
	    }
}
