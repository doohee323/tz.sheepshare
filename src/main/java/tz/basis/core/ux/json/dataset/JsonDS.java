package tz.basis.core.ux.json.dataset;

import java.util.List;

import tz.basis.core.dataset.DSColumn;
import tz.basis.core.dataset.DSImpl;
import tz.basis.core.dataset.DSRow;

/**
 *
 * @author TZ
 *
 */
public final class JsonDS extends DSImpl {

	public JsonDS(String dataSetId) {
		super(dataSetId, null, null);
	}

	public JsonDS(String dataSetId, List<DSColumn> cols, List<DSRow> rows) {
		super(dataSetId, cols, rows);
	}
}
