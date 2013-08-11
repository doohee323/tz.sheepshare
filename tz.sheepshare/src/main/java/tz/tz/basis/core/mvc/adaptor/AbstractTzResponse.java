package tz.basis.core.mvc.adaptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import tz.basis.core.CoreChnls;
import tz.basis.core.dataset.DS;
import tz.basis.core.dataset.converter.DSConverter;
import tz.basis.core.exception.TzException;
import tz.basis.core.mvc.TzDSAccessor;
import tz.basis.core.mvc.context.TzRequestContextHolder;
import tz.basis.upload.FileInfo;
import tz.basis.upload.handler.DownloadEventHandler;

import org.springframework.web.servlet.ModelAndView;

public abstract class AbstractTzResponse implements TzResponseIF , TzDSAccessor {

    protected Map<String, DS> dataSetMap = new HashMap();

    protected Map<String, String> params = new HashMap();

    protected List<String> successMessages = new ArrayList();

    protected String exceptionMessage;

    protected FileInfo fileInfo;

    protected DSConverter dataSetConverter;

    public Map<String, DS> getDSMap(){
        return this.dataSetMap;
    }

    public void addSuccessMessage(String message){
        this.successMessages.add(message);
    }

    public List<String> getSuccessMessags(){
        return this.successMessages;
    }

    public void setExceptionMessage(String exceptionMessage){
        this.exceptionMessage = exceptionMessage;
    }

    public String getExceptionMessage(){
        return this.exceptionMessage;
    }

    public void setViewName(String viewName){
//        throw new UnsupportedOperationException("지원하지 않는 연산");
    }

    public ModelAndView getModelAndView(){
        return null;
    }

    public <E> void set(String datasetId, E bean){
        set(datasetId, bean, null);
    }

    public <E> void set(String datasetId, E bean, Class<E> type){
        if((bean != null) && (Collection.class.isAssignableFrom(bean.getClass()))){
            throw new TzException("DSId-" + datasetId + "는 Collection Type 입니다. - setList 를 사용하십시오.");
        }

        DS dataSet = createUxDS(datasetId);

        if((this.dataSetConverter != null) && (this.dataSetConverter.support(bean))){
            this.dataSetConverter.convert(dataSet, bean);
        }else if((bean != null) && (Map.class.isAssignableFrom(bean.getClass())))
            dataSet.addRowMap((Map)bean, type);
        else{
            dataSet.addRowBean(bean, type);
        }

        addDS(datasetId, dataSet);
    }

    public <E> void setList(String datasetId, List<E> listOfModel){
        setList(datasetId, listOfModel, null);
    }

    public void addParam(String paramName, String paramValue){
        this.params.put(paramName, paramValue);
    }

    public Map<String, String> getParams(){
        return this.params;
    }

    public <E> void setList(String datasetId, List<E> listOfModel, Class<E> type){
        DS dataSet = createUxDS(datasetId);

        dataSet.addRowBean(null, type);

        if(listOfModel != null){
            for(Iterator i$ = listOfModel.iterator(); i$.hasNext();){
                Object bean = i$.next();
                if(Map.class.isAssignableFrom(bean.getClass()))
                    dataSet.addRowMap((Map)bean, type);
                else{
                    dataSet.addRowBean(bean, type);
                }
            }

            addDS(datasetId, dataSet);
        }
    }

    @Deprecated
    public void setMap(String datasetId, Map<String, ?> map){
        DS dataSet = createUxDS(datasetId);

        dataSet.addRowMap(map, null);

        addDS(datasetId, dataSet);
    }

    @Deprecated
    public void setMapList(String datasetId, List<? extends Map<String, ?>> mapList){
        DS dataSet = createUxDS(datasetId);

        for(Map map : mapList){
            dataSet.addRowMap(map, null);
        }

        addDS(datasetId, dataSet);
    }

    public void setDownStreamChnl(CoreChnls chnl){
        TzRequestContextHolder.get().setTzChnlType(chnl);
    }

    protected void addDS(String dataSetId, DS dataSet){
        this.dataSetMap.put(dataSetId, dataSet);
    }

    public FileInfo getDownloadFile(){
        return this.fileInfo;
    }

    public void setDownloadFile(FileInfo fileInfo){
        this.fileInfo = fileInfo;
    }

    public void setDownloadFile(DownloadEventHandler eventHandler, FileInfo fileInfo){
        if(eventHandler != null){
            eventHandler.preprocess(fileInfo);
        }

        this.fileInfo = fileInfo;
    }

    public boolean isFileProcessing(){
        return this.fileInfo != null;
    }

    public void setDSConverter(DSConverter dataSetConverter){
        this.dataSetConverter = dataSetConverter;
    }

    public abstract DS createUxDS(String paramString);
}