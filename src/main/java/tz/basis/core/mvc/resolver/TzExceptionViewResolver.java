package tz.basis.core.mvc.resolver;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import tz.basis.core.exception.CoreBizException;
import tz.basis.core.mvc.TzDSAccessor;
import tz.basis.core.mvc.TzExceptionMessageWriter;
import tz.basis.core.mvc.context.TzRequestContextHolder;
import tz.basis.core.mvc.handler.TzExceptionTraceHandler;
import tz.basis.core.mvc.view.TzView;

/**
 *
 * 시스템에서 발생한 예외를 통합하여 처리한다.
 *
 * @author TZ
 *
 */
public class TzExceptionViewResolver implements HandlerExceptionResolver , Ordered {

    private static final Logger logger = LoggerFactory.getLogger(TzExceptionViewResolver.class);

    @Autowired(required = false)
    private MessageSourceAccessor messageSourceAccessor;

    @Autowired(required = false)
    private TzExceptionTraceHandler traceHandler;

    private List<TzExceptionMessageWriter> exceptionMessageWriters;

    private TzExceptionMessageWriter defaultExceptionMessageWriter = new TzExceptionMessageWriter() {

        public String buildExceptionMessage(MessageSourceAccessor messageSourceAccessor, Exception exception){
            String code = "";
            String defaultMessage = "";
            Object[] arguments = null;
            String message = exception.getMessage();

            if(exception instanceof CoreBizException){
                code = ((CoreBizException)exception).getCode();
                defaultMessage = ((CoreBizException)exception).getDefaultMessage();
                arguments = ((CoreBizException)exception).getArgs();
            }

            if(messageSourceAccessor != null){
                try{
                    message = messageSourceAccessor.getMessage(code, arguments);
                }catch(Exception mex){
                    message = defaultMessage;
                }

                if(!StringUtils.hasText(message)){
                    message = exception.getMessage();
                }
            }

            return message;
        }

        public boolean accept(Exception exception){
            return true;
        }
    };

    private HttpStatus exceptionStatusCode = HttpStatus.METHOD_FAILURE;

    private int order = Integer.MAX_VALUE - 1000;

    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex){

        if(logger.isDebugEnabled()){
            logger.debug(ex.getMessage());
            ex.printStackTrace();
        }

        if(ex.getClass().isAnnotationPresent(ResponseStatus.class)){
            response.setStatus(ex.getClass().getAnnotation(ResponseStatus.class).value().value());
        }else{
            response.setStatus(exceptionStatusCode.value());
        }

        TzDSAccessor tzDSAccesor = TzRequestContextHolder.get().getDSAccessor();

        if(!TzRequestContextHolder.get().isTzRequest()){
            return null;
        }

        tzDSAccesor.setExceptionMessage(buildExceptionMessage(messageSourceAccessor, ex));

        TzRequestContextHolder.get().getDSWriter().setDSAccessor(tzDSAccesor);

        /*
         * 예외를 이용한 후처리 (ex. 이력처리등..)
         */
        if(traceHandler != null){
            traceHandler.handler(request, response, ex);
        }

        return new ModelAndView(new TzView());
    }

    /**
     *
     * 발생한 예외에 대한 적절한 예외 메세지를 생성하여 반환한다.
     *
     * @param ex
     * @return
     */
    private String buildExceptionMessage(MessageSourceAccessor messageSourceAccessor, Exception ex){
        TzExceptionMessageWriter exceptionWriter = defaultExceptionMessageWriter;

        if(exceptionMessageWriters != null){
            for(TzExceptionMessageWriter w : exceptionMessageWriters){
                if(w.accept(ex)){
                    exceptionWriter = w;
                    break;
                }
            }
        }

        String exceptionMessage = exceptionWriter.buildExceptionMessage(messageSourceAccessor, ex);

        logger.debug(exceptionMessage);
        return exceptionMessage;
    }

    public int getOrder(){
        return order;
    }

    public void setOrder(int order){
        this.order = order;
    }

    public void setExceptionStatusCode(HttpStatus exceptionStatusCode){
        this.exceptionStatusCode = exceptionStatusCode;
    }

    public void setExceptionMessageWriters(List<TzExceptionMessageWriter> exceptionMessageWriters){
        this.exceptionMessageWriters = exceptionMessageWriters;
    }

    public void setDefaultExceptionMessageWriter(TzExceptionMessageWriter defaultExceptionMessageWriter){
        this.defaultExceptionMessageWriter = defaultExceptionMessageWriter;
    }
}
