package com.sheepshare.customer.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import tz.extend.core.mvc.TzRequest;
import tz.extend.core.mvc.TzResponse;

import com.sheepshare.customer.service.CustomerService;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : CustomerController
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
@RequestMapping("/com/sheepshare/customer/*")
public class CustomerController {

    @Autowired
    private CustomerService service;

    /**
     * <pre>
     *  리스트 조회
     * </pre>
     * @param request   
     * @param response
     */
    @RequestMapping("retrieveCustomerList.*")
    public void retrieveCustomerList(TzRequest request, TzResponse response){

        Map<String, Object> data = request.getParam();
        List<Map<String, Object>> mData = service.retrieveCustomerList(data);

        response.setMapList("Customers", mData);
    }

    /**
     * <pre>
     *  리스트 저장
     * </pre>
     * @param request
     * @param response
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("saveCustomers.*")
    public void saveCustomers(TzRequest request, TzResponse response){

        Map<String, Object> customer = (Map<String, Object>)request.getMap("customer");
        Map<String, Object> orders = (Map<String, Object>)request.getMap("orders");
        service.saveCustomers(customer, orders);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("code", "0");
        data.put("msg", "ok");
        response.setMap("output1", data);
    }
}