Date.prototype.format = function(f) {
    if (!this.valueOf()) return " ";

    var weekName = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];
    var d = this;

    return f.replace(/(yyyy|yy|MM|dd|E|hh|mm|ss|a\/p)/gi, function($1) {
        switch ($1) {
            case "yyyy": return d.getFullYear();
            case "yy": return (d.getFullYear() % 1000).zf(2);
            case "MM": return (d.getMonth() + 1).zf(2);
            case "dd": return d.getDate().zf(2);
            case "E": return weekName[d.getDay()];
            case "HH": return d.getHours().zf(2);
            case "hh": return ((h = d.getHours() % 12) ? h : 12).zf(2);
            case "mm": return d.getMinutes().zf(2);
            case "ss": return d.getSeconds().zf(2);
            case "a/p": return d.getHours() < 12 ? "오전" : "오후";
            default: return $1;
        }
    });
};

String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
Number.prototype.zf = function(len){return this.toString().zf(len);};

///* 엘리먼트를 리턴 */
//function $(element_id) {
//	var obj;
//	obj = document.getElementById(element_id);
//	if(obj == null) {
//		obj = document.getElementsByName(element_id)[0];
//	}
//	return obj;
//}
//
///* 엘리먼트(또는 엘리먼트들)을 리턴 */
//function $N(element_name) {
//	return document.getElementsByName(element_name);
//}
//
///* 엘리먼트의 값을 리턴 */
//function $F(element_id) {
//  return $(element_id).value;
//}

/**
 * @type : function
 * @access : public
 * @desc : form submit reset
*/
function actCancel(oform, action, target) {
  if (target) oform.target = target;
  if (action) oform.action = action;
  if (!oform.method) oform.method = "post";
  oform.reset();
  oform.submit();
}

/**
 * @type : function
 * @access : public
 * @desc : form submit
*/
function actSubmit(_form, _action, _target) {
  if (_target) _form.target = _target;
  if (_action) _form.action = _action;
  if (!_form.method) _form.method = "post";
  _form.submit();
}

function cfResetVar(doc, objCnt) {
    document.getElementById("updateList").value = "";
    document.getElementById("deleteList").value = "";
    document.getElementById("atchCnt").value = "";
    var aAttachFile = cfGetObjectId('attachFile', objCnt);
    doc.getElementById(aAttachFile + "item").innerHTML = "";
    cfGetInputFile(doc, aAttachFile, 0);
}

function cfDownloadFile( aAttachFile , fileSeq ) {
    var objCnt = aAttachFile.substring("attachFile".length, aAttachFile.length);
   	var fileUrl = cfGetFileURL( aAttachFile , fileSeq );
   	cfGetPoppyFrame(document, objCnt).src = fileUrl;
}

function cfGetFileURL( attachFileNo , fileSeq ) {
	return "/tz/common/file/fileDownload.ajax?atchFileBunchNo="+attachFileNo+"&fileSeq="+fileSeq;
}

function cfSingleFileUpload( callbackFunction, policy, nCnt ) {
	var frm = document.frm;
	if ( callbackFunction != null && callbackFunction != "" ) document.getElementById("callbackFunction").value = callbackFunction;
	if ( nCnt != null && nCnt != "" )			  document.getElementById("nCnt").value = nCnt;
	frm.action  =  "/tz/common/file/saveSimpleFile.ajax";
	if ( policy != null && policy != "" ) {
		document.getElementById("config").value = policy;
		//if(policy == 'tz') {
		//	frm.action  =  "/tz/common/file/saveTzSimpleFile.ajax";
		//}
	}
	frm.method  = "post";
	frm.encType = "multipart/form-data";
        frm.target =  cfGetPoppyFrame(document, 0).name;
	frm.submit();
}

/**
 * @type   : function
 * @access : public
 * @desc   : Cookie에 name-value 쌍으로 값을 저장
 */
