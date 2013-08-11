package tz.basis.core.mvc.converter;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;

import tz.basis.core.CoreChnls;
import tz.basis.core.dataset.converter.DSConverter;
import tz.basis.core.dataset.io.DSReader;
import tz.basis.core.dataset.io.DSWriter;
import tz.basis.core.mvc.TzDSAccessor;
import tz.basis.core.mvc.adaptor.TzRequestIF;
import tz.basis.core.mvc.adaptor.TzResponseIF;
import tz.basis.core.mvc.context.TzRequestContextHolder;

/**
 *
 * REST 통신을 위한 메세지컨버터<br/>
 * XML이나 JSON을 이용한 통신에 이용할 수 있다.<br/>
 * request를 model에 바인딩하고 다시 view를 이용해 클라이언트로 보내는 대신 request와 response 자체를 메시지로 다룰 때 사용.<br/>
 * 파라미터에 RequestBody, 메소드에 ResponseBody annotation을 붙여서 사용한다.<br/>
 * annotation이 없으면 WebArgResolver, ViewResolver에서 파라미터를 변환하고 model과 view를 돌려준다.
 *
 * @author TZ
 *
 */
public abstract class TzHttpMessageConverter implements HttpMessageConverter<Object> {

    private static final Logger logger = LoggerFactory.getLogger(TzHttpMessageConverter.class);

    private static final Object UNRESOLVED = null;

    private DSConverter dataSetConverter;

    public void setDSConverter(DSConverter dataSetConverter){
        this.dataSetConverter = dataSetConverter;
    }

    /**
     * 지원 가능한 chnl 인지 여부를 판단하여 boolean 값으로 return
     */
    public boolean canRead(Class<?> clazz, MediaType mediaType){
        boolean canRead = (TzRequestIF.class.isAssignableFrom(clazz) || TzResponseIF.class.isAssignableFrom(clazz))
                && isSupportedChnl(mediaType);

        if(canRead && logger.isDebugEnabled()){
            logger.trace("[TZ] {} 객체 읽기를 위한 메세지 컨버터를 초기화합니다. : Class={}",
                    new Object[] { getChnlType(), this.getClass() });
        }

        return canRead;
    }

    /**
     * 지원 가능한 chnl 인지 여부를 판단하여 boolean 값으로 return
     */
    public boolean canWrite(Class<?> clazz, MediaType mediaType){
        boolean canWrite = TzResponseIF.class.isAssignableFrom(clazz) && isSupportedChnl(mediaType);

        if(canWrite && logger.isDebugEnabled()){
            logger.trace("[TZ] {} 객체 쓰기를 위한 메세지 컨버터를 초기화합니다. : Class={}",
                    new Object[] { getChnlType(), this.getClass() });
        }

        return canWrite;
    }

    /**
     *
     * @param mediaType
     * @return
     */
    protected boolean isSupportedChnl(MediaType mediaType){
        return getSupportedMediaTypes().contains(mediaType);
    }

