<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/include/Head.jspf" %>

<body>
<%@ include file="/common/include/Navbar.jspf" %>

<div class="container">
	<input type="text" placeholder="Start typing.." class="typeahead"  data-items="3"/>
	
	<ul class="nav nav-list" id='MovieUL'>
	  <li class="nav-header">List Header!!!</li>
	  <li class="active"><a href="#">Home</a></li>
	</ul>
	
	<script id="item-template" type="text/template">
                <li><a href="#"><span><@= title @></span><@= gradeImg @></a></li>
	</script>
 </div> <!-- end container -->

<%@ include file="/common/include/Tail.jspf" %>
<script src="./moviesList2.js"></script>

</body>
</html>

