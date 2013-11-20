package com.tz.common.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.tz.sy.template.pt42.domain.Region;
import com.tz.sy.template.pt42.service.CenterMgmtService;

/**
 * @author TZ
 * 
 */

@Controller
public class RegionRestController {

    @Autowired
    private CenterMgmtService service;

    @RequestMapping("/uip_regions")
    public @ResponseBody
    Map<String, Object> uipRegions(){
        Map<String, Object> list = new HashMap<String, Object>();
        List<Region> regions = service.retrieveRegionList(null);
        list.put("uip_regions", regions);
        return list;
    }

    @RequestMapping(value = "/uip_regions", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> save(@RequestBody Region region){
        Map<String, Object> data = new HashMap<String, Object>();
//      service.saveRegionOnly(region);
        data.put("uip_region", region);
        return data;
    }

    @RequestMapping(value = "/uip_regions/{id}", method = RequestMethod.PUT)
    public @ResponseBody
    Map<String, Object> update(@PathVariable("id") int id, @RequestBody Region region){
        Map<String, Object> list = new HashMap<String, Object>();
//      service.saveRegionOnly(region);
        list.put("uip_region", region);
        return list;
    }

    @RequestMapping(value = "/uip_regions/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable("id") int id){
//      service.saveRegionOnly(region);
    }
}
