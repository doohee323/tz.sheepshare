package tz.basis.core.ux.json.dataset;

import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import tz.basis.core.dataset.DSColumn;
import tz.basis.core.dataset.DSRow;
import tz.basis.core.dataset.DSRowImpl;
import tz.basis.core.dataset.io.DSReader;
import tz.basis.core.exception.TzException;
import tz.basis.core.ux.UxConstants;
import tz.basis.upload.FileInfo;

public class JsonDSReader implements DSReader {

	private ObjectMapper mapper = new ObjectMapper();
	private HttpServletRequest request;
	private Map<String, Object> map = new HashMap<String, Object>();

	public JsonDSReader(HttpServletRequest request) {
		this.mapper.getSerializationConfig().setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
		this.request = request;

		try {
			Reader reader = request.getReader();

			if (reader != null) {
				String jsonContent = FileCopyUtils.copyToString(request.getReader());

				if (StringUtils.hasText(jsonContent)) {
					map = mapper.readValue(jsonContent, HashMap.class);
				}
			}
		} catch (IOException e) {
			throw new TzException("[JsonDSReader] JsonDSReader - "
					+ e.getMessage(), e);
		}
	}

	public JsonDSReader(String jsonContent) {
		try {
			map = mapper.readValue(jsonContent, HashMap.class);
		} catch (IOException e) {
			throw new TzException("[JsonDSReader] JsonDSReader - "
					+ e.getMessage(), e);
		}
	}

	public Map<String, Object> getParamMap() {
		Map<String, Object> paramMap = new HashMap<String, Object>();

		@SuppressWarnings("unchecked")
		Map<String, String[]> requestParams = request.getParameterMap();

		Iterator<String> it = requestParams.keySet().iterator();

		while (it.hasNext()) {
			String paramName = it.next();
			String[] paramValue = requestParams.get(paramName);

			if (paramValue != null) {
				try {
					if (paramValue != null) {
						if(paramValue.length > 1){
							paramMap.put(paramName, paramValue);
						} else {
							paramMap.put(paramName, URLDecoder.decode(paramValue[0],"UTF-8"));
						}
					}
				} catch (UnsupportedEncodingException e) {
				}
			}
		}

		return paramMap;
	}

	public List<String> getDSIdList() {
		return Collections
				.unmodifiableList(new ArrayList<String>(map.keySet()));
	}

	public List<DSRow> getDSRows(String dataSetId) {
		List<DSRow> rows = new ArrayList<DSRow>();

		Object root = map.get(dataSetId);

		if (Collection.class.isAssignableFrom(root.getClass())) {
			List<Object> list = (List<Object>) root;

			for (Object node : list) {
				if (!Map.class.isAssignableFrom(node.getClass())) {
					throw new TzException(
							"[JsonDSReader] getDSRows - Invalid DS Format");
				}

				rows.add(map2DSRow(((Map<String, Object>) node)));
			}
		} else {
			rows.add(map2DSRow(((Map<String, Object>) root)));
		}

		return rows;
	}

	protected DSRow map2DSRow(Map<String, Object> map) {
		DSRow row = new DSRowImpl();

		Iterator<String> it = ((Map) map).keySet().iterator();

		while (it.hasNext()) {
			String columnName = it.next();
			Object columnValue = ((Map) map).get(columnName);

			if (columnName.equals(UxConstants.DEFAULT_ROWSTATUS_PROPERTY_NAME)) {
				row.setRowStatus((String) columnValue);
			} else {
				row.add(columnName, columnValue);
			}
		}

		return row;
	}

	public List<DSColumn> getDSColumns(String dataSetId) {
		List<DSColumn> cols = new ArrayList<DSColumn>();

		Object root = map.get(dataSetId);

		if (Collection.class.isAssignableFrom(root.getClass())) {
			root = ((List<Object>) root).get(0);

		}

		Iterator<String> it = ((Map<String, Object>) root).keySet().iterator();

		while (it.hasNext()) {
			String colunmName = it.next();
			Object colunmValue = ((Map<String, Object>) root).get(colunmName);

			cols.add(new DSColumn(colunmName, colunmValue != null ? colunmValue.getClass() : String.class));
		}

		return cols;
	}

	public List<FileInfo> getAttachments() {
		return Collections.emptyList();
	}
}
