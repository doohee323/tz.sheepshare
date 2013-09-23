package tz.basis.core.mvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import tz.basis.core.CoreChnls;
import tz.basis.core.dataset.io.DSReader;
import tz.basis.core.dataset.io.DSWriter;
import tz.basis.core.mvc.TzDSAccessor;
import tz.basis.core.mvc.context.TzRequestContextHolder;
import tz.basis.core.mvc.file.TzFileHandler;
import tz.basis.core.mvc.file.operator.FileOperator;

/**
 * 컨트롤러를 호출하기 전과 후에 request, response 를 참조하거나 가공할 수 있다.<br/>
 *
 * @author TZ
 */
public abstract class TzDSHandlerInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(TzDSHandlerInterceptor.class);

    @Autowired(required = false)
    protected TzFileHandler fileHandler;

    protected boolean isRestMode = false;

    /**
     *
     * @param request
     * @param response
     * @param handler
     *
     * 컨트롤러를 호출하기 전, parameter 로 넘어온 request, response를 참조하여<br/>
     * ChnlType에 따라 DSReader를 생성하고 TzRequestContext에 DSReader, ChannerType 정보를 설정한다.
     * @return Boolean
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{

        TzRequestContextHolder.get().setHttpServletRequest(request);
        TzRequestContextHolder.get().setHttpServletResponse(response);

        if(TzRequestContextHolder.get().getTzChnlType() == null && checkTzRequest(request)){
            if(logger.isDebugEnabled()){
                logger.trace("[TZ] {} 데이터셋 리더를 생성합니다. : Class={}", new Object[] { getChnlType(), this.getClass() });
            }

            TzRequestContextHolder.get().setTzRequest(checkTzRequest(request));
            TzRequestContextHolder.get().setDSReader(getDSReader(request, response));

            /**
             *  
             */
            TzRequestContextHolder.get().setDSWriter(getDSWriter(request, response, null));
            TzRequestContextHolder.get().setTzChnlType(getChnlType());
        }

        return true;
    }

    /**
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * TzRequestContext 리소스를 반환한다.<br/>
     */
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception{
        if(TzRequestContextHolder.get().isTzRequest()
                && TzRequestContextHolder.get().getTzChnlType() == getChnlType()){
            if(logger.isDebugEnabled()){
                logger.trace("[TZ] {} 요청 처리를 위해 생성된 데이터를 초기화합니다. : Class={}",
                        new Object[] { getChnlType(), this.getClass() });
            }

            TzRequestContextHolder.clear();
        }
    }

    /**
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     *
     * 컨트롤러가 실행되고 난 후의 후처리 작업을 진행한다.<br/>
     * TzRequestContext에 설정된 ChnlType에 따라 DSWriter를 생성한다.<br/>
     * preHandle() method에서 false를 return했을 경우 실행되지 않는다.
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception{
        /*
         * HandlerInterceptor 과 HttpMessageConverter 가 동시에 적용되었을 경우, 그 실행순서는..
         *
         * 1. HandlerInterceptor.preHandler
         * 2. HttpMessageConverter.read
         * 3. HttpMessageConverter.write
         * 4. HandlerInterceptor.postHanlder
         * 5. HandlerInterceptor.afterCompletion
         *
         * 의 순서로 동작한다.
         *
         */
        if(TzRequestContextHolder.get().isTzRequest()
                && TzRequestContextHolder.get().getTzChnlType() == getChnlType()){
            if(logger.isDebugEnabled()){
                logger.trace("[TZ] {} 데이터셋 라이터를 생성합니다. : Class={}", new Object[] { getChnlType(), this.getClass() });
            }

            TzDSAccessor accessor = getDSAccessor(request);

            if(accessor.isFileProcessing()){
                modelAndView.setViewName("AAA");
            }

            TzRequestContextHolder.get().setDSWriter(getDSWriter(request, response, accessor));
        }
    }

    public void setIsRestMode(boolean isRestMode){
        this.isRestMode = isRestMode;
    }

    /**
     *
     * 파일처리를 위한 핸들러를 등록한다.
     *
     * @param fileHandler
     */
    public void setFileHandler(TzFileHandler fileHandler){
        this.fileHandler = fileHandler;
    }

    protected FileOperator getFileOperator(){
        return fileHandler == null ? null : fileHandler.getFileOperator(getChnlType());
    }

    /**
     *
     * @param request
     * @return TzDSAccessor
     */
    protected TzDSAccessor getDSAccessor(HttpServletRequest request){
        return TzRequestContextHolder.get().getDSAccessor();
    }

    /**
     *
     * @return CoreChnls
     */
    protected abstract CoreChnls getChnlType();

    /**
     *
     * @param request
     * @param response
     * @return DSReader
     */
    protected abstract DSReader getDSReader(HttpServletRequest request, HttpServletResponse response);

    /**
     *
     * @param request
     * @param response
     * @param accessor
     * @return DSWriter
     */
    protected abstract DSWriter getDSWriter(HttpServletRequest request, HttpServletResponse response,
            TzDSAccessor accessor);

    /**
     *
     * parameter와 header를 읽어 해당 chnl type의 TzRequest인지 여부를 Boolean 값으로 반환
     *
     * @param request
     * @return Boolean
     */
    protected abstract boolean checkTzRequest(HttpServletRequest request);
}
