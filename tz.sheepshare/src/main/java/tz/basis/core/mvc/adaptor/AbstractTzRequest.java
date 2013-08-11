package tz.basis.core.mvc.adaptor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import tz.basis.core.dataset.DS;
import tz.basis.core.dataset.GridDataImpl;
import tz.basis.core.exception.TzException;
import tz.basis.core.mvc.context.TzRequestContextHolder;
import tz.basis.core.mvc.file.TzPersistenceManager;
import tz.basis.data.GridData;
import tz.basis.upload.FileInfo;
import tz.basis.upload.MultiPartInfo;
import tz.basis.upload.handler.UploadEventHandler;

/**
 *
 * 요청값을 원하는 형태로 가공하여 반환한다.
 *
 * @author TZ
 */
public abstract class AbstractTzRequest implements TzRequestIF {

    protected Map<String, DS> dataSetMap = new HashMap<String, DS>();

    protected Map<String, Object> paramMap = new HashMap<String, Object>();

    protected List<FileInfo> attachments = new ArrayList<FileInfo>();

    public String getRequestURI(){
        return TzRequestContextHolder.get().getHttpServletRequest().getRequestURI();
    }

    public List<String> getDSIds(){
        return Collections.unmodifiableList(new ArrayList<String>(dataSetMap.keySet()));
    }

    public <E> E get(String datasetId, Class<E> clazz){
        return get(datasetId, 0, clazz, null);
    }

    public <E> E get(String datasetId, Class<E> clazz, String filter){
        return get(datasetId, 0, clazz, filter);
    }

    public <E> E get(String datasetId, int rowNum, Class<E> clazz){
        return get(datasetId, rowNum, clazz, null);
    }

    public <E> E get(String datasetId, int rowNum, Class<E> clazz, String filter){
        E bean = null;

        if(dataSetMap.get(datasetId).getRowCount() >= rowNum){
            bean = dataSetMap.get(datasetId).getBean(clazz, rowNum, filter);
        }
        return bean;
    }

    public Map<String, ?> getMap(String datasetId){
        return get(datasetId, 0, HashMap.class, null);
    }

    public Map<String, ?> getMap(String datasetId, String filter){
        return get(datasetId, 0, HashMap.class, filter);
    }

    public Map<String, ?> getMap(String datasetId, int rowNum){
        return get(datasetId, rowNum, HashMap.class);
    }

    public List<Map<String, String>> getMapList(String datasetId){
        return getMapList(datasetId, null);
    }

    public List<Map<String, String>> getMapList(String datasetId, String filter){
        DS dataSet = dataSetMap.get(datasetId);

        int rowCount = dataSet.getRowCount();

        List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();

        for(int i = 0; i < rowCount; ++i){
            mapList.add(dataSet.getBean(HashMap.class, i, filter));
        }

        return mapList;
    }

    public <E> GridData<E> getGridData(String datasetId, Class<E> clazz){
        return new GridDataImpl<E>(dataSetMap.get(datasetId), clazz, null);
    }

    public <E> GridData<E> getGridData(String datasetId, Class<E> clazz, String filter){
        return new GridDataImpl<E>(dataSetMap.get(datasetId), clazz, filter);
    }

    public List<String> getParameterNames(){
        return Collections.unmodifiableList(new ArrayList<String>(paramMap.keySet()));
    }

    public Map<String, Object> getParam(){
        Map<String, Object> params = new HashMap<String, Object>();
        params.putAll(paramMap);
        return params;
    }

    public <T> T getParam(Class<T> type){
        return getParam(type, null);
    }

    public <T> T getParam(Class<T> type, String filter){
        T object = BeanUtils.instantiate(type);
        Field[] fields = type.getDeclaredFields();

        for(Field field : fields){
            Object value = null;

            if(String[].class.isAssignableFrom(field.getType())){
                value = getParamArray(field.getName());
            }else{
                value = getParam(field.getName());
            }

            if(StringUtils.hasText(filter)){
                if(!isSelectedProperty(filter, field.getName())){
                    continue;
                }

                if(isRequiredProperty(filter, field.getName())){
                    if(value == null || (value instanceof String && !StringUtils.hasText((String)value))){
                        throw new TzException("Column[" + field.getName() + "] 는 요청[" + filter
                                + "] 에 의해 필수값으로 설정되었습니다.");
                    }
                }
            }

            field.setAccessible(true);

            try{
                if(value != null){
                    field.set(object, value);
                }
            }catch(Exception e){
                throw new TzException("[AbstractTzRequest] AbstractTzRequest - " + e.getMessage(), e);
            }
        }

        return object;
    }

    protected boolean isSelectedProperty(String expr, String property){
        boolean result = true;

        if(expr != null){
            result = expr.contains(property);
        }

        return result;
    }

    protected boolean isRequiredProperty(String expr, String property){
        return expr.contains(String.format("%s+", property));
    }

    public String getParam(String paramName){
        return getParam(paramName, "");
    }

    public String getParam(String paramName, String defaultValue){
        Object paramValue = paramMap.get(paramName);
        String retValue = null;

        if(paramValue != null){
            if(String[].class.isAssignableFrom(paramValue.getClass())){
                retValue = ((String[])paramValue)[0];
            }else
                retValue = (String)paramValue;
        }

        return !StringUtils.hasText(retValue) ? defaultValue : retValue;
    }

    public String[] getParamArray(String paramName){
        Object value = paramMap.get(paramName);

        if(value != null){
            if(String[].class.isAssignableFrom(value.getClass())){
                return (String[])value;
            }else{
                return new String[] { (String)value };
            }
        }

        return null;
    }

    @Deprecated
    public List<FileInfo> getAttachments(){
        return attachments;
    }

    public void handleIfMultipart(UploadEventHandler eventHandler, TzPersistenceManager persistence){

        if(eventHandler == null){
            throw new TzException("UploadEventHandler 가 정의되어 있지 않습니다.");
        }

        List<FileInfo> serverFileInfos = new ArrayList<FileInfo>();

        String serverFolder = eventHandler.getFolder(TzRequestContextHolder.get().getHttpServletRequest());

        eventHandler.prepareStorage(persistence, serverFolder);

        for(FileInfo fileInfo : attachments){
            if(fileInfo.getLength() > eventHandler.getMaxUploadSize()){
                throw new TzException("허용 가능한 파일 크기를 초과하였습니다. - maxsize={" + eventHandler.getMaxUploadSize()
                        + "} filesize={" + fileInfo.getLength() + "}");
            }

            String fileName = eventHandler.createFileNameIfAccepted(serverFolder, fileInfo);

            try{
                serverFileInfos.add(persistence.moveFile(fileInfo.getFolder(), fileInfo.getCallName(), serverFolder,
                        fileName));
            }catch(Exception e){
                throw new TzException("error moving uploaded file " + fileInfo.getCallName(), e);
            }finally{
                persistence.deleteFile(fileInfo.getFolder(), fileInfo.getCallName());
            }
        }

        eventHandler.postprocess(serverFolder, new MultiPartInfo(getParam(), serverFileInfos), persistence);
    }

}