function cfSetCookie(name, value) {
    var expires = new Date();
    expires.setTime(expires.getTime() + 1000*60*60*24*30); //30일간 유지
    document.cookie = name + '=' + escape(value) + '; path=/; expires='+ expires.toGMTString()+'; ';
}

/**
 * @type   : function
 * @access : public
 * @desc   : 쿠키정보를 리턴한다.
 */
function cfGetCookie(name){
	var nameOfCookie = name + "=";
	var x=0;
	while(x<= document.cookie.length){
		var y= (x+nameOfCookie.length);
		if(document.cookie.substring(x,y) == nameOfCookie){
			if((endOfCookie= document.cookie.indexOf(";",y)) == -1)
				endOfCookie = document.cookie.length;
			return unescape(document.cookie.substring(y,endOfCookie));
		}
		x= document.cookie.indexOf(" ",x)+1;
		if(x==0)
		break;
	}
	return "";
}

/**
 * @type   : function
 * @access : public
 * @desc   : Cookie에 저장된 값을 삭제한다
 */
function deleteCookie(sName){
    document.cookie = sName+"=; expires=Fri, 31 Dec 1999 23:59:59 GMT;";
}

/**
 * @type   : function
 * @access : public
 * @desc   : 특정 경로의 파일 다운로드
 */
function cfDownloadTemplate(serverFileName, clientFileName) {
	if(!clientFileName) clientFileName = '';
	var fileUrl = "/tz/common/file/downloadFile.ajax?serverFileName="+serverFileName + "&clientFileName="+clientFileName;
   	cfGetPoppyFrame(document, 0).src = fileUrl;
}

/**
 * @type   : function
 * @access : public
 * @desc   : 특정 프레임의 document 객체를 리턴
 */
function cfGetFrameDoc(frm){
        if(cfGetFrame(frm)) {
            return cfGetFrame(frm).document;
        } else {
            return null;
        }
}

/**
 * @type   : function
 * @access : public
 * @desc   : 이름에 해당하는 TZ 화면 프레임을 리턴
 */
function cfGetFrame(frm){
    var aFrame;
    if(frm == 'tabFrame') {
		if(parent.tabFrame) {
			aFrame = parent.tabFrame;
		} else if(top.tabFrame) {
			aFrame = top.tabFrame;
		}
    } else if(frm == 'mainFrame') {
		if(parent.mainFrame) {
			aFrame = parent.mainFrame;
		} else if(top.mainFrame) {
			aFrame = top.mainFrame;
		}
    } else if(frm == 'topFrame') {
		if(parent.topFrame) {
			aFrame = parent.topFrame;
		} else if(top.topFrame) {
			aFrame = top.topFrame;
		}
    } else if(frm == 'leftFrame') {
		if(parent.leftFrame) {
			aFrame = parent.leftFrame;
		} else if(top.leftFrame) {
			aFrame = top.leftFrame;
		}
    }
    if(aFrame) return aFrame;
    return null;
}

/**
 * @type   : function
 * @access : public
 * @desc   : 왼쪽 메뉴 펼치기/접기 버튼 클릭 이벤트 함수
 */
function cfControlMenu(imagePath, frameseq) {
        var aFixMenu = document.all.bFixMenu.value;
        if (aFixMenu == 'true') {
                cfLeftMenuShow(imagePath, frameseq);
        }
}

/**
 * @type : function
 * @access : public
 * @desc : UTF-8에서 한글 처리
 */
function cfHangle(val){
	return encodeURIComponent(val);
}

/**
 * @type : function
 * @access : public
 * @desc : iframe의 document 리턴
 */
function cfGetIframeDoc(ifm){
	return ifm.contentWindow.document;
}

/**
 * @type : function
 * @access : public
 * @desc : hidden frame 생성 및 리턴
 */
