package tz.basis.data.web.context;

import org.springframework.core.NamedInheritableThreadLocal;

/**
 *
 * @author TZ
 *
 */
public class RequestContextHolder {

	private static final String CONTEXT_NAME = "TZ";
	private static final ThreadLocal<RequestContext> INHERIABLE_REQUEST_CONTEXT_HOLDER = new NamedInheritableThreadLocal<RequestContext>(CONTEXT_NAME);

	public static RequestContext getContext()	{
		RequestContext context = INHERIABLE_REQUEST_CONTEXT_HOLDER.get();

		if(context == null)	{
			context = new RequestContext();
			INHERIABLE_REQUEST_CONTEXT_HOLDER.set(context);
		}

		return context;
	}

	public static void clear()	{
		INHERIABLE_REQUEST_CONTEXT_HOLDER.remove();
	}

	public static void clear(String contextName) {

	}
}
