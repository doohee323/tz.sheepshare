package tz.basis.core.mvc.context;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tz.basis.core.CoreChnls;
import tz.basis.core.dataset.io.DSReader;
import tz.basis.core.dataset.io.DSWriter;
import tz.basis.core.mvc.TzDSAccessor;

/**
 *
 * @author TZ
 *
 */
public class TzRequestContext {

    private CoreChnls tzChnlType;

    private boolean isTzRequest;

    private DSReader reader;

    private DSWriter writer;

    private TzDSAccessor accessor;

    private HttpServletRequest httpServletRequest;

    private HttpServletResponse httpServletResponse;

    private Map<String, Object> namedParameter = new HashMap<String, Object>();

    /**
     *
     * @return
     */
    public boolean isTzRequest(){
        return isTzRequest;
    }

    /**
     *
     * @param isTzRequest
     */
    public void setTzRequest(boolean isTzRequest){
        this.isTzRequest = isTzRequest;
    }

    /**
     *
     * @return
     */
    public DSReader getDSReader(){
        return reader;
    }

    /**
     *
     * @param reader
     */
    public void setDSReader(DSReader reader){
        this.reader = reader;
    }

    /**
     *
     * @return
     */
    public DSWriter getDSWriter(){
        return writer;
    }

    /**
     *
     * @param writer
     */
    public void setDSWriter(DSWriter writer){
        this.writer = writer;
    }

    /**
     *
     * @return
     */
    public TzDSAccessor getDSAccessor(){
        return accessor;
    }

    /**
     *
     * @param accessor
     */
    public void setDSAccessor(TzDSAccessor accessor){
        this.accessor = accessor;
    }

    /**
     *
     * @return
     */
    public HttpServletRequest getHttpServletRequest(){
        return httpServletRequest;
    }

    /**
     *
     * @param httpServletRequest
     */
    public void setHttpServletRequest(HttpServletRequest httpServletRequest){
        this.httpServletRequest = httpServletRequest;
    }

    /**
     *
     * @return
     */
    public HttpServletResponse getHttpServletResponse(){
        return httpServletResponse;
    }

    /**
     *
     * @param httpServletResponse
     */
    public void setHttpServletResponse(HttpServletResponse httpServletResponse){
        this.httpServletResponse = httpServletResponse;
    }

    /**
     *
     * @return
     */
    public CoreChnls getTzChnlType(){
        return tzChnlType;
    }

    /**
     *
     * @param tzChnlType
     */
    public void setTzChnlType(CoreChnls tzChnlType){
        this.tzChnlType = tzChnlType;
    }

    /**
     *
     * @param key
     * @param value
     * @return
     */
    public TzRequestContext addNamedParameter(String key, Object value){
        this.namedParameter.put(key, value);
        return this;
    }

    /**
     *
     * @param key
     * @return
     */
    public Object getNamedParameter(String key){
        return this.namedParameter.get(key);
    }
}
