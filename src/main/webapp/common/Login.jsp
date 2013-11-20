<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/include/Head.jspf" %>

<script type="text/javascript">
    $(document).ready(function(){
        $('#container a[href="#"]').each(function(idx, item){
            $(item).click(function(event){
                event.preventDefault();         // # 링크 이동 중지
            });
        });

        $.ajaxSetup({
        	timeout: 30000,
            error: function(jqXHR, textStatus, errorThrown) {
                try {
                    var json = $.parseJSON(jqXHR.responseText);
                    if(json.message) {
                        alert(json.message);
                        return true;
                    }
                } catch(e) { }
                 
                if(jqXHR.status==0){
                    //alert(' 네트워크 연결이 되지 않았습니다.\n 네트워크 연결 후 다시 시도해주세요.');
                } else if(jqXHR.status==403) {
                    // document.location.href='http://m.kyobobook.com/login';
                    alert(' 로그인 사용자만 이용가능한 서비스입니다.\n 로그인 후 진행해주세요.');
                } else if(jqXHR.status==404) {
                    alert('존재하지 않는 페이지이거나 잘못된 주소로 접근하셨습니다.');
                } else if(jqXHR.status==500) {
                    alert(' 네트워크 장애가 발생되었습니다.\n 잠시 후 다시 시도해주세요.\n status:500');
                } else if(errorThrown=='parsererror') {
                    alert('Error.\nParsing JSON Request failed.');
                    //alert('네트워크 장애가 발생되었습니다.\n잠시 후 다시 시도해주세요.');
                } else if(errorThrown=='timeout') {
                    alert('연결 시간이 초과되었습니다.\n잠시 후 다시 시도해주세요');
                } else if(errorThrown=='abort'){
                	alert('Ajax 요청이 거부되었습니다.');
                } else { 
                    //alert(' 네트워크 장애가 발생되었습니다.\n 잠시 후 다시 시도해주세요.\n Unknown error\n' + jqXHR.status + '\n' +  errorThrown);
                }
            }
        });
        
        var userId = cfGetCookie('userId');
    	if(userId != null && jQuery.trim(userId) != "") {
    		$('#userId').val(userId);
    	}

    	if (cfGetCookie('rememberId') == 'true') {
    		$('#rememberId')[0].checked = true;
    	} else {
    		$('#rememberId')[0].checked = false;
    	}
    	$('#userId').focus();
    	$('#submit').attr("disabled", true);
    });
    
    function fcLogin() {
    	var fm = document.loginForm;
    	if(fm.userId.value=='') {
    		cfGetMsg('sy.err.noUserId');
    		fm.userId.focus();
    		return;
    	}
    	if(fm.userPwd.value=='') {
    		cfGetMsg('sy.err.wrongPasswd');
    		fm.userPwd.focus();
    		return;
    	}

     	cfSetCookie('rememberId', $('#rememberId')[0].checked);
     	cfSetCookie('userId', $('#rememberId')[0].checked ? $('#userId').val() : '');
    	fnLoginCall();
    }
    
    function fnCheckUserNm() {
    	if(loginForm.userId.value == '') return;
    	var params = {params : {
				qId : "loginXP.chkUserLogin",
				userId : loginForm.userId.value
			}
		};
    	$.ajax({
    	    type: "post",
    	    url: gPath + '/tz/common/login/existUserId.ajax',
			data :  JSON.stringify(params),
			datatype : "json",
			contentType : "application/json; charset=utf-8",
			success : function(data) {
				var msg;
				if (data && data.success) {
					msg = data.success[0];
					if (msg && msg != '') {
						$('#submit').attr("disabled", true);
						alert(msg);
					}
				} else {
					$('#submit').attr("disabled", true);
					cfGetMsg("sy.err.noUserId");
				}
			}
		});
	}

	function fnLoginCall() {
		var userId = cfHangle(loginForm.userId.value);
		var passwd = cfHangle(loginForm.userPwd.value);
		$('#j_username').val(userId);
		$('#j_password').val(passwd);

		var frm = $('#f')[0];
		frm.submit();
	}
