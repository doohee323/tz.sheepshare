package tz.basis.integration.webservice;

import org.springframework.ws.client.core.WebServiceTemplate;

/**
 *
 * @author TZ
 *
 */
class WSTemplateFactory {
	public static WebServiceTemplate getWSTemplate(String wsdl)	{
		WebServiceTemplate template = new WebServiceTemplate();
//		template.setDefaultUri(wsdl);
//		template.setCheckConnectionForError(true);
		return template;
	}
}
