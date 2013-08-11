package tz.common.locale.controller;

import java.util.Map;

import tz.basis.data.GridData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import tz.common.locale.domain.LoclInfo;
import tz.common.locale.service.LocaleService;

import tz.extend.iam.UserInfo;
import tz.extend.core.mvc.TzRequest;
import tz.extend.core.mvc.TzResponse;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : LocaleController
 * 설    명 : Locale 관련 컨트롤러
 * 작 성 자 : TZ
 * 작성일자 : 2013.04.17
 * 수정이력 :
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 * 2013.04.12    TZ    최초  작성1
 * ---------------------------------------------------------------
 * </pre>
 * @version 1.0
 */

@Controller
@RequestMapping("/tz/common/locale/*")
public class LocaleController {

	/**
	 * <pre>
	 *  changeLocale
	 * </pre>
	 * @param request
	 * @param response
	 */
	/**
	 * Service 클래스 이용하기위한 변수
	 */
	@Autowired
	private LocaleService service;

	@RequestMapping("changeLocale.*")
    public void changeLocaleSession(TzRequest request, TzResponse response) throws Exception {

		Map<String,Object> inputData = request.getParam();
        GridData<LoclInfo> mData = request.getGridData("input1", LoclInfo.class);

		UserInfo.getUserInfo().setLoclCd(inputData.get("localeCd").toString());
		service.updateUserLocl(mData);
    }
}