function cfGetPoppyFrame(doc, objCnt) {
	var aObjCnt = '';
	if(objCnt) aObjCnt = objCnt;
	var poppy = doc.getElementById("poppy");
	if ( poppy == null )  {
		if (cfGetMsieYn() == true)  {
		//if (/MSIE (\d+\.\d+);/.test(navigator.userAgent)) {
			doc.body.insertAdjacentHTML("beforeEnd", "<div id='poppy'/></div>");
		} else {
			alert('poppy div not found!!!');
			return null;
		}
	}
	doc.getElementById("poppy").innerHTML = "<iframe id='poppyFrame"+aObjCnt+"' name='poppyFrame"+aObjCnt+"' src='about:blank' style='width:0;height:0;border:0;frameborder:0;padding:0;margin:0' scrolling=no></iframe>";
	poppy = doc.getElementById("poppyFrame" + aObjCnt);
	return poppy  ;
}

/**
 * @type   : function
 * @access : public
 * @desc   : Msie 6~10 버전 해당 여부
 * </pre>
 * @author : TZ
 */
function cfGetMsieYn(){
	if (navigator.appVersion.indexOf("MSIE 6.0")>=0 || navigator.appVersion.indexOf("MSIE 7.0")>=0
		|| navigator.appVersion.indexOf("MSIE 8.0")>=0 ||  navigator.appVersion.indexOf("MSIE 9.0")>=0
		||  navigator.appVersion.indexOf("MSIE 10.0")>=0 || navigator.appVersion.indexOf("Chrome")>=0 ) {
		return true;
	}
	return false;
}

/**
 * @type   : function
 * @access : public
 * @desc   : url에서 param의 value 가져오기
 * <pre>	cfGetUrlParam("http:~~", "menuCd");
 * </pre>
 * @author : TZ
 */
function cfGetUrlParam(aUrl, aCol){
    var strRslt = "";
	if(aUrl.indexOf("?") > 0) {
	  var tmp = aUrl.substring(aUrl.indexOf("?") + 1, aUrl.length);
	  var tmpArry = tmp.split("&");
	  for(i=0;i<tmpArry.length;i++){
	    var tmp2Arry = tmpArry[i].split("=");
        if(tmp2Arry[0] == aCol) {
          strRslt = tmp2Arry[1];
        }
      }
    }
    return strRslt;
  }

 /**
 * @type   : function
 * @access : public
 * @desc   : url에서 param의 value 가져오기
 * <pre>	cfGetParam("aaa=bbb;ccc=ddd", "menuCd");
 * </pre>
 * @author : TZ
 */
function cfGetParam(tmpArry, aCol){
  var strRslt = "";
  for(i=0;i<tmpArry.length;i++){
    var tmp2Arry = tmpArry[i].split("=");
    if(tmp2Arry[0] == aCol) {
      strRslt = tmp2Arry[1];
      return strRslt;
    }
  }
  return "";
}

/**
* @type   : function
* @access : public
* @desc   : param array를 param url로 변환
* <pre>	cfGetUrlWithParam(["aaa=bbb,ccc=ddd"]);
* </pre>
* @author : TZ
*/
function cfGetUrlWithParam(aArry, aGubun){
	var sGubun = '&';
	if (aGubun)
		sGubun = aGubun;
	var strRslt = "";
	for (i = 0; i < aArry.length; i++) {
		if (aArry[i]) {
			var tmp2Arry = aArry[i].split("=");
			strRslt += sGubun + tmp2Arry[0] + "=" + cfHangle(tmp2Arry[1]);
		}
	}
	if (strRslt.indexOf(sGubun) > -1)
		strRslt = strRslt.substring(sGubun.length, strRslt.length);
	return strRslt;
}

/**
 * @type   : function
 * @access : public
 * @desc   : javascript에서 lastIndexOf 함수
 * <pre>
 * ex) cfLastIndexOf(aStr, aGubun)
 * </pre>
 */
