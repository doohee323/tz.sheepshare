package com.tz.sy.template.rest.controller;

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

import com.tz.sy.template.pt42.domain.Center;
import com.tz.sy.template.pt42.service.CenterMgmtService;

/**
 * @author TZ
 * 
 */

@Controller
@RequestMapping("/*")
public class CenterRestController {

    @Autowired
    private CenterMgmtService service;

	@RequestMapping("uip_centers")
	public @ResponseBody
	Map<String, Object> uipCenters() {
		Map<String, Object> list = new HashMap<String, Object>();
		List<Center> centers = service.retrieveCenterCodeList(null);
		list.put("uip_centers", centers);
		return list;
	}

	@RequestMapping(value = "uip_centers", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> save(@RequestBody Center center) {
		Map<String, Object> list = new HashMap<String, Object>();
//		service.saveCenterOnly(center);
		list.put("uip_center", center);
		return list;
	}

	@RequestMapping(value = "uip_centers/{id}", method = RequestMethod.PUT)
	public @ResponseBody
    Map<String, Object> update(@PathVariable("id") int id,
			@RequestBody Center center) {
		Map<String, Object> list = new HashMap<String, Object>();
//      service.saveCenterOnly(center);
		list.put("uip_center", center);
		return list;
	}

	@RequestMapping(value = "uip_centers/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
	public void delete(@PathVariable("id") int id) {
//      service.saveCenterOnly(center);
	}
}
