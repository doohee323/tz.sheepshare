package tz.basis.core.dataset;

import java.util.HashMap;
import java.util.Map;

import tz.basis.data.RowStatus;
import tz.basis.core.exception.TzException;

/**
*
* RowStatus와 data group를 가지는 class
*
* @author TZ
*/
public class DSRowImpl implements DSRow {

    private String rowStatus;

    private Map<String, Object> dataSetRowMap = new HashMap<String, Object>();

    /*
     * 변경전 RowData
     */
    private DSRow orgDSRow;

    /**
     * dataSetRowMap에 data add
     */
    public DSRow add(String fieldName, Object fieldValue){
        dataSetRowMap.put(fieldName, fieldValue);
        return this;
    }

    /**
     * dataSetRowMap에서 data 반환
     * @param fieldName : String
     */
    public Object get(String fieldName){
        Object value = dataSetRowMap.get(fieldName);

        if(value == null && value instanceof String){
            value = "";
        }

        return value;
    }

    /**
     * rowStatus 반환
     */
    public String getRowStatus(){
        return rowStatus;
    }

    /**
     * rowStatus 지정
     * @param rowStatus : String
     */
    public void setRowStatus(String rowStatus){
        RowStatus rst = RowStatus.valueOf(RowStatus.class, rowStatus.toUpperCase());

        if(rst == null){
            throw new TzException(String.format("지원하지 않는 RowStatus Type 입니다. - RowsStatus={%s}", rowStatus));
        }

        this.rowStatus = rowStatus;
    }

    public void setOrgDSRow(DSRow orgDSRow){
        this.orgDSRow = orgDSRow;
    }

    public DSRow getOrgDSRow(){
        return orgDSRow;
    }
}
