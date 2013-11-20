package tz.basis.core.ux.comweb.mvc;

import org.springframework.web.context.request.NativeWebRequest;

import tz.basis.core.CoreChnls;
import tz.basis.core.mvc.adaptor.TzRequestIF;
import tz.basis.core.mvc.adaptor.TzResponseIF;
import tz.basis.core.mvc.resolver.TzWebArgResolver;
import tz.basis.core.ux.comweb.dataset.ComWebDSReader;

/**
 *
 * @author TZ
 *
 */
public class ComWebArgResolver extends TzWebArgResolver {

    @Override
    protected CoreChnls getChnlType(){
        return CoreChnls.COMWEB;
    }

    @Override
    protected TzRequestIF getTzRequest(NativeWebRequest webRequest){
        return new ComWebRequest((ComWebDSReader)getDSReader(webRequest));
    }

    @Override
    protected TzResponseIF getTzResponse(NativeWebRequest webRequest){
        return new ComWebResponse();
    }
}
