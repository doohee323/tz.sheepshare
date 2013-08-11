package tz.common.message.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import tz.common.message.service.MessageService;

import tz.extend.core.mvc.TzRequest;
import tz.extend.core.mvc.TzResponse;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : MessageController
 * 설    명 : 메시지 관리 Controller Class
 * 작 성 자 : TZ
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
@RequestMapping("/tz/common/message/*")
public class MessageController {

    @Autowired
    private MessageService service;

    /**
     * <PRE>
     * 메시지 조회
     * </PRE>
     * @author
     */
    @RequestMapping("getMessage.*")
    public void getMessage(TzRequest request, TzResponse response){
        Map<String, Object> paramMap = request.get("params", HashMap.class);
        response.addSuccessMessage(service.getMessageByAjax(paramMap));
    }
}