function cfLastIndexOf(aStr, aGubun) {
	var pos = 0;
	var tmp = aStr;
	while(tmp.indexOf(aGubun) > -1) {
		pos = pos + tmp.indexOf(aGubun) + 1;
		tmp = tmp.substring(tmp.indexOf(aGubun) + 1, tmp.length);
	}
	return pos - 1;
}

/**
 * @type : function
 * @access : public
 * @desc : 특정 첨부파일 묶음 번호에 속해 있는 파일을 새로운 경로로 복사하는 함수
 *
 * <pre>
 * ex) cfCopyCommonFile(v_atchFileBunchNo, v_newFilePath)
 * </pre>
 */
function cfCopyCommonFile(v_atchFileBunchNo, v_newFilePath) {
	var url = '/tz/common/file/CopyCommonFile.ajax?atchFileBunchNo=' + v_atchFileBunchNo + '&newFilePath=' + v_newFilePath;
	DU.LConnect.tzncRequest('post', url, {
		success : function(e) {
			strRslt = e.responseText;
		},
		failure : function(e) {
			strRslt = e.responseText;
		}
	}, params);
}

/**
 * @type : function
 * @access : public
 * @desc : 첨부 파일을 경로 이동
 *
 * <pre>
 * ex) cfMoveCommonFile(v_atchFileBunchNo, v_newFilePath)
 * </pre>
 */
function cfMoveCommonFile(v_atchFileBunchNo, v_newFilePath, v_newFileName) {
	var url = '/tz/common/file/MoveCommonFile.ajax?atchFileBunchNo=' + v_atchFileBunchNo
		+ '&newFilePath=' + v_newFilePath
		+ '&newFileName=' + v_newFileName;
	DU.LConnect.tzncRequest('post', url, {
		success : function(e) {
			strRslt = e.responseText;
		},
		failure : function(e) {
			strRslt = e.responseText;
		}
	}, params);
}

/**
 * @type : method
 * @access : public
 * @object : cfEncodeParam
 * @desc : 특정 문자 암호화 처리
 */
function cfEncodeParam(postData) {
	if($encodeParam && $encodeParam != '') {
		var encodeParamArry = $encodeParam.split(",");
		for(var i=0;i<encodeParamArry.length;i++) {
			var strName = encodeParamArry[i] + '=';
			if(postData.indexOf(strName) > -1) {
				var strStr1 = postData.substring(0, postData.indexOf(strName) + strName.length);
				var strVal = '';
				var nPos = 0;
				if(postData.indexOf('&', postData.indexOf(strName)) > -1) {
					nPos = postData.indexOf('&', postData.indexOf(strName));
				} else {
					nPos = postData.length;
				}
				strVal = postData.substring(postData.indexOf(strName) + strName.length, nPos);
				strVal = base64Encode(strVal);
				var strStr2 = postData.substring(nPos, postData.length);
				postData = strStr1 + strVal + strStr2;
			}
		}
	}
	return postData;
}

function cfGetContextPath() {
	var offset = location.href.indexOf(location.host) + location.host.length;
	return location.href.substring(offset,location.href.indexOf('/', offset+1));
}

/**
 * @type : method
 * @access : public
 * @object : cfGetUrl
 * @desc : 현재 서버의 서버 url
 */
function cfGetUrl(aDomain) {
	var strUrl = '';
	if(aDomain && aDomain != '') {
		strUrl = aDomain;
	} else {
		strUrl = window.location.protocol + "//" + window.location.host;
	    try {
	    	strUrl += gPath;
	    } catch(e) {
			strUrl += cfGetContextPath();
	    }
	}
	return strUrl;
}

/**
 * @type : method
 * @access : public
 * @object : cfGetUrl
 * @desc : 현재 서버의 서버 url
 */
function cfGetServerFrontUrl() {
	var strUrl = window.location.protocol + "//" + window.location.host;
	/*if(window.location.port != '') {
		strUrl += ":" + window.location.port
	}*/
	return strUrl;
}

/**
 * Encodes a string into the Base64 encoded notation.
 * @param string the string to encode
 * @return string the encoded string
 */

