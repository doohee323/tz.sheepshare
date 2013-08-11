package tz.basis.core.ux.comweb.dataset;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.StringUtils;

import tz.basis.core.dataset.DSColumn;
import tz.basis.core.dataset.DSRow;
import tz.basis.core.dataset.io.DSReader;
import tz.basis.core.exception.TzException;
import tz.basis.core.mvc.file.operator.FileOperator;
import tz.basis.upload.FileInfo;
import tz.basis.upload.MultiPartInfo;

/**
 *
 * @author TZ
 *
 */
public class ComWebDSReader implements DSReader {

    private Map<String, Object> paramMap = new HashMap<String, Object>();

    private List<FileInfo> attachments = new ArrayList<FileInfo>();

    public ComWebDSReader(HttpServletRequest request, FileOperator fileHandler) {
        try{
            if(fileHandler != null && fileHandler.isMultiPartRequest(request)){
                paramMap.putAll(getParameters(request.getQueryString()));
                MultiPartInfo info = fileHandler.handleMultiPartRequest(request);

                paramMap.putAll(info.getAttributes());
                attachments.addAll(info.getFileInfos());

            }else{
                @SuppressWarnings("unchecked")
                Map<String, Object> parameterMap = request.getParameterMap();
                paramMap.putAll(parameterMap);
                paramMap.putAll(getParameters(request.getQueryString()));
            }
        }catch(Exception e){
            throw new TzException("[ComWebDSReader] ComWebDSReader - " + e.getMessage(), e);
        }
    }

    private Map<String, Object> getParameters(String queryString){
        Map<String, Object> map = new HashMap<String, Object>();

        if(StringUtils.hasText(queryString)){
            try{
                //한글 decode추가
                queryString = URLDecoder.decode(queryString.toString(), "utf-8");
                String[] paramValues = queryString.split("&");

                for(int i = 0; i < paramValues.length; ++i){
                    String[] paramValue = paramValues[i].split("=", 2);

                    if(map.containsKey(paramValue[0])){
                        Object fromParamValue = map.get(paramValue[0]);
                        String[] toParamValue = null;

                        if(String[].class.isAssignableFrom(fromParamValue.getClass())){
                            toParamValue = (String[])ArrayUtils.add((Object[])fromParamValue, paramValue[1]);
                        }else{
                            toParamValue = (String[])ArrayUtils.add(new String[] { (String)fromParamValue },
                                    paramValue[1]);
                        }

                        map.put(paramValue[0], toParamValue);
                    }else{
                        try {
                            map.put(paramValue[0], paramValue[1]);
                        }catch(Exception e1){
                            e1.getStackTrace();
                        }
                    }
                }
            }catch(UnsupportedEncodingException e1){
                throw new TzException(e1);
            }
        }

        return map;
    }

    public Map<String, Object> getParamMap(){
        return paramMap;
    }

    public List<FileInfo> getAttachments(){
        return attachments;
    }

    public List<String> getDSIdList(){
        throw new UnsupportedOperationException("[ComWebDSReader] ComWeb 채널에서는 DS과 관련한 연산을 지원하지 않습니다.");
    }

    public List<DSRow> getDSRows(String dataSetId){
        throw new UnsupportedOperationException("[ComWebDSReader] ComWeb 채널에서는 DS과 관련한 연산을 지원하지 않습니다.");
    }

    public List<DSColumn> getDSColumns(String dataSetId){
        throw new UnsupportedOperationException("[ComWebDSReader] ComWeb 채널에서는 DS과 관련한 연산을 지원하지 않습니다.");
    }
}
