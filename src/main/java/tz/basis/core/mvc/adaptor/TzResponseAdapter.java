package tz.basis.core.mvc.adaptor;

import java.util.List;
import java.util.Map;

import tz.basis.upload.FileInfo;
import tz.basis.upload.handler.DownloadEventHandler;

// @Component
public class TzResponseAdapter implements TzResponseIF {

    //	@Autowired
    public TzResponseAdapter() {}

    private TzResponseIF response;

    //	@Autowired
    public TzResponseAdapter(TzResponseIF response) {
        this.response = response;
    }

    public <E> void set(String datasetId, E bean){
        this.response.set(datasetId, bean);
    }

    public <E> void set(String datasetId, E bean, Class<E> type){
        this.response.set(datasetId, bean, type);
    }

    public <E> void setList(String datasetId, List<E> listOfModel){
        this.response.setList(datasetId, listOfModel);
    }

    public <E> void setList(String datasetId, List<E> listOfModel, Class<E> type){
        this.response.setList(datasetId, listOfModel, type);
    }

    public void setMapList(String datasetId, List<? extends Map<String, ?>> mapList){
        this.response.setMapList(datasetId, mapList);
    }

    public void setMap(String datasetId, Map<String, ?> map){
        this.response.setMap(datasetId, map);
    }

    public void addSuccessMessage(String message){
        this.response.addSuccessMessage(message);
    }

    public void addParam(String paramName, String paramValue){
        this.response.addParam(paramName, paramValue);
    }

    public void setViewName(String viewName){
        this.response.setViewName(viewName);
    }

    public void setDownloadFile(FileInfo fileInfo){
        this.response.setDownloadFile(fileInfo);
    }

    public void setDownloadFile(DownloadEventHandler eventHandler, FileInfo fileInfo){
        this.response.setDownloadFile(eventHandler, fileInfo);
    }
}