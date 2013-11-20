package tz.basis.core.ux.json.mvc;

import java.util.List;

import tz.basis.core.mvc.adaptor.AbstractTzRequest;
import tz.basis.core.ux.json.dataset.JsonDS;
import tz.basis.core.ux.json.dataset.JsonDSReader;

/**
 *
 * @author TZ
 *
 */
public final class JsonRequest extends AbstractTzRequest {

	public JsonRequest(JsonDSReader reader) {
		List<String> dataSetIdList = reader.getDSIdList();

		for (String dataSetId : dataSetIdList) {
			dataSetMap.put(dataSetId, new JsonDS(dataSetId, reader.getDSColumns(dataSetId), reader.getDSRows(dataSetId)));
		}

		paramMap = reader.getParamMap();
	}
}
