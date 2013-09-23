package tz.common.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.taskdefs.MacroInstance.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import tz.common.code.service.CodeService;
import tz.common.locale.service.LocaleService;
import tz.extend.iam.UserInfo;
import tz.extend.util.StringUtil;
//import net.sf.ehcache.Cache;
//import net.sf.ehcache.CacheManager;
//import net.sf.ehcache.Element;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : 공통
 * 프로그램 : CodeCache
 * 설    명 : 코드성 데이터 캐시 보관소.
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
public class CodeCache {

    /**
     * log 처리를 위한 변수 선언
     */
    private static final Logger logger = LoggerFactory.getLogger(CodeCache.class);

    public final String KEY_COMM_GRP_CDS = "KEY_COMM_GRP_CDS";

    @Autowired
    @Qualifier("appProperties")
    private Properties appProperties;

    @Autowired
    public CacheManager cacheManager;

    @Autowired
    CodeService commonService = new CodeService();

    @Autowired
    private LocaleService localeService;

    @PostConstruct
    public void init(){
        try{
            if(StringUtil.getText(appProperties.get("tz.commCd.cache.useYn")).equals("true")){
                loadCodeCache();
            }
        }catch(Exception e){
            logger.error(e.getMessage());
        }
    }

    private Cache cache = null;

    private Cache getCache(){
        if(cache == null){
            cache = cacheManager.getCache("codeCache");
        }
        return cache;
    }

