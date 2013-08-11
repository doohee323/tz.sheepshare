package tz.basis.core.dataset;

/**
*
* @author TZ
*
*/
public interface DSRow {

    DSRow add(String fieldName, Object fieldValue);

    Object get(String fieldName);

    String getRowStatus();

    void setRowStatus(String rowStatus);

    void setOrgDSRow(DSRow savedDSRow);

    DSRow getOrgDSRow();
}