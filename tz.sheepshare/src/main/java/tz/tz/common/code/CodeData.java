package tz.common.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import tz.common.code.service.CodeService;
import tz.extend.util.SpringUtil;
import tz.extend.util.StringUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : CodeData
 * 설    명 : 코드성 데이터 정보를 제공하는 클래스.
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
@Service
public class CodeData {

    /**
     * log 처리를 위한 변수 선언
     */
    private static final Logger logger = LoggerFactory.getLogger(CodeData.class);

    @Autowired
    private CodeService commonService;
    
    @Autowired
    @Qualifier("appProperties")
    private Properties appProperties;

    /**
     * 모든 코드그룹키/값 목록을 리턴.
     * value는 MESSAGE_KO_KR 테이블의 해당 값.
     *
     * @return List<Map<String, Object>>[code,value]
     */
    public List<Map<String, Object>> commGrpCdList(){
        CodeCache codeCache = (CodeCache)SpringUtil.getApplicationContext().getBean("codeCache");
        return codeCache.getCommGrpCdCache();
    }

    /**
     * 코드 그룹의 키/값 목록을 리턴.
     * @return List<Map<String, Object>>[code,value]
     */
    public List<Map<String, Object>> commGrpCdListReal(){
        try{
            return commonService.commGrpCdValueList();
        }catch(Exception e){
            e.fillInStackTrace();
        }
        return new ArrayList<Map<String, Object>>();
    }

    /**
     * 코드 그룹의 키/값 목록을 리턴.
     * @param input
     * @return List<Map<String, Object>>[code,value]
     */
    public List<Map<String, Object>> commGrpCdListReal(Map<String, Object> input){
        try{
            return commonService.commGrpCdValueList(input);
        }catch(Exception e){
            logger.error(e.getMessage());
        }
        return new ArrayList<Map<String, Object>>();
    }

    /**
     * 주어진 코드그룹에 해당하는 코드키/값 목록조회.
     * - value는 현재 사용자의 언어에 따라 달라짐.
     *
     * @param commGrpCd
     * @return List<Map<String, Object>>[code,value]
     */
    public List<Map<String, Object>> codeList(String commGrpCd){
        if(StringUtil.getText(appProperties.get("tz.commCd.cache.useYn")).equals("true")){
            CodeCache codeCache = (CodeCache)SpringUtil.getApplicationContext().getBean("codeCache");
            return codeCache.getCodeCache(commGrpCd);
        }else{
            Map<String, Object> inputData = new HashMap<String, Object>();
            inputData.put("commGrpCd", commGrpCd);
            return commonService.codeValueList(inputData);
        }
    }

    /**
     * 주어진 코드그룹에 해당하는 코드키/값 목록 쿼리 조회.
     * @return List<Map<String, Object>>[code,value]
     */
    public List<Map<String, Object>> codeValueList(Map<String, Object> inputData) throws Exception{
        return commonService.codeValueList(inputData);
    }

    /**
     * 메뉴 데이터 목록을 리턴
     * @return List<Map<String, Object>>[code,value]
     */
    public List<Map<String, Object>> menuComboList(String menuLvl, String menuCd){
        Map<String, Object> input = new HashMap<String, Object>();
        input.put("menuLvl", menuLvl);
        if(!menuCd.equals(""))
            input.put("menuCd", menuCd);
        try{
            return null;//menuService.retrieveMenuListCombo(input);
        }catch(Exception e){
            logger.error(e.getMessage());
            return new ArrayList<Map<String, Object>>();
        }
    }

    /**
     * List<Map<String, Object>>[code,value]에서 code에 해당하는 value 찾기.
     * @param list
     * @param code
     * @return 해당하는 value가 없는 경우 (code) 리턴
     */
    public String getMatchValue(List<Map<String, Object>> list, String code, String type){
        String value = null;
        try{
            String strCode = "";
            String strValue = "";
            if(type != null && type.equals("code")){
                strCode = "code";
                strValue = "value";
            }else{
                strCode = "value";
                strValue = "code";
            }
            int matchIndex = -1;
            int codeSize = list.size();
            for(int i = 0; i < codeSize; i++){
                if(code.equals(list.get(i).get(strCode).toString())){
                    matchIndex = i;
                }
            }
            value = list.get(matchIndex).get(strValue).toString();
        }catch(Exception e){
        }
        if(StringUtils.isEmpty(value)){
            return "(" + code + ")";
        }
        return value;
    }

    /**
     * List<Map<String, Object>>[code,value]에서 code에 해당하는 value 찾기. 기본 code 검색
     *
     * @param list
     * @param code
     * @return 해당하는 value가 없는 경우 (code) 리턴
     */
    private String getMatchValue(List<Map<String, Object>> list, String code){
        return getMatchValue(list, code, "code");
    }

    /**
     * 코드명 또는 코드그룹명 리턴.
     *
     * - 결과값은 현재 사용자의 언어에 따라 달라짐.
     *
     * @param commGrpCd
     * @param comm_cd comm_cd가 없을 경우 commGrpCd의 이름 리턴.
     * @return 해당하는 name이 없는 경우 (code) 리턴.
     */
    public String commCdNm(String commGrpCd, String commCd){
        if(StringUtils.isEmpty(commCd)){
            return getMatchValue(commGrpCdList(), commGrpCd);
        }
        return getMatchValue(codeList(commGrpCd), commCd);
    }

    /**
     * 코드명 또는 코드그룹명 리턴.
     *
     * - 결과값은 현재 사용자의 언어에 따라 달라짐.
     *
     * @param commGrpCd
     * @param comm_cd_nm comm_cd_nm가 없을 경우 commGrpCd의 이름 리턴.
     * @return 해당하는 value이 없는 경우 (value) 리턴.
     */
    public String codeValue(String commGrpCd, String commCdNm) throws Exception{
        if(StringUtils.isEmpty(commCdNm)){
            return getMatchValue(commGrpCdList(), commGrpCd, "value");
        }
        return getMatchValue(codeList(commGrpCd), commCdNm, "value");
    }

    /**
     * 쿼리를 받아 처리하는 확장 콤보 지원
     *
     * @param commGrpCd
     * @return List<Map<String, Object>>[code,value]
     */
    public List<Map<String, Object>> extCodeList(Map inputData){
        try{
            return commonService.getExtCodeList(inputData);
        }catch(Exception e){
        }
        return new ArrayList<Map<String, Object>>();
    }
}