package tz.basis.core.ux.comweb.mvc;

import java.util.List;

import org.springframework.web.servlet.ModelAndView;

import tz.basis.core.dataset.DS;
import tz.basis.core.mvc.adaptor.AbstractTzResponse;
import tz.basis.core.ux.comweb.dataset.ComWebDS;

/**
 *
 * @author TZ
 *
 */
public class ComWebResponse extends AbstractTzResponse {

    private ModelAndView modelAndView = new ModelAndView();

    @Override
    public void setViewName(String viewName){
        modelAndView.setViewName(viewName);
    }

    @Override
    public ModelAndView getModelAndView(){
        return modelAndView;
    }

    @Override
    public <E> void set(String datasetId, E bean){
        super.set(datasetId, bean);
        modelAndView.addObject(datasetId, bean);
    }

    @Override
    public <E> void setList(String datasetId, List<E> listOfModel){
        super.setList(datasetId, listOfModel);
        modelAndView.addObject(datasetId, listOfModel);
    }

    @Override
    public DS createUxDS(String datasetId){
        return new ComWebDS(datasetId);
    }
}