    /**
     * 초기화 : 코드성 데이터들을 캐싱
     */
    public void loadCodeCache(){
        try{
            if(StringUtil.getText(appProperties.get("tz.commCd.cache.useYn")).equals("true")){
                if(StringUtil.getText(appProperties.get("tz.refresh.useYn")).equals("false")){
                    loadCommGrpCdList();
                    loadCodeList();
                }else{
                    String target = StringUtil.getText(appProperties.get("tz.refresh.cache.target"));
                    String targetArry[] = target.split("\\,");
                    for(int i = 0; i < targetArry.length; i++){
                        if(targetArry[i].equals("loadCommGrpCd")){
                            loadCommGrpCdList();
                        }else if(targetArry[i].equals("loadCode")){
                            loadCodeList();
                        }
                    }
                }
            }
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * <pre>
     * 코드 그룹 캐쉬
     * </pre>
     */
    private void loadCommGrpCdList(){
        try{
            List<Map<String, Object>> mData = localeService.retrieveLocaleList(new HashMap<String, Object>());
            for(int i = 0; i < mData.size(); i++){
                String loclCd = mData.get(i).get("loclCd").toString();
                Map<String, Object> input = new HashMap<String, Object>();
                input.put("loclCd", loclCd);
                List<Map<String, Object>> groupValueList = commonService.commGrpCdValueList(input);
//                getCache().put(new Element(buildCodeCacheKey(KEY_COMM_GRP_CDS, loclCd), groupValueList));
            }
        }catch(Exception e){
            logger.error("loadCommGrpCdList() : " + e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * <pre>
     *  코드목록 데이터를 캐시에 추가.
     * </pre>
     */
    private void loadCodeList(){
        try{
            List<Map<String, Object>> groupKeyList = commonService.commGrpCdKeyList();
            int groupSize = groupKeyList.size();
            for(int i = 0; i < groupSize;){
                int loopSize = 100;
                if((groupSize - i) < 100){
                    loopSize = groupSize - i - 1;
                }
                ArrayList<String> commGrpCd = new ArrayList<String>();
                for(int j = 0; j <= loopSize; j++){
                    String comm_grp_cd = groupKeyList.get(i).get("code").toString();
                    commGrpCd.add(comm_grp_cd);
                    i++;
                }
                loadCode(commGrpCd);
            }
        }catch(Exception e){
            logger.error("loadCodeList() : " + e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * <pre>
     *  코드목록 데이터를 캐시에 추가.
     * </pre>
     * @param request
     * @param response
     */
    private List<Map<String, Object>> loadCode(ArrayList<String> commGrpCd){
        List<Map<String, Object>> codeListTmp = new ArrayList<Map<String, Object>>();
        try{
            List<Map<String, Object>> mData = localeService.retrieveLocaleList(new HashMap<String, Object>());
            for(int i = 0; i < mData.size(); i++){
                String loclCd = mData.get(i).get("loclCd").toString();
                Map<String, Object> input = new HashMap<String, Object>();
                input.put("loclCd", loclCd);
                input.put("sortCodeNotnull", "Y");
                List<Map<String, Object>> codeList = commonService.codeValueAllList(commGrpCd, input);
                for(int j = 0; j < codeList.size(); j++){
                    if(codeList.size() > (j + 1)
                            && !codeList.get(j).get("commGrpCd").toString()
                                    .equals(codeList.get(j + 1).get("commGrpCd").toString())){
                        codeListTmp.add(codeListTmp.size(), codeList.get(j));

//                        getCache().put(
//                                new Element(buildCodeCacheKey(codeList.get(j).get("commGrpCd").toString(), loclCd),
//                                        codeListTmp));

                        codeListTmp = new ArrayList<Map<String, Object>>();
                    }else{
                        codeListTmp.add(codeListTmp.size(), codeList.get(j));
                    }
                }
                if(!codeListTmp.isEmpty()){
//                    getCache()
//                            .put(new Element(buildCodeCacheKey(codeList.get(codeList.size() - 1).get("commGrpCd")
//                                    .toString(), loclCd), codeListTmp));
                }
            }
        }catch(Exception e){
            logger.error("loadCode() : " + e);
            throw new RuntimeException(e.getMessage());
        }
        return codeListTmp;
    }

    /**
     * <pre>
     * 캐시에 담기 위한 캐시키 생성
     * </pre>
     * @param comm_grp_cd
     * @param locale ko_KR, en_US 등
     * @return
     */
    private String buildCodeCacheKey(String comm_grp_cd){
        return comm_grp_cd + "_" + StringUtils.lowerCase(UserInfo.getLoclCd());
    }

    private String buildCodeCacheKey(String comm_grp_cd, String loclCd){
        return comm_grp_cd + "_" + StringUtils.lowerCase(loclCd);
    }

    /**
     * <pre>
     * 주어진 코드그룹 및 언어에 해당하는
     * 캐시된 코드키/값 목록 리턴.
     * </pre>
     * @param comm_grp_cd
     * @param locale
     * @return
     */
    public List<Map<String, Object>> getCodeCache(String comm_grp_cd){
        String cacheKey = buildCodeCacheKey(comm_grp_cd);
//        return (List<Map<String, Object>>)getCache().get(cacheKey).getObjectValue();
        return null;
    }

    /**
     * <pre>
     * 모든 코드그룹키/값 목록을 리턴.
     * value는 MESSAGE_KO_KR 테이블의 해당 값.
     * </pre>
     * @return
     */
    public List<Map<String, Object>> getCommGrpCdCache(){
        String cacheKey = buildCodeCacheKey(KEY_COMM_GRP_CDS);
//        return (List<Map<String, Object>>)getCache().get(cacheKey).getObjectValue();
        return null;
    }

    /**
     * <pre>
     * 캐시값 리턴.
     * </pre>
     * @param key
     * @return
     */
    public Object getCache(Object key){
//        return getCache().get(key).getObjectValue();
        return null;
    }

    /**
     * <pre>
     * 캐시를 갱신한다.
     * </pre>
     * @param key 캐시 키
     * @param value 새로운 캐시 값
     */
    public void refresh(Object key, Object value){
//        getCache().put(new Element(key, value));
    }

    /**
     * <pre>
     * 캐시를 삭제한다.
     * </pre>
     * @param key 캐시 키
     */
    public void remove(Object key){
//        getCache().remove(key);
        logger.debug("remove()");
    }

    /**
     * <pre>
     * 캐싱을 다시 하는 method
     * </pre>
     */
    public void refresh(){
        synchronized(this){
        }
    }
}