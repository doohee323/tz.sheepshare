package tz.basis.core.mvc.adaptor;

import java.util.List;
import java.util.Map;

import tz.basis.core.mvc.file.TzPersistenceManager;
import tz.basis.data.GridData;
import tz.basis.upload.FileInfo;
import tz.basis.upload.handler.UploadEventHandler;

// @Component
public class TzRequestAdapter implements TzRequestIF {

    protected TzRequestIF request;

    //	@Autowired
    public TzRequestAdapter() {}

    //	@Autowired
    public TzRequestAdapter(TzRequestIF request) {
        this.request = request;
    }

    public String getRequestURI(){
        return this.request.getRequestURI();
    }

    public List<String> getDSIds(){
        return this.request.getDSIds();
    }

    public <E> E get(String datasetId, Class<E> clazz){
        return this.request.get(datasetId, clazz);
    }

    public <E> E get(String datasetId, Class<E> clazz, String filter){
        return this.request.get(datasetId, clazz, filter);
    }

    public <E> E get(String datasetId, int rowNum, Class<E> clazz){
        return this.request.get(datasetId, rowNum, clazz);
    }

    public <E> E get(String datasetId, int rowNum, Class<E> clazz, String filter){
        return this.request.get(datasetId, rowNum, clazz, filter);
    }

    public <E> GridData<E> getGridData(String datasetId, Class<E> clazz){
        return this.request.getGridData(datasetId, clazz);
    }

    public <E> GridData<E> getGridData(String datasetId, Class<E> clazz, String filter){
        return this.request.getGridData(datasetId, clazz, filter);
    }

    public Map<String, ?> getMap(String datasetId){
        return this.request.getMap(datasetId);
    }

    public Map<String, ?> getMap(String datasetId, String filter){
        return this.request.getMap(datasetId, filter);
    }

    public Map<String, ?> getMap(String datasetId, int rowNum){
        return this.request.getMap(datasetId, rowNum);
    }

    public List<Map<String, String>> getMapList(String datasetId){
        return this.request.getMapList(datasetId);
    }

    public List<Map<String, String>> getMapList(String datasetId, String filter){
        return this.request.getMapList(datasetId, filter);
    }

    public List<String> getParameterNames(){
        return this.request.getParameterNames();
    }

    public Map<String, Object> getParam(){
        return this.request.getParam();
    }

    public <T> T getParam(Class<T> type){
        return this.request.getParam(type);
    }

    public <T> T getParam(Class<T> type, String filter){
        return this.request.getParam(type, filter);
    }

    public String getParam(String paramName){
        return this.request.getParam(paramName);
    }

    public String getParam(String paramName, String defaultValue){
        return this.request.getParam(paramName, defaultValue);
    }

    public String[] getParamArray(String paramName){
        return this.request.getParamArray(paramName);
    }

    public List<FileInfo> getAttachments(){
        return this.request.getAttachments();
    }

    public void handleIfMultipart(UploadEventHandler dispatcher, TzPersistenceManager persistence){
        this.request.handleIfMultipart(dispatcher, persistence);
    }
}