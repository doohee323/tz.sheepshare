package tz.basis.core.ux.json.mvc;

import org.springframework.web.context.request.NativeWebRequest;

import tz.basis.core.CoreChnls;
import tz.basis.core.mvc.adaptor.TzRequestIF;
import tz.basis.core.mvc.adaptor.TzResponseIF;
import tz.basis.core.mvc.resolver.TzWebArgResolver;
import tz.basis.core.ux.json.dataset.JsonDSReader;

/**
 *
 * @author TZ
 *
 */
public class JsonWebArgResolver extends TzWebArgResolver {

	protected TzRequestIF getTzRequest(NativeWebRequest webRequest) {
		return new JsonRequest((JsonDSReader) getDSReader(webRequest));
	}

	protected TzResponseIF getTzResponse(NativeWebRequest webRequest) {
		return new JsonResponse();
	}

	protected CoreChnls getChnlType() {
		return CoreChnls.JSON;
	}
}
