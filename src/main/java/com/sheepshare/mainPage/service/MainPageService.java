package com.sheepshare.mainPage.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tz.extend.query.CommonDao;
import tz.extend.util.ObjUtil;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : 공통모듈
 * 프로그램 : MainPageService
 * 설    명 : 처리를 위한 service 클래스
 * 작 성 자 :
 * 작성일자 :
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 *
 * ---------------------------------------------------------------
 * </pre>
 *
 * @version 1.0
 */
@Service
public class MainPageService {

    /**
     * log 처리를 위한 변수 선언
     */
    private static final Logger logger = LoggerFactory.getLogger(MainPageService.class);

    /**
     * DB처리를 위한 공통 dao
     */
    @Autowired
    @Qualifier("mainDB")
    private CommonDao dao;

    /**
    * 목록조회.
    *
    * @return List<Map<String, Object>>[code]
    */
    public List<Map<String, Object>> retrieveMainPageList(Map<String, Object> data){
        
        List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
        Map<String, Object> mData = new HashMap<String, Object>();
        
        String gradeImg = "<img src='http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png' alt=''  />";
        gradeImg += "<img src='http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png' alt=''  />";
        gradeImg += "<img src='http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png' alt=''  />";
        gradeImg += "<img src='http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png' alt=''  />";
        gradeImg += "<img src='http://image.kyobobook.com/mimages/static/images/common/ico_star.png' alt=''  />";
        mData.put("gradeImg", gradeImg);
        
        mData.put("collapse", "1");
        mData.put("grade", "1위");
        mData.put("title", "아프니까 청춘이다");
        mData.put("booksImg", "<img src='http://image.kyobobook.com/mimages/static/images/books/img_book.jpg' alt='' />");
        mData.put("writer", "김난도");
        mData.put("publisher", "샘앤파커스");
        mData.put("price", "12,600");
        mData.put("gradeImg", gradeImg);
        mData.put("reviewCnt", "8");
        mData.put("facebookLikeCnt", "11");
        mList.add(mData);
        
        Map<String, Object> mData2 = (Map<String, Object>)ObjUtil.deepClone(mData);
        
        mData2.put("collapse", "2");
        mData2.put("grade", "2위");
        mData2.put("title", "청춘이니까 아프다");
        mList.add(mData2);

        return mList;
    }

}
