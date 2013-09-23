package tz.basis.core.mvc.resolver;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import tz.basis.core.mvc.context.TzRequestContextHolder;
import tz.basis.core.mvc.view.TzView;

public class TzViewResolver implements ViewResolver , Ordered {

    private static final Logger logger = LoggerFactory.getLogger(TzViewResolver.class);

    private Map<String, View> viewMap;

    private View defaultView;

    private int order;

    private InternalResourceViewResolver pageViewResolver;

    public TzViewResolver() {
        this.viewMap = new HashMap();
        this.viewMap.put("_STREAMING", new TzView());
    }

    public View resolveViewName(String viewName, Locale locale) throws Exception{
        View view = null;

        if(this.viewMap != null){
            view = (View)this.viewMap.get(viewName);
        }

        if(view == null){
            if(TzRequestContextHolder.get().isTzRequest()){
                ModelAndView modelAndView = TzRequestContextHolder.get().getDSAccessor().getModelAndView();

                if((modelAndView == null) || (modelAndView.getViewName() == null))
                    view = this.defaultView;
                else{
                    view = this.pageViewResolver.resolveViewName(modelAndView.getViewName(), locale);
                }
            }else if(this.pageViewResolver != null){
                view = this.pageViewResolver.resolveViewName(viewName, locale);
            }

        }

        if(logger.isDebugEnabled()){
            logger.trace("[TZ] {} 뷰생성 작업을 수행합니다. : ViewClass={}, ViewName={}", new Object[] {
                    TzRequestContextHolder.get().getTzChnlType(), view.getClass(), viewName });
        }

        return view;
    }

    public int getOrder(){
        return this.order;
    }

    public void setOrder(int order){
        this.order = order;
    }

    public void setDefaultView(View defaultView){
        this.defaultView = defaultView;
    }

    public void setViewMap(Map<String, View> viewMap){
        this.viewMap = viewMap;
    }

    public void setPageViewResolver(InternalResourceViewResolver pageViewResolver){
        this.pageViewResolver = pageViewResolver;
    }
}