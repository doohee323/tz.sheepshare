package com.sheepshare.customer.service;

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
 * 프로그램 : CustomerService
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
public class CustomerService {

    /**
     * log 처리를 위한 변수 선언
     */
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    /**
     * DB처리를 위한 공통 dao
     */
    @Autowired
    @Qualifier("mainDB")
    private CommonDao dao;
    
    private  List<Map<String, Object>> mList =  null;

    /**
    * 목록조회.
    *
    * @return List<Map<String, Object>>[code]
    */
    public List<Map<String, Object>> retrieveCustomerList(Map<String, Object> data){
        
        mList = new ArrayList<Map<String, Object>>();
        Map<String, Object> mCustomer = new HashMap<String, Object>();
        
        Map<String, Object> mOrder = new HashMap<String, Object>();
        mOrder.put("product", "Basket");
        mOrder.put("price", "29.99");
        mOrder.put("quantity", "1");
        mOrder.put("orderTotal", "29.99");

        mCustomer.put("id", "1");
        mCustomer.put("firstName", "Lee");
        mCustomer.put("lastName", "Carroll");
        mCustomer.put("address", "1234 Anywhere St.");
        mCustomer.put("city", "Phoenix");
        mCustomer.put("orders", mOrder);
        mList.add(mCustomer);
        
        Map<String, Object> mCustomer2 = (Map<String, Object>)ObjUtil.deepClone(mCustomer);
        mList.add(mCustomer2);

        return mList;
    }

    /**
    * 목록 저장.
    *
    * @return List<Map<String, Object>>[code]
    */
    public void saveCustomers(Map<String, Object> mCustomer, Map<String, Object> mOrder){
        
        mOrder.put("product", "Basket");
        mOrder.put("price", "29.99");
        mOrder.put("quantity", "1");
        mOrder.put("orderTotal", "29.99");

        mCustomer.put("id", "3");
        mCustomer.put("firstName", "Hong");
        mCustomer.put("lastName", "Dewey");
        mCustomer.put("address", "1234 Anywhere St.");
        mCustomer.put("city", "Phoenix");
        mCustomer.put("orders", mOrder);
        mList.add(mCustomer);
        
        Map<String, Object> mCustomer2 = (Map<String, Object>)ObjUtil.deepClone(mCustomer);
        mList.add(mCustomer2);
    }
}
