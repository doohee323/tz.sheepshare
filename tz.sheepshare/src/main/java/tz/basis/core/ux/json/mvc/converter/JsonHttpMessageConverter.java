package tz.basis.core.ux.json.mvc.converter;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;

import tz.basis.core.CoreChnls;
import tz.basis.core.dataset.io.DSReader;
import tz.basis.core.dataset.io.DSWriter;
import tz.basis.core.mvc.TzDSAccessor;
import tz.basis.core.mvc.adaptor.TzRequestIF;
import tz.basis.core.mvc.adaptor.TzResponseIF;
import tz.basis.core.mvc.converter.TzHttpMessageConverter;
import tz.basis.core.ux.json.dataset.JsonDSReader;
import tz.basis.core.ux.json.dataset.JsonDSWriter;
import tz.basis.core.ux.json.mvc.JsonRequest;
import tz.basis.core.ux.json.mvc.JsonResponse;

/**
 *
 * @author TZ
 *
 */
public class JsonHttpMessageConverter extends TzHttpMessageConverter {

	private List<MediaType> mediaTypes = Arrays.asList(new MediaType("application", "json+core"));

	public List<MediaType> getSupportedMediaTypes() {
		return mediaTypes;
	}

	@Override
	protected CoreChnls getChnlType() {
		return CoreChnls.JSON;
	}

	@Override
	protected TzRequestIF getTzRequest(DSReader reader) {
		return new JsonRequest((JsonDSReader) reader);
	}

	@Override
	protected TzResponseIF getTzResponse() {
		return new JsonResponse();
	}

	@Override
	protected DSReader getDSReader(HttpServletRequest request) {
		return new JsonDSReader(request);
	}

	@Override
	protected DSWriter getDSWriter(HttpServletRequest request, HttpServletResponse response, TzDSAccessor accessor) {
		return new JsonDSWriter(response, accessor);
	}
}
