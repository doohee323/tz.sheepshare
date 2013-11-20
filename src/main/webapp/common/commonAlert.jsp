<%@ include file="/common/include/doctype.jspf"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%--
/**
 */
--%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/include/Head.jspf" %>
<%@ page import="tz.extend.util.ServletUtil" %>

<%
	String $domainNm = "";

	String alert = ServletUtil.getData(request, "alert").trim();
	String reload = ServletUtil.getData(request, "reload").trim();
	String href = ServletUtil.getData(request, "href").trim();
	String popup = ServletUtil.getData(request, "popup").trim();

	String sScript = "";
	int h=800;
	int w=900;
	String sPopup = "'commonAlert', 'height="+h+",width="+w+",left=10,top=10,status=no,toolbar=no,menubar=no,location=no,scrollbars=no,resizable=yes'";
	if(!alert.equals("")) {
	    alert = alert.replace("(", "[");
	    alert = alert.replace(")", "]");
	    alert = alert.replace("'", "\"");
	    sScript = "alert('" + alert + "')";
    } else if(!reload.equals("")) {
        if(!popup.equals("")) {
            sScript = "window.open('" + reload + "', " + sPopup + ")";
        } else {
            sScript = "'" + reload + "'";
        }
    } else if(!href.equals("")) {
        if(!popup.equals("")) {
            sScript = "window.open('" + href + "', " + sPopup + ")";
        } else {
            sScript = "location.href = '" + href + "'";
        }
    }
	System.out.println("commonAlert => " + sScript);
%>

<BODY bgcolor="#FFFFFF" aLink=#000000 link=#000000 text=#000000 topMargin=0 vLink=#000000>

<script>

var $domainNm = '<%=$domainNm%>';

//if($domainNm != '') {
//  document.domain = $domainNm;
//}

<%=sScript%>;
</script>

</html>