</script>

</head>
<body>
<div id="wraper">
	<header id="header" class="clfix">
		<!-- header inc -->
		<script type="text/javascript" src="${jsPath}/header.js"></script>
		<noscript><p>자바스크립트를 가 실행되지 않으면 제대로된 서비스를 제공받지 못합니다.</p></noscript>
		<!-- //header inc -->
	</header>

	<nav id="gnb" class="clfix">
		<!-- nav inc -->
		<script type="text/javascript" src="${jsPath}/nav.js"></script>
		<noscript><p>자바스크립트를 가 실행되지 않으면 제대로된 서비스를 제공받지 못합니다.</p></noscript>
		<!-- //nav inc -->
	</nav>

<form id='loginForm' name='loginForm' method='post'>
	<section id="container">
		<h3 class="h3_bul01">로그인</h3>
		<div class="content_wrap">
			<div class="box101 mgb20">
				<form name="" method="" action="">
					<table class="tb202">
					<colgroup>
						<col width="*" /><col width="120PX" />
					</colgroup>
					<tr>
						<td class="inputform"><input type="text" id="userId" name="userId" value="lhfadm"  onBlur="fnCheckUserNm()" class="inputTxt" title="아이디를 입력하세요" placeholder="아이디" /></td>
						<td>
							<label class="inputCheckbox">
								<span class="checkbox"></span>아이디저장
								<input type="checkbox" name="" value="" class="styled" checked="checked" />
							</label>
						</td>
					</tr>
					<tr>
						<td class="inputform"><input type="password" id="userPwd" name="userPwd" value="fhtep" class="inputTxt" title="비밀번호를 입력하세요" placeholder="비밀번호"></td>
						<td>
							<label class="inputCheckbox">
								<span class="checkbox"></span>비밀번호저장
								<input type="checkbox" id="rememberId"  name="rememberId" value="" class="styled" />
							</label>
						</td>
					</tr>
					<tr>
						<td colspan="2"><div class="clfix"><input type="text" class="btn_direct_blue bder_radius03 tcolor_w btn_sb" value="로그인"  onclick="fcLogin()"/></div></td>
					</tr>
					</table>
				</form>
			</div>
			<div class="findIdForm">
				<p class="mgb20">
					<span class="bl_so text_st_kor txt_s14">아이디가 없으세요?</span>
					<a href="" class="btn_normal08 bder_radius txt_s14">회원가입</a>
				</p>
				<p class="tcolor_g3 tcolor_g7">
					아이디/비밀번호 찾기는 PC에서 이용 가능합니다.<br />
					PC 브라우저에서는 비밀번호 저장이 되지 않습니다.
				</p>
			</div>
		</div>
	</section>
</form>

	<div id="cart_area">
		<!-- cart inc -->
		<script type="text/javascript" src="${jsPath}/cart.js"></script>
		<noscript><p>자바스크립트를 가 실행되지 않으면 제대로된 서비스를 제공받지 못합니다.</p></noscript>
		<!-- //cart inc -->
	</div>

	<footer id="footer">
		<!-- footer inc -->
		<script type="text/javascript" src="${jsPath}/footer.js"></script>
		<noscript><p>자바스크립트를 가 실행되지 않으면 제대로된 서비스를 제공받지 못합니다.</p></noscript>
		<!-- //footer inc -->
	</footer>
</div>

<form name='f' id='f' action='${pageContext.request.contextPath}/j_spring_security_check?pt=true' method='POST'>
	<input type="hidden" id="j_username" name="j_username" value="" />
	<input type="hidden" id="j_password" name="j_password" value="" />
	<input type="hidden" id="source" name="source" value="web">
	<input type="hidden" id="loclCd" name="loclCd" value="ko_KR">
</form>

</body>
</html>