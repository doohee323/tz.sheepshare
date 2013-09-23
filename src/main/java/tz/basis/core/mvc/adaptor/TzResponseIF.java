package tz.basis.core.mvc.adaptor;

import java.util.List;
import java.util.Map;

import tz.basis.upload.FileInfo;
import tz.basis.upload.handler.DownloadEventHandler;

/**
 * @author TZ
 *
 */
public interface TzResponseIF {

    /**
     * @param <E>
     * @param datasetId
     * @param bean
     */
    <E> void set(String datasetId, E bean);

    /**
     *
     * @param <E>
     * @param datasetId
     * @param bean
     * @param type
     */
    <E> void set(String datasetId, E bean, Class<E> type);

    /**
     * @param <E>
     * @param datasetId
     * @param listOfModel
     */
    <E> void setList(String datasetId, List<E> listOfModel);

    /**
     *
     * @param <E>
     * @param datasetId
     * @param listOfModel
     * @param type
     */
    <E> void setList(String datasetId, List<E> listOfModel, Class<E> type);

    /**
     * @param datasetId
     * @param mapList
     */
    void setMapList(String datasetId, List<? extends Map<String, ?>> mapList);

    /**
     * @param datasetId
     * @param map
     */
    void setMap(String datasetId, Map<String, ?> map);

    /**
     *
     * @param message
     */
    void addSuccessMessage(String message);

    /**
     *
     * @param paramName
     * @param paramValue
     */
    void addParam(String paramName, String paramValue);

    /**
     *
     * @param viewName
     */
    void setViewName(String viewName);
    
	void setDownloadFile(FileInfo fileInfo);

    void setDownloadFile(DownloadEventHandler eventHandler, FileInfo fileInfo);
    
}
