package tz.basis.core.ux.comweb.dataset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tz.basis.core.dataset.io.DSWriter;
import tz.basis.core.mvc.TzDSAccessor;
import tz.basis.core.mvc.file.operator.FileOperator;

/**
 *
 * {@link DSWriter} 의 표준웹 구현체
 *
 * @author TZ
 *
 */
public class ComWebDSWriter implements DSWriter {

    private HttpServletRequest request;

    private HttpServletResponse response;

    private FileOperator fileOperator;

    private TzDSAccessor accessor;

    public ComWebDSWriter(HttpServletRequest request, HttpServletResponse response, FileOperator fileOperator,
            TzDSAccessor accessor) {
        this.request = request;
        this.response = response;
        this.fileOperator = fileOperator;
        this.accessor = accessor;
    }

    /**
     * {@inheritDoc}
     */
    public void write(){
        if(accessor != null && accessor.isFileProcessing()){
            fileOperator.sendFileStream(request, response, accessor.getDownloadFile());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setDSAccessor(TzDSAccessor accessor){
        this.accessor = accessor;
    }
}