    /**
     * DSReader를 생성하고 parameter로 넘어온 request를 각 chnl type의 TzRequest로 변환
     */
    public Object read(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException,
            HttpMessageNotReadableException{

        if(TzRequestIF.class.isAssignableFrom(clazz)){
            if(logger.isDebugEnabled()){
                logger.trace("[TZ] {} 메세지 컨버터를 이용하여 요청 데이터를 조회합니다. : Class={}",
                        new Object[] { getChnlType(), this.getClass() });
            }

            DSReader reader = TzRequestContextHolder.get().getDSReader();

            if(reader == null){
                TzRequestContextHolder.get().setHttpServletRequest(getHttpServletRequest(inputMessage));
                TzRequestContextHolder.get().setTzChnlType(getChnlType());

                if(logger.isDebugEnabled()){
                    logger.trace("[TZ] {} 데이터셋 리더를 생성합니다. : Class={}", new Object[] { getChnlType(), this.getClass() });
                }

                reader = getDSReader(TzRequestContextHolder.get().getHttpServletRequest());

                TzRequestContextHolder.get().setDSReader(reader);
            }

            //			TzRequestContextHolder.get().setHttpServletRequest(getHttpServletRequest(inputMessage));
            //			TzRequestContextHolder.get().setTzChnlType(getChnlType());
            //
            //			if(logger.isDebugEnabled()){
            //				logger.trace("[TZ] {} 데이터셋 리더를 생성합니다. : Class={}", new Object[] { getChnlType(), this.getClass() });
            //			}
            //
            //			DSReader reader = getDSReader(TzRequestContextHolder.get().getHttpServletRequest());
            //
            //			TzRequestContextHolder.get().setDSReader(reader);

            if(logger.isDebugEnabled()){
                logger.trace("[TZ] 파라미터 변환 작업을 수행합니다. : ChnlType={}, Class={}, ArguemntType={}", new Object[] {
                        getChnlType(), this.getClass(), clazz });
            }

            return getTzRequest(reader);
        }

        if(TzResponseIF.class.isAssignableFrom(clazz)){
            if(logger.isDebugEnabled()){
                logger.trace("[TZ] 파라미터 변환 작업을 수행합니다. : ChnlType={}, Class={}, ArguemntType={}", new Object[] {
                        getChnlType(), this.getClass(), clazz });
            }

            TzResponseIF tzResponse = getTzResponse();

            /*
             * 사용자 정의 객체를 처리하기 위한 컨버터 등록
             */
            ((TzDSAccessor)tzResponse).setDSConverter(dataSetConverter);

            return tzResponse;
        }

        return UNRESOLVED;
    }

    /**
     * DSWriter를 생성하고 dataSetMap을 얻어와서 outputStream으로 내보낸 후 바인딩된 ThreadLocal resource를 반환
     */
    public void write(Object accessor, MediaType contentType, HttpOutputMessage outputMessage) throws IOException,
            HttpMessageNotWritableException{
        if(logger.isDebugEnabled()){
            logger.trace("[TZ] {} 데이터셋 라이터를 생성합니다. : Class={}", new Object[] { getChnlType(), this.getClass() });
        }

        HttpServletRequest request = TzRequestContextHolder.get().getHttpServletRequest();
        HttpServletResponse response = getHttpServletResponse(outputMessage);

        DSWriter writer = getDSWriter(request, response, (TzDSAccessor)accessor);

        if(logger.isDebugEnabled()){
            logger.trace("[TZ] {} 메세지 컨버터를 이용하여 응답 데이터를 렌더링합니다. : Class={}",
                    new Object[] { getChnlType(), this.getClass() });
        }

        writer.write();

        if(logger.isDebugEnabled()){
            logger.trace("[TZ] {} 요청 처리를 위해 생성된 데이터를 초기화합니다. : Class={}",
                    new Object[] { getChnlType(), this.getClass() });
        }

        TzRequestContextHolder.clear();
    }

    protected HttpServletRequest getHttpServletRequest(HttpInputMessage inputMessage){
        ConfigurablePropertyAccessor propertyAccessor = PropertyAccessorFactory
                .forDirectFieldAccess((ServletServerHttpRequest)inputMessage);
        return (HttpServletRequest)propertyAccessor.getPropertyValue("servletRequest");
    }

    protected HttpServletResponse getHttpServletResponse(HttpOutputMessage outputMessage){
        ConfigurablePropertyAccessor propertyAccessor = PropertyAccessorFactory
                .forDirectFieldAccess((ServletServerHttpResponse)outputMessage);
        return (HttpServletResponse)propertyAccessor.getPropertyValue("servletResponse");
    }

    /**
     *
     * @return
     */
    protected abstract CoreChnls getChnlType();

    /**
     *
     */
    public abstract List<MediaType> getSupportedMediaTypes();

    /**
     *
     * @param reader
     * @return
     */
    protected abstract TzRequestIF getTzRequest(DSReader reader);

    /**
     *
     * @return
     */
    protected abstract TzResponseIF getTzResponse();

    /**
     *
     * @param request
     * @return
     */
    protected abstract DSReader getDSReader(HttpServletRequest request);

    /**
     *
     * @param request TODO
     * @param response
     * @param accessor
     * @return
     */
    protected abstract DSWriter getDSWriter(HttpServletRequest request, HttpServletResponse response,
            TzDSAccessor accessor);

}