package tz.basis.core.ux.comweb.dataset;

import java.util.List;

import tz.basis.core.dataset.DSColumn;
import tz.basis.core.dataset.DSImpl;
import tz.basis.core.dataset.DSRow;

/**
 *
 * @author TZ
 *
 */
public class ComWebDS extends DSImpl {

    public ComWebDS(String datasetId) {
        super(datasetId, null, null);
    }

    public ComWebDS(String dataSetId, List<DSColumn> dataSetColumns, List<DSRow> dataSetRows) {
        super(dataSetId, dataSetColumns, dataSetRows);
    }
}
