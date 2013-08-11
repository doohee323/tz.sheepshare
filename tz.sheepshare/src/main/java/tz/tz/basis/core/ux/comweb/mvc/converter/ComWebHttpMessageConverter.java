package tz.basis.core.ux.comweb.mvc.converter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.ModelAndView;

import tz.basis.core.CoreChnls;
import tz.basis.core.dataset.io.DSReader;
import tz.basis.core.dataset.io.DSWriter;
import tz.basis.core.mvc.TzDSAccessor;
import tz.basis.core.mvc.adaptor.TzRequestIF;
import tz.basis.core.mvc.adaptor.TzResponseIF;
import tz.basis.core.mvc.converter.TzHttpMessageConverter;
import tz.basis.core.ux.comweb.dataset.ComWebDSReader;
import tz.basis.core.ux.comweb.mvc.ComWebRequest;
import tz.basis.core.ux.comweb.mvc.ComWebResponse;

/**
 *
 * @author TZ
 *
 */
public class ComWebHttpMessageConverter extends TzHttpMessageConverter {

    private FormHttpMessageConverter converter = new FormHttpMessageConverter();

    @Override
    public void write(Object accessor, MediaType contentType, HttpOutputMessage outputMessage) throws IOException,
            HttpMessageNotWritableException{
        ModelAndView modelAndView = ((TzDSAccessor)accessor).getModelAndView();

        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<String, Object>(
                (Map)modelAndView.getModelMap());

        converter.write(multiValueMap, contentType, outputMessage);
    }

    @Override
    protected CoreChnls getChnlType(){
        return CoreChnls.COMWEB;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes(){
        return converter.getSupportedMediaTypes();
    }

    @Override
    protected TzRequestIF getTzRequest(DSReader reader){
        return new ComWebRequest((ComWebDSReader)reader);
    }

    @Override
    protected TzResponseIF getTzResponse(){
        return new ComWebResponse();
    }

    @Override
    protected DSReader getDSReader(HttpServletRequest request){
        return new ComWebDSReader(request, null);
    }

    @Override
    protected DSWriter getDSWriter(HttpServletRequest request, HttpServletResponse response,
            TzDSAccessor accessor){
        return null;
    }

}
