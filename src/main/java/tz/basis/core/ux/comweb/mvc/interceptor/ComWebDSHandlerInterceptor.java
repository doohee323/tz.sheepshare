package tz.basis.core.ux.comweb.mvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import tz.basis.core.CoreChnls;
import tz.basis.core.dataset.io.DSReader;
import tz.basis.core.dataset.io.DSWriter;
import tz.basis.core.mvc.TzDSAccessor;
import tz.basis.core.mvc.context.TzRequestContextHolder;
import tz.basis.core.mvc.interceptor.TzDSHandlerInterceptor;
import tz.basis.core.ux.comweb.dataset.ComWebDSReader;
import tz.basis.core.ux.comweb.dataset.ComWebDSWriter;
import tz.extend.util.StringUtil;

/**
 *
 * @author TZ
 *
 */
public class ComWebDSHandlerInterceptor extends TzDSHandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception{
        if(TzRequestContextHolder.get().getTzChnlType() == getChnlType()){
            ModelAndView comweb = getDSAccessor(request).getModelAndView();

            modelAndView.setViewName(comweb.getViewName());
            modelAndView.addAllObjects(comweb.getModelMap());

            if(getDSAccessor(request).isFileProcessing()){
                super.postHandle(request, response, handler, modelAndView);
            }
        }
    }

    @Override
    protected CoreChnls getChnlType(){
        return CoreChnls.COMWEB;
    }

    @Override
    protected DSReader getDSReader(HttpServletRequest request, HttpServletResponse response){
        return new ComWebDSReader(request, getFileOperator());
    }

    /**
     * 스트리밍 채널로 사용
     */
    @Override
    protected DSWriter getDSWriter(HttpServletRequest request, HttpServletResponse response,
            TzDSAccessor accessor){
        return new ComWebDSWriter(request, response, getFileOperator(), accessor);
    }

    /**
     * 
     */
    @Override
    protected boolean checkTzRequest(HttpServletRequest request){
        String contentType = request.getHeader("Content-Type");
        String agent = request.getHeader("User-Agent");
        String url = request.getRequestURI();

        if(agent != null && (agent.indexOf("XPLATFORM") != -1)){
            return false;
        } else if (StringUtil.getText(contentType).indexOf("application/json") > -1 || url.endsWith(".ajax")) {
            return false;
        }
        return true;
    }
}
