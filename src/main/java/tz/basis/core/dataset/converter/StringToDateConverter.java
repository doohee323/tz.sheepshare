package tz.basis.core.dataset.converter;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.core.convert.converter.Converter;

/**
 *
 * @author TZ
 *
 */
public class StringToDateConverter implements Converter<String, Timestamp>{

	private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	public Timestamp convert(String source) {
		Timestamp date = null;

		try {
			date = new Timestamp(format.parse(source).getTime());
		} catch (ParseException e) {
		}

		return date;
	}
}