package tz.basis.core.ux.json.dataset;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.springframework.util.StringUtils;

import tz.basis.core.dataset.DS;
import tz.basis.core.dataset.io.DSWriter;
import tz.basis.core.exception.TzException;
import tz.basis.core.mvc.TzDSAccessor;

/**
 *
 * @author TZ
 *
 */
public final class JsonDSWriter implements DSWriter {

	private ObjectMapper mapper = new ObjectMapper();

	private HttpServletResponse response;
	private TzDSAccessor accessor;

	private String contentType = "application/json";
	private String charset = "utf-8";

	public JsonDSWriter(HttpServletResponse response, TzDSAccessor accessor) {
		this.response = response;
		this.accessor = accessor;
		this.mapper.getSerializationConfig().setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		this.mapper.getSerializationConfig().set(Feature.FAIL_ON_EMPTY_BEANS, false);
	}

	public void write() {
		response.setContentType(contentType);
		response.setCharacterEncoding(charset);

		try {
			JsonGenerator jsonGenerator = mapper.getJsonFactory().createJsonGenerator(response.getOutputStream(), JsonEncoding.UTF8);
			mapper.writeValue(jsonGenerator, getJsonMapObject(accessor.getDSMap(), accessor.getSuccessMessags(), accessor.getExceptionMessage()));
		} catch (Exception e) {
			throw new TzException("[JsonDSWriter] write - "
					+ e.getMessage(), e);
		}
	}

	private Map<String, Object> getJsonMapObject(Map<String, DS> dataSetMap, List<String> successMessages, String exceptionMessage) {
		Iterator<String> dataSetIds = dataSetMap.keySet().iterator();

		Map<String, Object> map = new HashMap<String, Object>();

		while (dataSetIds.hasNext()) {
			String dataSetId = dataSetIds.next();

			DS dataSet = dataSetMap.get(dataSetId);

//			boolean isFromCollection = ((JsonResponse) accessor).isFromCollection(dataSetId);

			int rowCount = dataSet.getRowCount();

			Object object = null;

			if(rowCount > 0)	{
//				if(isFromCollection){
					object = new HashMap[rowCount];

					for (int i = 0; i < rowCount; ++i) {
						((HashMap[]) object)[i] = dataSet.getBean(HashMap.class, i);
					}
//				} else {
//					object = new HashMap[rowCount];
//
//					((HashMap[]) object)[0] = dataSet.getBean(HashMap.class, 0);
//				}
			}

			map.put(dataSet.getId(), object);
		}

		if(successMessages.size() > 0){
			map.put("success", successMessages);
		}

		if(StringUtils.hasText(exceptionMessage)){
			map.put("exception", exceptionMessage);
		}

		return map;
	}

	public void setDSAccessor(TzDSAccessor accessor) {
		this.accessor = accessor;
	}
}
