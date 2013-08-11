package tz.basis.core.dataset.io;

import java.util.List;
import java.util.Map;

import tz.basis.core.dataset.DSColumn;
import tz.basis.core.dataset.DSRow;
import tz.basis.upload.FileInfo;

/**
 *
 * @author TZ
 *
 * HTTP request 에서 data 추출
 *
 */
public interface DSReader {

    /**
     * [GET] Parameter (Variable) 조회
     *
     * @return
     */
    public Map<String, Object> getParamMap();

    /**
     *
     * DS ID 리스트 조회
     *
     * @return
     */
    List<String> getDSIdList();

    /**
     * DSRow list 조회
     * @param dataSetId
     * @return
     */
    List<DSRow> getDSRows(String dataSetId);

    /**
     * DSColumn list 조회
     * @param dataSetId
     * @return
     */
    List<DSColumn> getDSColumns(String dataSetId);

    /**
     * 첨부파일 조회
     *
     * @return
     */
    List<FileInfo> getAttachments();

}