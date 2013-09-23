package tz.basis.core.mvc.adaptor;

import java.util.List;
import java.util.Map;

import tz.basis.data.GridData;
import tz.basis.core.mvc.file.TzPersistenceManager;
import tz.basis.upload.FileInfo;
import tz.basis.upload.handler.UploadEventHandler;

/**
 * @author TZ
 *
 */
public interface TzRequestIF {

    /**
     *
     * @return
     */
    String getRequestURI();

    /**
     *
     * @return
     */
    List<String> getDSIds();

    /**
     * @param <E>
     * @param datasetId DATASET ID
     * @param clazz     Return Type
     * @return UserDefinedModel
     */
    <E> E get(String datasetId, Class<E> clazz);

    /**
     *
     * @param <E>
     * @param datasetId DATASET ID
     * @param clazz     Return Type
     * @param filter    유효성 체크를 위한 Expression (ex. id+,name,description : id=required, name, description 조회)
     * @return
     */
    <E> E get(String datasetId, Class<E> clazz, String filter);

    /**
     * @param <E>
     * @param datasetId
     * @param rowNum
     * @param clazz
     * @return UserDefinedModel
     */
    <E> E get(String datasetId, int rowNum, Class<E> clazz);

    /**
     *
     * @param <E>
     * @param datasetId DATASET ID
     * @param rowNum    rowNum번째 행의 데이터를 반환
     * @param clazz     Return Type
     * @param filter    유효성 체크를 위한 Expression (ex. id+,name,description : id=required, name, description 조회)
     * @return
     */
    <E> E get(String datasetId, int rowNum, Class<E> clazz, String filter);

    /**
     * @param <E>
     * @param datasetId
     * @param clazz
     * @return Map
     */
    <E> GridData<E> getGridData(String datasetId, Class<E> clazz);

    /**
     *
     * @param <E>
     * @param datasetId DATASET ID
     * @param clazz     Return Type
     * @param filter    유효성 체크를 위한 Expression (ex. id+,name,description : id=required, name, description 조회)
     * @return
     */
    <E> GridData<E> getGridData(String datasetId, Class<E> clazz, String filter);

    /**
     * @param datasetId
     * @return Map
     */
    Map<String, ?> getMap(String datasetId);

    /**
     *
     * @param datasetId DATASET ID
     * @param filter    유효성 체크를 위한 Expression (ex. id+,name,description : id=required, name, description 조회)
     * @return
     */
    Map<String, ?> getMap(String datasetId, String filter);

    /**
     * @param datasetId
     * @param rowNum
     * @return Map
     */
    @Deprecated
    Map<String, ?> getMap(String datasetId, int rowNum);

    /**
     * @param datasetId
     * @return
     */
    List<Map<String, String>> getMapList(String datasetId);

    /**
     *
     * @param datasetId DATASET ID
     * @param filter    유효성 체크를 위한 Expression (ex. id+,name,description : id=required, name, description 조회)
     * @return
     */
    List<Map<String, String>> getMapList(String datasetId, String filter);

    /**
     *
     * @return
     */
    List<String> getParameterNames();

    /**
     * @return Map
     */
    Map<String, Object> getParam();

    /**
     *
     * @param <T>
     * @param type
     * @return
     */
    <T> T getParam(Class<T> type);

    /**
     *
     * @param <T>
     * @param type   Return Type
     * @param filter 유효성 체크를 위한 Expression (ex. id+,name,description : id=required, name, description 조회)
     * @return
     */
    <T> T getParam(Class<T> type, String filter);

    /**
     *
     * @param paramName
     * @return
     */
    String getParam(String paramName);

    /**
     *
     * @param paramName
     * @param defaultValue paramName으로 조회된 값이 null 이거나 empty string인 경우 defaultValue로 치환되어 반환됨.
     * @return
     */
    String getParam(String paramName, String defaultValue);

    /**
     *
     * @param paramName
     * @return
     */
    String[] getParamArray(String paramName);

    /**
     *
     * 요청에 포함된 첨부파일을 반환한다.
     *
     * @return
     */
    @Deprecated
    List<FileInfo> getAttachments();

    /**
     *
     * @param dispatcher
     * @param persistence
     */
    void handleIfMultipart(UploadEventHandler dispatcher, TzPersistenceManager persistence);

}