function base64Encode(str)
{
	var charBase64 = new Array(
		'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P',
		'Q','R','S','T','U','V','W','X','Y','Z','a','b','c','d','e','f',
		'g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v',
		'w','x','y','z','0','1','2','3','4','5','6','7','8','9','+','/'
	);

	var out = "";
	var chr1, chr2, chr3;
	var enc1, enc2, enc3, enc4;
	var i = 0;

	var len = str.length;

	do
	{
		chr1 = str.charCodeAt(i++);
		chr2 = str.charCodeAt(i++);
		chr3 = str.charCodeAt(i++);

		//enc1 = (chr1 & 0xFC) >> 2;
		enc1 = chr1 >> 2;
		enc2 = ((chr1 & 0x03) << 4) | (chr2 >> 4);
		enc3 = ((chr2 & 0x0F) << 2) | (chr3 >> 6);
		enc4 = chr3 & 0x3F;

		out += charBase64[enc1] + charBase64[enc2];

		if (isNaN(chr2))
  		{
			out += '==';
		}
  		else if (isNaN(chr3))
  		{
			out += charBase64[enc3] + '=';
		}
		else
		{
			out += charBase64[enc3] + charBase64[enc4];
		}
	}
	while (i < len);

	return out;
}

function CreateHTTPRequestObject () {
	var forceActiveX = (window.ActiveXObject && location.protocol === "file:");
	if (window.XMLHttpRequest && !forceActiveX) {
	    return new XMLHttpRequest();
	}
	else {
	    try {
	        return new ActiveXObject("Microsoft.XMLHTTP");
	    } catch(e) {}
	}
	alert ("Your browser doesn't support XML handling!");
	return null;
}

function CreateMSXMLDocumentObject () {
	if (typeof (ActiveXObject) != "undefined") {
	    var progIDs = [
	                    "Msxml2.DOMDocument.6.0",
	                    "Msxml2.DOMDocument.5.0",
	                    "Msxml2.DOMDocument.4.0",
	                    "Msxml2.DOMDocument.3.0",
	                    "MSXML2.DOMDocument",
	                    "MSXML.DOMDocument"
	                  ];
	    for (var i = 0; i < progIDs.length; i++) {
	        try {
	            return new ActiveXObject(progIDs[i]);
	        } catch(e) {};
	    }
	}
	return null;
}

function CreateXMLDocumentObject (rootName) {
	if (!rootName) {
	    rootName = "";
	}
	var xmlDoc = CreateMSXMLDocumentObject ();
	if (xmlDoc) {
	    if (rootName) {
	        var rootNode = xmlDoc.createElement (rootName);
	        xmlDoc.appendChild (rootNode);
	    }
	}
	else {
	    if (document.implementation.createDocument) {
	        xmlDoc = document.implementation.createDocument ("", rootName, null);
	    }
	}

	return xmlDoc;
}

function ParseHTTPResponse (httpRequest) {
	var xmlDoc = httpRequest.responseXML;

	if (!xmlDoc || !xmlDoc.documentElement) {
	    if (window.DOMParser) {
	        var parser = new DOMParser();
	        try {
	            xmlDoc = parser.parseFromString (httpRequest.responseText, "text/xml");
	        } catch (e) {
	            alert ("XML parsing error");
	            return null;
	        };
	    } else {
	        xmlDoc = CreateMSXMLDocumentObject ();
	        if (!xmlDoc) {
	            return null;
	        }
	        xmlDoc.loadXML (httpRequest.responseText);

	    }
	}

	var errorMsg = null;
	if (xmlDoc.parseError && xmlDoc.parseError.errorCode != 0) {
	    errorMsg = "XML Parsing Error: " + xmlDoc.parseError.reason
	              + " at line " + xmlDoc.parseError.line
	              + " at position " + xmlDoc.parseError.linepos;
	} else {
	    if (xmlDoc.documentElement) {
	        if (xmlDoc.documentElement.nodeName == "parsererror") {
	            errorMsg = xmlDoc.documentElement.childNodes[0].nodeValue;
	        }
	    }
	}
	if (errorMsg) {
	    alert (errorMsg);
	    return null;
	}
	return xmlDoc;
}

