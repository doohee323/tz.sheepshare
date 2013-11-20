<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta http-equiv="Cache-Control" content="no-cache; no-store; no-save"/>
<meta http-equiv="Pragma" content="no-cache"/>

<!--
주의 : 수정 및 삭제할 경우 에러 화면에 문제가 생길 수 있습니다.

RUI에서 Ajax응답시 404페이지 자체가 ajax응답으로 나올 수 없으므로
응답 스트림에서 meta tag정보로 404페이지 여부를 판단한다.
-->
<meta name="description" content="404Page" />

<title>::: 페이지를 찾을 수 없습니다 :::</title>
</head>
<body id="LblockMain">
<!-- 페이지 시작 -->
<!-- 페이지타이틀 시작 -->
<div id="LblockPageTitle">
	<h1>요청하신 페이지를 찾을 수 없습니다</h1>
</div><!-- 페이지타이틀 끝 -->

<!-- 본문영역 시작 -->
<div id="LblockMainBody">
    <p>
		방문하시려는 페이지의 주소가 잘못 입력되었거나,<br />
		페이지의 주소가 변경 혹은 삭제되어 요청하신 페이지를 찾을 수 없습니다.
		<br /><br />
		입력하신 주소가 정확한지 다시 한번 확인해 주시기 바랍니다.<br />
		감사합니다.
    </p>
</div><!-- 본문영역 끝 -->

<!-- 버튼영역 시작 -->
<div id="LblockButton">
    <span id="backBtn" class="L-button L-undefined-button">
        <span class="first-child">
            <button type="button" id="back" onclick="history.back();">이전 페이지</button>
        </span>
    </span>
</div><!-- 버튼영역 끝 -->
<!-- 페이지 끝 -->
</body>
</html>
