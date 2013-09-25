<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/include/Head.jspf" %>

<body>
<%@ include file="/common/include/Navbar.jspf" %>

<div class="container'>
<%@ include file="/common/include/Subhead.jspf" %>

	<section id="login">
		<div class="page-header">
			<h1>Login</h1>
		</div>
		<!-- Headings & Paragraph Copy -->
		<div class="row-fluid member-container">
			<div id="content" class="span12">
				<form id='loginForm' name='loginForm' method='post'>
					<ul id="loginUL">
						<div id="loginDiv">
							<input placeholder="ID" type="text" id="userId" value="lhfadm" />
							<input placeholder="PSWD" type="text" id="userPwd" value="fhtep" />
							<button id="add" type="submit" class="btn">Sign in</button>
						</div>
					</ul>
				</form>
				<script id="template" type="text/template">
     					<span><@= msg @></span>
   				</script>
			</div>
		</div>
	</section>

	<section id="downloadapp">
		<div id="downloadapp">
			<header>
				<h1>Download list</h1>
				<input id="new-download" type="text"
					placeholder="What do you want to download?">
			</header>
			<section id="main">
				<input id="toggle-all" type="checkbox"> 
				<label for="toggle-all">Mark all</label>
				<ul id="download-list"></ul>
			</section>
			<footer>
				<a id="clear-downloaded">Clear item</a>
				<div id="download-count"></div>
			</footer>
		</div>
	</section>

	<form name='f' id='f' action='j_spring_security_check?pt=true'
		method='POST'>
		<input type="hidden" id="j_username" name="j_username" /> <input
			type="hidden" id="j_password" name="j_password" value="" /> <input
			type="hidden" id="source" name="source" value="web"> <input
			type="hidden" id="loclCd" name="loclCd" value="ko_KR">
	</form>

	<!-- Templates -->
	<script id="item-template" type="text/template">
	    <div class="view">
	      <input class="toggle" type="checkbox" <@= done ? 'checked="checked"' : '' @> />
	      <label><@- title @></label>
	      <a class="destroy"></a>
	    </div>
	    <input class="edit" type="text" value="<@- title @>" />
  	</script>

	<script type="text/template" id="stats-template">
    <@ if (done) { @>
      <a id="clear-downloaded">Clear <@= done @> downloaded <@= done == 1 ? 'item' : 'items' @></a>
    <@ } @>
    <div class="download-count"><b><@= remaining @></b> <@= remaining == 1 ? 'item' : 'items' @> left</div>
  	</script>
</div> <!-- end container -->

<%@ include file="/common/include/Tail.jspf" %>

<script src="${pageContext.request.contextPath}/backbonejs/common/js/main.js"></script>
<%-- <script src="${pageContext.request.contextPath}/backbonejs/common/js/downloads.js"></script> --%>

</body>
</html>
  
  
  