package tz.common.converter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ezmorph.bean.MorphDynaBean;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : JSONConverter
 * 설    명 : JSONConverter : Entiry <-> Json Rich UI에서 정의한 기본 포멧을 유지한여 처리한다
 * 작 성 자 :
 * 작성일자 :
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 *
 * ---------------------------------------------------------------
 * </pre>
 * @version 1.0
 */
public class JSONConverter {

    private static Logger logger = LoggerFactory.getLogger(JSONConverter.class);
	/**
	 * getLogger
	 */
	public Logger getLogger(){
		return logger;
	}

    /**
     * <pre>
     * 인자로 넘어온 input Map을 Json 포맷(JSONObject)으로 변환
     * </pre>
     * @param request
     * @param response
     */
    public static JSONObject convertMap2Json(Map<String, Object> input) {
        JSONObject object = JSONObject.fromObject(input);
        return object;
    }

    /**
     * <pre>
     * 인자로 넘어온 input String을 KEY:VALUE Map으로 변환
     * </pre>
     * @param request
     * @param response
     */
    public static Map<String, Object> convertStr2Map(String input) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            JSONObject jsonObject = JSONObject.fromObject(input);
            result = (Map<String, Object>)JSONObject.toBean(jsonObject, java.util.HashMap.class);

            result = (Map<String, Object>)new Object(){
            	public Object toMap(Object object){
            		if(object instanceof Map){
            			Map<String, Object> map = (Map<String, Object>) object;
            			for(Map.Entry<String, Object> entry: map.entrySet()){
            				String key = entry.getKey();
            				map.put(key, toMap(entry.getValue()));
            			}
            			return map;
            		}else if(object instanceof DynaBean){
            			Map<String, Object> map = new HashMap<String, Object>();
            			MorphDynaBean bean = (MorphDynaBean) object;
            			for(DynaProperty property :  bean.getDynaClass().getDynaProperties()){
            				String key = property.getName();
            				map.put(property.getName(), toMap(bean.get(key)));
            			}
            			return map;
            		}else if(object instanceof List){
            			List<Object> list = (List<Object>) object;
            			for(int i = 0 ; i < list.size(); i++){
            				list.set(i, toMap(list.get(i)));
            			}
            			return list;
            		}
            		return object;
            	}
            }.toMap(result);
        } catch (Exception e) {
        }
        return result;
    }

}
