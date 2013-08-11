package com.sheepshare.mainPage.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import tz.extend.core.mvc.TzRequest;
import tz.extend.core.mvc.TzResponse;

import com.sheepshare.mainPage.service.MainPageService;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : MainPageController
 * 설    명 : 처리를 위한 controller 클래스
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
@Controller
@RequestMapping("/com/sheepshare/mainPage/*")
public class MainPageController {

    @Autowired
    private MainPageService service;

    /**
     * <pre>
     *  리스트 조회
     * </pre>
     * @param request
     * @param response
     */
    @RequestMapping("retrieveMainPageList.*")
    public void retrieveMainPageList(TzRequest request, TzResponse response){

        Map<String, Object> data = request.getParam();
        List<Map<String, Object>> mData = service.retrieveMainPageList(data);

        response.setMapList("Movies", mData);
    }
}