function IsRequestSuccessful (httpRequest) {
	var success = (httpRequest.status == 0 ||
	    (httpRequest.status >= 200 && httpRequest.status < 300) ||
	    httpRequest.status == 304 || httpRequest.status == 1223);
	return success;
}

//post방식 전송
function cfPostToUrl(path, params, method){
    method = method || "post";
    var form = document.createElement("form");
    form.setAttribute("method", method);
    form.setAttribute("action", path);
    for(var key in params) {
        var hiddenField = document.createElement("input");
        hiddenField.setAttribute("type", "hidden");
        hiddenField.setAttribute("name", key);
        hiddenField.setAttribute("value", params[key]);
        form.appendChild(hiddenField);
    }
    document.body.appendChild(form);
    form.submit();
    return true;
}

function cfEmailChk(Email){
	var invalidchars = " /:,;";
	for(var i=0; i < invalidchars.length; i++){
		badchar = invalidchars.charAt(i);
		if (Email.indexOf(badchar,0) > -1){
			return false;
		}
	}
	var atPos = Email.indexOf("@",1);
	if(atPos == -1){
		return false;
	}
	if(Email.indexOf("@",atPos+1) > -1){
		return false;
	}
	var periodPos = Email.indexOf(".",atPos);
	if(periodPos == -1){
		return false;
	}
	if(periodPos+3 > Email.length) {
		return false;
	}
	return true;
}

function cfCheckEmpty(str){
	for (var i = 0; i < str.length; i++){
	   if ( str.charAt(i) == " ")
	      return true;
	}
	return false;
}

// RSA 암호화 후 인증 요청
function cfEncryptedLogin(loginForm, frm) {
	var userId = cfHangle(loginForm.userId.value);
	var passwd = cfHangle(loginForm.userPwd.value);
	loginForm.userPwd.value = "";

	var rsaPublicKeyModulus = frm.publicKeyModulus.value;
	var rsaPpublicKeyExponent = frm.publicKeyExponent.value;
    var rsa = new RSAKey();
    rsa.setPublic(rsaPublicKeyModulus, rsaPpublicKeyExponent);

    // 사용자ID와 비밀번호를 RSA로 암호화한다.
    var securedUserId = rsa.encrypt(userId);
    var securedPasswd = rsa.encrypt(passwd);

    // POST 로그인 폼에 값을 설정하고 발행(submit) 한다.
	var ifm = cfGetPoppyFrame(document);
	frm.target = ifm.name;
    frm.j_username.value = securedUserId;
    frm.j_password.value = securedPasswd;
    frm.submit();
}

// RSA keY 가져오기
function cfGetRSAKey(frm) {
	$.ajax({
	    type: "post",
	    url: gPath + '/tz/common/login/loginRSAKey.ajax',
	    data: {},
	    datatype: "json",
	    contentType: "application/json+core; charset=utf-8",
	    success: function(data){
	    	var msg;
	    	if(data && data.success) {
	    		msg = data.success[0];
	    		if(msg && msg != '') {
	    		} else {
	    			msg = "|";
	    		}
	    	} else {
    			msg = "|";
	    	}
    		var key = msg.split("|");
    		frm.publicKeyModulus.value = key[0];
    		frm.publicKeyExponent.value = key[1];
	    }
	});
}

