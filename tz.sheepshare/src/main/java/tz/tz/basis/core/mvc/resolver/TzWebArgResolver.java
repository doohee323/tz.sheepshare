package tz.basis.core.mvc.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

import tz.basis.core.CoreChnls;
import tz.basis.core.dataset.converter.DSConverter;
import tz.basis.core.dataset.io.DSReader;
import tz.basis.core.mvc.TzDSAccessor;
import tz.basis.core.mvc.adaptor.TzRequestAdapter;
import tz.basis.core.mvc.adaptor.TzRequestIF;
import tz.basis.core.mvc.adaptor.TzResponseAdapter;
import tz.basis.core.mvc.adaptor.TzResponseIF;
import tz.basis.core.mvc.context.TzRequestContextHolder;
import tz.extend.core.mvc.TzResponse;

public abstract class TzWebArgResolver implements WebArgumentResolver {

    protected static final Logger logger = LoggerFactory.getLogger(TzWebArgResolver.class);

    protected DSConverter dataSetConverter;

    public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception{
        if(TzRequestContextHolder.get().getTzChnlType() == getChnlType()){
            if(TzRequestAdapter.class.isAssignableFrom(methodParameter.getParameterType())){
                return BeanUtils.instantiateClass(
                        methodParameter.getParameterType().getConstructor(new Class[] { TzRequestIF.class }),
                        new Object[] { buildTzRequest(methodParameter, webRequest) });
            }

            if(TzRequestIF.class.isAssignableFrom(methodParameter.getParameterType())){
                return buildTzRequest(methodParameter, webRequest);
            }

            if(TzResponseAdapter.class.isAssignableFrom(methodParameter.getParameterType())){
                return BeanUtils.instantiateClass(
                        methodParameter.getParameterType().getConstructor(new Class[] { TzResponseIF.class }),
                        new Object[] { buildTzResponse(webRequest) });
            }

            if(TzResponseIF.class.isAssignableFrom(methodParameter.getParameterType())){
                return buildTzResponse(webRequest);
            }

        }

        return UNRESOLVED;
    }

    protected Object buildTzResponse(NativeWebRequest webRequest){
        TzResponseIF tzResponse = getTzResponse(webRequest);

        ((TzDSAccessor)tzResponse).setDSConverter(this.dataSetConverter);

        TzRequestContextHolder.get().setDSAccessor((TzDSAccessor)tzResponse);

        if(logger.isDebugEnabled()){
            logger.trace("[TZ] 파라미터 변환 작업을 수행합니다. : ChnlType={}, Class={}", new Object[] { getChnlType(), getClass() });
        }

        return tzResponse;
    }

    protected Object buildTzRequest(MethodParameter methodParameter, NativeWebRequest webRequest){
        if(logger.isDebugEnabled()){
            logger.trace("[TZ] 파라미터 변환 작업을 수행합니다. : ChnlType={}, Class={}", new Object[] { getChnlType(), getClass() });
        }
        return getTzRequest(webRequest);
    }

    protected abstract CoreChnls getChnlType();

    protected abstract TzRequestIF getTzRequest(NativeWebRequest paramNativeWebRequest);

    protected abstract TzResponseIF getTzResponse(NativeWebRequest paramNativeWebRequest);

    protected DSReader getDSReader(NativeWebRequest webRequest){
        return TzRequestContextHolder.get().getDSReader();
    }

    public void setDSConverter(DSConverter dataSetConverter){
        this.dataSetConverter = dataSetConverter;
    }

}