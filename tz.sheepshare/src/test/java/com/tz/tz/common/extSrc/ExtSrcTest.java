package com.tz.tz.common.extSrc;

import java.util.HashMap;
import java.util.Map;

import tz.extend.test.TestSupport;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
//@Ignore
public class ExtSrcTest extends TestSupport {

    /**
     */
    private static final Logger logger = LoggerFactory.getLogger(ExtSrcTest.class);

    
//    @Autowired
//    private ExtSrcController controller;

    @Test
    public void inserLog(){
        try {
            Map<String, Object> input = new HashMap<String, Object>();
            input.put("db", "true");// search, sysCd
//            controller.insertExtSrcList(input);
        } catch (Exception e) {
            System.out.println("Convert fail!!");
        }
        System.out.println("Convert Success!!");
    }
}
