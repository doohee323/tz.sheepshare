package tz.basis.core.ux.json.mvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tz.basis.core.dataset.DS;
import tz.basis.core.mvc.adaptor.AbstractTzResponse;
import tz.basis.core.ux.json.dataset.JsonDS;

public class JsonResponse extends AbstractTzResponse {

	private Map<String, Boolean> srcDataTypeMap = new HashMap<String, Boolean>();

	@Override
	public DS createUxDS(String datasetId) {
		return new JsonDS(datasetId);
	}

	public <E> void set(String datasetId, E bean) {
		super.set(datasetId, bean);
		srcDataTypeMap.put(datasetId, false);
	}

	public <E> void setList(String datasetId, List<E> listOfModel) {
		super.setList(datasetId, listOfModel);
		srcDataTypeMap.put(datasetId, true);
	}

	public boolean isFromCollection(String datasetId)	{
		return srcDataTypeMap.get(datasetId);
	}
}
