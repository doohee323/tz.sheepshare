<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/include/Head.jspf" %>

<body>
<%@ include file="/common/include/Navbar.jspf" %>

<div class="container'>
	<input type="text" placeholder="Start typing.." class="typeahead"  data-items="3"/>
	
	<ul class="accordion" id="accordion2">
		<div id="MovieUL"/>
	</ul>
	<script id="item-template" type="text/template">
		<div class="accordion-group">
			<div class="accordion-heading">
				<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapse<@= collapse @>">
					<span><@= grade @></span><span class="title"><@= title @></span>
				</a>
			</div>
			<div id="collapse<@= collapse @>" class="accordion-body collapse">
				<div class="accordion-inner">
					<ul>
						<li class="write">
							<span class="writer"><@= writer @></span>
							<span class="publisher"><@= publisher @></span>
						</li>
						<li class="price">
							<span class="p_price text_st"><@= price @></span><span class="won">원</span>
						</li>
						<li class="grade_img">
							<@= gradeImg @>
							<span class="review">
								회원리뷰
								<span class="text_st point"><@= reviewCnt @></span>개
							</span>
						</li>
						<li class="facebook">
							<img src="http://image.kyobobook.com/mimages/static/images/common/ico_facebook.gif" alt="좋아요" />
							<span class="review"><span><@= facebookLikeCnt @></span></span>
						</li>
					</ul>
				</div>
			</div>
		</div>
	</script>
</div> <!-- end container -->

<%@ include file="/common/include/Tail.jspf" %>
<script src="./moviesList.js"></script>

</body>
</html>
  
  
  