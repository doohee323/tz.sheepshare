<%@ include file="/jsp/common/include/doctype.jspf" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isErrorPage="true"
    errorPage="/common/error/404.html"
    session="false" %>
<%--
/**
 *---------------------------------------------------------------------------------
 * DESC : 어플리케이션에서 발생하는 에러처리 페이지(springmvc-config.xml 설정 참조)
 *---------------------------------------------------------------------------------
 */
--%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko">
<head>
<%@ include file="/jsp/common/include/Head.jspf" %>
<title>::: ERROR :::</title>
<script type="text/javascript">
//<![CDATA[
<%
    // 에러메시지 생성 작업
    String message = null;//"Unknown Error Occured.";
    // 1순위 : exception 객체, 2순위 : exception Attribute, 3순위 : Servlet Throwable
    Throwable throwable = (Throwable)request.getAttribute("javax.servlet.error.exception");
    if(exception != null) {
        message = exception.getMessage();
    } else if(request.getAttribute("exception") != null) {
        Exception exceptionAttr = (Exception)request.getAttribute("exception");
        message = exceptionAttr.getMessage();
    } else if(throwable != null && throwable.getCause() != null) {
        message = throwable.getCause().getMessage();
    } else {
        message = "MessageCodes.MSG_ERROR_DEFAULT";
    }
    if(message == null || message.isEmpty()) {
        message = "알 수 없는 오류가 발생하였습니다.";
    } else {
        message = message.replaceAll("\n","<br/>");
    }
%>
//]]>
</script>
</head>
<body id="LblockMain">
<%-- 페이지 시작 --%>
<%-- 페이지타이틀 시작 --%>
<div id="LblockPageTitle"">
    <h1>요청하신 서비스를 수행할 수 없습니다</h1>
</div><%-- 페이지타이틀 끝 --%>

<%-- 본문영역 시작 --%>
<div id="LblockMainBody">
    <p>
        요청하시려는 서비스의 주소가 잘못 입력되었거나,<br />
        긴급한 점검작업으로 요청하신 서비스를 수행할 수 없습니다.
        <br /><br />
        E-mail us at <a href="#">admin@tz.com</a><br/>
        감사합니다.
    </p>
    <br/><br/>
    <h4>에러 상세정보</h4>
    <p>
        <%=message %>
    </p>
</div><%-- 본문영역 끝 --%>

<%-- 버튼영역 시작 --%>
<div id="LblockButton">
    <span id="backBtn" class="L-button L-undefined-button">
        <span class="first-child">
            <button type="button" id="back" onclick="history.back();">이전 페이지</button>
        </span>
    </span>
</div><%-- 버튼영역 끝 --%>

<%-- 공통모듈 작업 --%>
<%@include file="/jsp/common/include/Tail.jspf"%>
<%-- 페이지 끝 --%>
</body>
</html>
