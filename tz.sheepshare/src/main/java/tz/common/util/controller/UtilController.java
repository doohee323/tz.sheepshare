package tz.common.util.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import tz.common.util.service.UtilService;

import tz.extend.util.StringUtil;
import tz.extend.core.mvc.TzRequest;
import tz.extend.core.mvc.TzResponse;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : Util
 * 설    명 : Util 처리를 위한 controller 클래스
 * 작 성 자 : 배태일
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
@Controller
@RequestMapping("/tz/common/util/*")
public class UtilController {

	@Autowired
	private UtilService service;

    @Autowired
    @Qualifier("appProperties")
    private Properties appProperties;

    /**
     * <PRE>
     * DB 조회를 통해 특정 쿼리를 조회
     * </PRE>
     * @author
     */
    @RequestMapping("retrieveSimpleData.*")
    public void retrieveSimpleData(TzRequest request, TzResponse response) {
        Map<String, Object> inputData = request.getParam();
        response.setMapList("output", service.retrieveSimpleData(inputData));
    }

    /**
     * <PRE>
     * DB 조회를 통해 특정 값을 조회
     * </PRE>
     * @author
     */
    @RequestMapping("retrieveSingleData.*")
    public void retrieveSingleData(TzRequest request, TzResponse response) {
        Map<String, Object> inputData = request.getParam();
        response.addParam("fv_GvSingleData", service.retrieveSingleData(inputData));
    }

    /**
     * <PRE>
     * DB 조회를 통해 특정 값을 조회
     * </PRE>
     * @author
     */
    @RequestMapping("retrieveSequenceData.*")
    public void retrieveSequenceData(TzRequest request, TzResponse response) {
        Map<String, Object> inputData = request.getParam();
        inputData.put("qId", "util.retrieveSequence");
        response.addParam("fv_GvSingleData", service.retrieveSingleData(inputData));
    }

    /**
     * <PRE>
     * xml 환경 파일로부터 특정 값을 조회
     * </PRE>
     * @author
     */
    @RequestMapping("retrieveConfData.*")
    public void retrieveConfData(TzRequest request, TzResponse response) {
        Map<String, Object> inputData = request.getParam();
        String confId = StringUtil.getText(inputData.get("confId"));
        String strStr = StringUtil.getText(appProperties.getProperty(confId));
        if(strStr.equals("")) strStr = StringUtil.getText(inputData.get("default"));
        List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("strStr", strStr);
        mData.add(hashMap);
        response.setList("output", mData);
    }

    /**
     * <PRE>
     * 우편번호 팝업 조회
     * </PRE>
     * @author
     */
    @RequestMapping("retrieveZpcdListPopup.*")
    public void retrieveZpcdListPopup(TzRequest request, TzResponse response) {
        Map<String, Object> inputData = request.getParam();
        response.setMapList("output", service.retrieveZpcdListPopup(inputData));
    }

}
