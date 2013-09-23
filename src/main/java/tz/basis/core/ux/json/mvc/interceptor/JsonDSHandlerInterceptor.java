package tz.basis.core.ux.json.mvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

import tz.basis.core.CoreChnls;
import tz.basis.core.dataset.io.DSReader;
import tz.basis.core.dataset.io.DSWriter;
import tz.basis.core.mvc.TzDSAccessor;
import tz.basis.core.mvc.interceptor.TzDSHandlerInterceptor;
import tz.basis.core.ux.json.dataset.JsonDSReader;
import tz.basis.core.ux.json.dataset.JsonDSWriter;
import tz.extend.util.StringUtil;

/**
 *
 * @author TZ
 *
 */
public class JsonDSHandlerInterceptor extends TzDSHandlerInterceptor {

	protected CoreChnls getChnlType() {
		return CoreChnls.JSON;
	}

	protected DSReader getDSReader(HttpServletRequest request, HttpServletResponse response) {
		return new JsonDSReader(request);
	}

	protected DSWriter getDSWriter(HttpServletRequest request, HttpServletResponse response, TzDSAccessor accessor) {
		return new JsonDSWriter(response, accessor);
	}

	protected boolean checkTzRequest(HttpServletRequest request) {
		String contentType = request.getHeader("Content-Type");
		String url = request.getRequestURI();

		if (StringUtil.getText(contentType).indexOf("application/json") > -1 || url.endsWith(".ajax")) {
			return true;
		} else {
			return false;
		}
	}
}
