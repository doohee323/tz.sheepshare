package com.tz.sy.template.pt42.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tz.basis.core.ux.UxConstants;
import tz.basis.data.GridData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tz.sy.template.pt42.domain.Center;
import com.tz.sy.template.pt42.domain.Region;
import com.tz.sy.template.pt42.service.CenterMgmtService;

import tz.extend.core.mvc.TzRequest;
import tz.extend.core.mvc.TzResponse;
import tz.extend.util.StringUtil;


/**
*
* UI 템플릿 (Master/Detail n:n) 처리를 위한 Controller
*
* @author LHF
*
*/
@Controller
@RequestMapping("/pattern/pt42/masterdetail/*")
public class CenterMgmtController {

	@Autowired
	private CenterMgmtService service;

	/**
	 *
	 * <p>
	 *  Center 코드 조회
	 * </p>
	 *
	 * <p>
	 * example:
	 * </p>
	 *
	 * <p>
	 * {@code
	 *
	 * }
	 * </p>
	 * @param request
	 * @param response
	 */
	@RequestMapping("initCenter.*")
	public void initCenterInfo(TzRequest request, TzResponse response) {
		List<Center> centerList = service.retrieveCenterCodeList(null);
		response.setList("output1", centerList, Center.class);
	}

	/**
	 *
	 * <p>
	 *  Center 정보 조회
	 * </p>
	 *
	 * <p>
	 * example:
	 * </p>
	 *
	 * <p>
	 * {@code
	 *
	 * }
	 * </p>
	 * @param request
	 * @param response
	 */
	@RequestMapping("retrieveCenterList.*")
	public void retrieveCenterList(TzRequest request, TzResponse response) {
		String centerCode = StringUtil.getText(request.getMap("params").get("code"));

		List<Center> centerList = service.retrieveCenterCodeList(centerCode);

		response.setList("output1", centerList, Center.class);
	}

	/**
	 *
	 * <p>
	 *  Region 정보 조회
	 * </p>
	 *
	 * <p>
	 * example:
	 * </p>
	 *
	 * <p>
	 * {@code
	 *
	 * }
	 * </p>
	 * @param request
	 * @param response
	 */
	@RequestMapping("retrieveRegionList.*")
	public void retrieveRegionList(TzRequest request, TzResponse response) {
		String centerCode = StringUtil.getText(request.getMap("params").get("centerCode"));

		List<Region> regionList = service.retrieveRegionList(centerCode);

		response.setList("output1", regionList, Region.class);
	}

	/**
	 *
	 * <p>
	 *  Center / Region 수정
	 * </p>
	 *
	 * <p>
	 * example:
	 * </p>
	 *
	 * <p>
	 * {@code
	 *
	 * }
	 * </p>
	 * @param request
	 * @param response
	 */
	@RequestMapping("saveCenterRegion.*")
	public void saveCenterRegion(TzRequest request, TzResponse response) {
		GridData<Center> centerList = request.getGridData("uip_center", Center.class);
		GridData<Region> regionList = request.getGridData("uip_region", Region.class);

		service.saveCenterRegion(centerList, regionList);
        response.setMap("output1", UxConstants.setMsg(true));
	}
}