// 메시지 가져오기
function cfGetMsg(msgId, paramArray, loclCd) {
	if(!loclCd) loclCd = 'ko_KR';
	if(!paramArray) paramArray = '';
	var params = "{\"params\" : {\"msgId\" : \"" + msgId + "\", \"loclCd\" : \"" + loclCd + "\", \"paramArray\" : \"" + paramArray + "\"  }}";
	$.ajax({
	    type: "post",
	    url: gPath + '/tz/common/message/getMessage.ajax',
	    data: params,
	    datatype: "json",
	    contentType: "application/json+core; charset=utf-8",
	    success: function(data){
			var msg;
	    	if(data && data.success) {
	    		msg = data.success[0];
	    		if(msg && msg != '') {
	    		} else {
	    			msg = "no message!";
	    		}
	    	} else {
	    		msg = "can't get the message!";
	    	}
	    	if(msg && msg != '') {
	    		alert(msg);
	    	}
	    }
	});
}


/**
* @type   : function
* @access : public
* @desc   : 결재를 한 내용을 볼수 있는 뷰어를 오픈한다.
* <pre>
* params[0] = 'formCode=RETURNURL2010041502';	// 결재 연동 프로그램 코드 (필수)
* params[1] = 'dutySysCd=co';		// 시스템 코드 (필수)
* params[2] = 'elapId=' + dataSet.getNameValue(dataSet.getRow(), 'elapId');	// 결재 문서 번호
* params[3] = 'atchFileBunchNo=117';	// 첨부파일 묶음 번호
* var eapParams = new Array();	//폼파라미터
* eapParams[0] = 'par1=' + dataSet.getNameValue(dataSet.getRow(), 'num');
* eapParams[1] = 'par2=val2';
* cfApprRequestView(params, eapParams);
* </pre>
* @param    : params
*/
function cfApprRequestView(params, eapParams, eapprMode) {
    var url = 'tz/common/eappr/requestDraft.ajax?' + cfGetEapprParam(params);
    if(eapParams) url += '&' + cfGetEapprParam(eapParams);
    alert(url);
	var h=800;
	var w=900;
	var left = (screen.width - w)/2;
	var top = 100; //(screen.height - h)/2;
	if(gEapprMode == 'real') {
		if(!eapprMode || eapprMode == 'test') {
			var apprViewWindow = window.open(url, "apprViewTarget", "height="+h+",width="+w+",left="+left+",top="+top+",status=no,toolbar=no,menubar=no,location=no,scrollbars=no,resizable=yes");
		} else {
			cfGetPoppyFrame(document).src = url;
		}
	} else {
		var apprViewWindow = window.open(url, "apprViewTarget", "height="+h+",width="+w+",left="+left+",top="+top+",status=no,toolbar=no,menubar=no,location=no,scrollbars=no,resizable=yes");
	}
}

/**
* @type   : function
* @access : public
* @desc   : param array를 param url로 변환
* <pre>	cfGetUrlWithParam(["aaa=bbb,ccc=ddd"]);
* </pre>
* @author : 홍두희
*/
function cfGetUrlWithParam(aArry, aGubun){
	var sGubun = '&';
	if (aGubun)
		sGubun = aGubun;
	var strRslt = "";
	for (var i=0;i<aArry.length;i++) {
		if (aArry[i]) {
			var tmp2Arry = aArry[i].split("=");
			strRslt += sGubun + tmp2Arry[0] + "=" + cfHangle(tmp2Arry[1]);
		}
	}
	if (strRslt.indexOf(sGubun) > -1)
		strRslt = strRslt.substring(sGubun.length, strRslt.length);
	return strRslt;
}

/**
* @type   : function
* @access : public
* @desc   : param array를 param url로 변환
* <pre>	cfGetEapprParam(["aaa=bbb,ccc=ddd"]);
* </pre>
* @author : 홍두희
*/
function cfGetEapprParam(aType, aArry){
 var strRslt = "";
 for(var i=0;i<aArry.length;i++){
	var tmp2Arry = aArry[i].split("=");
	strRslt += "&" + tmp2Arry[0] + "=" + cfHangle(tmp2Arry[1]);
 }
 if(strRslt.indexOf("&") > -1) strRslt = strRslt.substring(1, strRslt.length);
 return strRslt;
}

