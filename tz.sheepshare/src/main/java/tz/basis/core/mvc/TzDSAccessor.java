package tz.basis.core.mvc;

import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

import tz.basis.core.dataset.DS;
import tz.basis.core.dataset.converter.DSConverter;
import tz.basis.upload.FileInfo;

/**
 *
 * response 에서 데이터를 추출하기 위한 interface
 * @author TZ
 *
 */
public interface TzDSAccessor {

    /**
     *
     * @return
     */
    Map<String, DS> getDSMap();

    /**
     *
     * @return
     */
    Map<String, String> getParams();

    /**
     *
     * @return
     */
    List<String> getSuccessMessags();

    /**
     *
     * @return
     */
    String getExceptionMessage();

    /**
     *
     * @param exceptionMessage
     */
    void setExceptionMessage(String exceptionMessage);

    /**
     *
     * @return
     */
    ModelAndView getModelAndView();

    /**
     *
     * @return
     */
    FileInfo getDownloadFile();

    /**
     *
     * @return
     */
    boolean isFileProcessing();

    /**
     *
     * @param converter
     */
    void setDSConverter(DSConverter converter);
}
