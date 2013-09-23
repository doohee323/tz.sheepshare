<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/include/Head.jspf" %>

<SCRIPT LANGUAGE="JavaScript">

alert('로긴됨!');

</SCRIPT>

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

	<section id="container">
		<ul class="list_type">
			<li class="on"><a href="books_list.jsp"><img src="http://image.kyobobook.com/mimages/static/images/books/ico_listType01.png" alt="" /></a></li>
			<li><a href="books_list02.html"><img src="http://image.kyobobook.com/mimages/static/images/books/ico_listType02.png" alt="" /></a></li>
		</ul>
		<ul class="list_type02">
			<li class="ct_view">
				<a class="text_st2 list"><div class="loadingIndicator"></div><img class="retina list_img" src="http://image.kyobobook.com/mimages/static/images/common/ico_book_list.png" alt="" />베스트셀러</a>
				<ol class="book_info">
					<li>
						<a href="./books_detail.jsp">
							<dl>
								<dt><span class="grade_num first">1위</span><span class="title">아프니까 청춘이다</span></dt>
								<dd class="books_img"><img src="http://image.kyobobook.com/mimages/static/images/books/img_book.jpg" alt="" /></dd>
								<dd class="books_detail">
									<ul>
										<li class="write"><span class="name">김난도</span><span class="publisher">샘앤파커스</span></li>
										<li class="price"><span class="p_price text_st">12,600</span><span class="won">원</span>(<span class="p_sale">10%</span> + <span class="p_point">1,260</span>원)</li>
										<li class="grade_img"><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star.png" alt=""  /><span class="review">회원리뷰<span class="text_st point">87</span>개</span></li>
										<li class="facebook"><img src="http://image.kyobobook.com/mimages/static/images/common/ico_facebook.gif" alt="좋아요" /><span class="review"><span>116</span></span></li>
									</ul>
								</dd>
							</dl>
						</a>
					</li>
					<li>
						<a href="./books_detail.jsp">
							<dl>
								<dt><span class="grade_num">2위</span><span class="title">엄마를 부탁해</span></dt>
								<dd class="books_img"><img src="http://image.kyobobook.com/mimages/static/images/books/img_book.jpg" alt="" /></dd>
								<dd class="books_detail">
									<ul>
										<li class="write"><span class="name">신경숙</span><span class="publisher">창비</span></li>
										<li class="price"><span class="p_price text_st">8,000</span><span class="won">원</span>(<span class="p_sale">20%</span> + <span class="p_point">400</span>원)</li>
										<li class="grade_img"><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star.png" alt=""  /><span class="review">회원리뷰<span class="text_st point">399</span>개</span></li>
										<li class="facebook"><img src="http://image.kyobobook.com/mimages/static/images/common/ico_facebook.gif" alt="좋아요" /><span class="review"><span>1</span></span></li>
									</ul>
								</dd>
							</dl>
						</a>
					</li>
					<li class="more_view"><a href=""><div class="list_more" onclick="moreViewClick();"><div class="loadingIndicatorMore"></div><b>5개</b> 더보기 <span class="tcolor_o">(<b>4</b>/253)</span></div></a></li>
				</ol>
			</li>
			<li class="ct_view">
				<a class="text_st2 list"><div class="loadingIndicator"></div><img class="retina list_img" src="http://image.kyobobook.com/mimages/static/images/common/ico_book_list03.png" alt="" />특가 도서</a>
				<ol class="book_info">
					<li>
						<a href="./books_detail.jsp">
							<dl>
								<dt><span class="title">아프니까 청춘이다</span></dt>
								<dd class="books_img"><img src="http://image.kyobobook.com/mimages/static/images/books/img_book.jpg" alt="" /></dd>
								<dd class="books_detail">
									<ul>
										<li class="write"><span class="name">김난도</span><span class="publisher">샘앤파커스</span></li>
										<li class="price"><span class="p_price text_st">12,600</span><span class="won">원</span>(<span class="p_sale">10%</span> + <span class="p_point">1,260</span>원)</li>
										<li class="grade_img"><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star.png" alt=""  /><span class="review">회원리뷰<span class="text_st point">87</span>개</span></li>
										<li class="facebook"><img src="http://image.kyobobook.com/mimages/static/images/common/ico_facebook.gif" alt="좋아요" /><span class="review"><span>116</span></span></li>
									</ul>
								</dd>
							</dl>
						</a>
					</li>
					<li>
						<a href="./books_detail.jsp">
							<dl>
								<dt><span class="title">엄마를 부탁해</span></dt>
								<dd class="books_img"><img src="http://image.kyobobook.com/mimages/static/images/books/img_book.jpg" alt="" /></dd>
								<dd class="books_detail">
									<ul>
										<li class="write"><span class="name">신경숙</span><span class="publisher">창비</span></li>
										<li class="price"><span class="p_price text_st">8,000</span><span class="won">원</span>(<span class="p_sale">20%</span> + <span class="p_point">400</span>원)</li>
										<li class="grade_img"><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star.png" alt=""  /><span class="review">회원리뷰<span class="text_st point">399</span>개</span></li>
										<li class="facebook"><img src="http://image.kyobobook.com/mimages/static/images/common/ico_facebook.gif" alt="좋아요" /><span class="review"><span>1</span></span></li>
									</ul>
								</dd>
							</dl>
						</a>
					</li>
					<li class="more_view"><a href=""><div class="list_more" onclick="moreViewClick();"><div class="loadingIndicatorMore"></div><b>5개</b> 더보기 <span class="tcolor_o">(<b>4</b>/253)</span></div></a></li>
				</ol>
			</li>
			<li class="ct_view">
				<a class="text_st2 list"><div class="loadingIndicator"></div><img class="retina list_img" src="http://image.kyobobook.com/mimages/static/images/common/ico_book_list04.png" alt="" />이벤트 도서</a>
			</li>
			<li class="ct_view">
				<a class="text_st2 list"><div class="loadingIndicator"></div><img class="retina list_img" src="http://image.kyobobook.com/mimages/static/images/common/ico_book_list05.png" alt="" />대학교재</a>
				<!-- 대학교제 서브 컨텐츠 -->
				<ol class="book_info">
					<li>
						<form name="" method="" action="">
							<table class="tb102" summary="대학교재 찾기 폼">
								<colgroup><col width="30%" /><col width="35%" /><col width="35%" /></colgroup>
								<tr>
									<th class="txt_left"><span class="bl_sg">대학명</span></th>
									<td colspan="2">
										<label class="inputSelect pdt0 txt_left">
											<span class="select">대학선택</span>
											<select class="styled">
												<option value="대학선택">대학선택</option>																																		
											</select>						
										</label>
									</td>
								</tr>
								<tr>
									<th class="txt_left"><span class="bl_sg">연도/학기</span></th>
									<td>
										<label class="inputSelect pdt0 txt_left">
											<span class="select">2011</span>
											<select class="styled">
												<option value="2011">2011</option>																																		
											</select>						
										</label>
									</td>
									<td>
										<label class="inputSelect pdt0 txt_left">
											<span class="select">1학기</span>
											<select class="styled">
												<option value="1학기">1학기</option>																															
												<option value="2학기">2학기</option>																															
											</select>						
										</label>
									</td>
								</tr>
								<tr>
									<th class="txt_left"><span class="bl_sg">개설학부</span></th>
									<td>
										<label class="inputSelect pdt0 txt_left">
											<span class="select">계열선택</span>
											<select class="styled">
												<option value="계열선택">계열선택</option>																																		
											</select>						
										</label>
									</td>
									<td>
										<label class="inputSelect pdt0 txt_left">
											<span class="select">학과선택</span>
											<select class="styled">
												<option value="학과선택">학과선택</option>																															
											</select>						
										</label>
									</td>
								</tr>
								<tr>
									<th class="txt_left"><span class="bl_sg">교수명</span></th>
									<td colspan="2">
										<input type="text" name="" value="" class="inputTxt" title="교수명" />
									</td>
								</tr>
								<tr>
									<th></th>
									<td colspan="2"><a class="btn_util02 bder_radius mgb5 text_st_kor" id="uSearchStart">대학교재 찾기</a></td>
								</tr>
							</table>
						</form>
					</li>
				</ol>
				<!-- //대학교제 서브 컨텐츠 -->
				
				<!-- 대학교제 검색결과 노출 -->
				<div id="uSearchResult" style="display:none">				
				<ol class="book_info">
					<li><div class="book_info_uSearchTitile"><span class="tcolor_o">2011년 2학기</span>수강과목 및 교재정보입니다.</div></li>
					<li>
						<a href="./books_detail.jsp">
							<dl>
								<dt><span class="title">아프니까 청춘이다2</span></dt>
								<dd class="books_img"><img src="http://image.kyobobook.com/mimages/static/images/books/img_book.jpg" alt="" /></dd>
								<dd class="books_detail">
									<ul>
										<li class="write"><span class="name">김난도</span><span class="publisher">샘앤파커스</span></li>
										<li class="price"><span class="p_price text_st">12,600</span><span class="won">원</span>(<span class="p_sale">10%</span> + <span class="p_point">1,260</span>원)</li>
										<li class="grade_img"><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star.png" alt=""  /><span class="review">회원리뷰<span class="text_st point">87</span>개</span></li>
										<li class="facebook"><img src="http://image.kyobobook.com/mimages/static/images/common/ico_facebook.gif" alt="좋아요" /><span class="review"><span>116</span></span></li>
									</ul>
								</dd>
							</dl>
						</a>
					</li>
					<li>
						<a href="./books_detail.jsp">
							<dl>
								<dt><span class="title">아프니까 청춘이다2</span></dt>
								<dd class="books_img"><img src="http://image.kyobobook.com/mimages/static/images/books/img_book.jpg" alt="" /></dd>
								<dd class="books_detail">
									<ul>
										<li class="write"><span class="name">김난도</span><span class="publisher">샘앤파커스</span></li>
										<li class="price"><span class="p_price text_st">12,600</span><span class="won">원</span>(<span class="p_sale">10%</span> + <span class="p_point">1,260</span>원)</li>
										<li class="grade_img"><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star.png" alt=""  /><span class="review">회원리뷰<span class="text_st point">87</span>개</span></li>
										<li class="facebook"><img src="http://image.kyobobook.com/mimages/static/images/common/ico_facebook.gif" alt="좋아요" /><span class="review"><span>116</span></span></li>
									</ul>
								</dd>
							</dl>
						</a>
					</li>
					<li class="more_view"><a href=""><div class="list_more" onclick="moreViewClick();"><div class="loadingIndicatorMore"></div><b>5개</b> 더보기 <span class="tcolor_o">(<b>4</b>/253)</span></div></a></li>
				</ol>
				</div>
				<!-- //대학교제 검색결과 노출 -->
			</li>
			<li class="ct_view">
				<a class="text_st2 list"><div class="loadingIndicator"></div><img class="retina list_img" src="http://image.kyobobook.com/mimages/static/images/common/ico_book_list06.png" alt="" />전체 카테고리</a>
				<!-- 전체 카테고리의 서브 카테고리 -->
				<ul class="book_info">
					<li class="sub"><a class="text_st2"><div class="loadingIndicatorSub"></div>소설</a>
						<ol class="book_info">
							<li>
								<a href="./books_detail.jsp">
									<dl>
										<dt><span class="title">아프니까 청춘이다2</span></dt>
										<dd class="books_img"><img src="http://image.kyobobook.com/mimages/static/images/books/img_book.jpg" alt="" /></dd>
										<dd class="books_detail">
											<ul>
												<li class="write"><span class="name">김난도</span><span class="publisher">샘앤파커스</span></li>
												<li class="price"><span class="p_price text_st">12,600</span><span class="won">원</span>(<span class="p_sale">10%</span> + <span class="p_point">1,260</span>원)</li>
												<li class="grade_img"><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star.png" alt=""  /><span class="review">회원리뷰<span class="text_st point">87</span>개</span></li>
												<li class="facebook"><img src="http://image.kyobobook.com/mimages/static/images/common/ico_facebook.gif" alt="좋아요" /><span class="review"><span>116</span></span></li>
											</ul>
										</dd>
									</dl>
								</a>
							</li>
							<li>
								<a href="./books_detail.jsp">
									<dl>
										<dt><span class="title">아프니까 청춘이다2</span></dt>
										<dd class="books_img"><img src="http://image.kyobobook.com/mimages/static/images/books/img_book.jpg" alt="" /></dd>
										<dd class="books_detail">
											<ul>
												<li class="write"><span class="name">김난도</span><span class="publisher">샘앤파커스</span></li>
												<li class="price"><span class="p_price text_st">12,600</span><span class="won">원</span>(<span class="p_sale">10%</span> + <span class="p_point">1,260</span>원)</li>
												<li class="grade_img"><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star.png" alt=""  /><span class="review">회원리뷰<span class="text_st point">87</span>개</span></li>
												<li class="facebook"><img src="http://image.kyobobook.com/mimages/static/images/common/ico_facebook.gif" alt="좋아요" /><span class="review"><span>116</span></span></li>
											</ul>
										</dd>
									</dl>
								</a>
							</li>
							<li class="more_view"><a href=""><div class="list_more" onclick="moreViewClick();"><div class="loadingIndicatorMore"></div><b>5개</b> 더보기 <span class="tcolor_o">(<b>4</b>/253)</span></div></a></li>
						</ol>
					</li>
					<li class="sub"><a class="text_st2"><div class="loadingIndicatorSub"></div>경제/경영</a>
						<ol class="book_info">
							<li>
								<a href="./books_detail.jsp">
									<dl>
										<dt><span class="title">아프니까 청춘이다2</span></dt>
										<dd class="books_img"><img src="http://image.kyobobook.com/mimages/static/images/books/img_book.jpg" alt="" /></dd>
										<dd class="books_detail">
											<ul>
												<li class="write"><span class="name">김난도</span><span class="publisher">샘앤파커스</span></li>
												<li class="price"><span class="p_price text_st">12,600</span><span class="won">원</span>(<span class="p_sale">10%</span> + <span class="p_point">1,260</span>원)</li>
												<li class="grade_img"><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star.png" alt=""  /><span class="review">회원리뷰<span class="text_st point">87</span>개</span></li>
												<li class="facebook"><img src="http://image.kyobobook.com/mimages/static/images/common/ico_facebook.gif" alt="좋아요" /><span class="review"><span>116</span></span></li>
											</ul>
										</dd>
									</dl>
								</a>
							</li>
							<li>
								<a href="./books_detail.jsp">
									<dl>
										<dt><span class="title">아프니까 청춘이다2</span></dt>
										<dd class="books_img"><img src="http://image.kyobobook.com/mimages/static/images/books/img_book.jpg" alt="" /></dd>
										<dd class="books_detail">
											<ul>
												<li class="write"><span class="name">김난도</span><span class="publisher">샘앤파커스</span></li>
												<li class="price"><span class="p_price text_st">12,600</span><span class="won">원</span>(<span class="p_sale">10%</span> + <span class="p_point">1,260</span>원)</li>
												<li class="grade_img"><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star.png" alt=""  /><span class="review">회원리뷰<span class="text_st point">87</span>개</span></li>
												<li class="facebook"><img src="http://image.kyobobook.com/mimages/static/images/common/ico_facebook.gif" alt="좋아요" /><span class="review"><span>116</span></span></li>
											</ul>
										</dd>
									</dl>
								</a>
							</li>
							<li class="more_view"><a href=""><div class="list_more" onclick="moreViewClick();"><div class="loadingIndicatorMore"></div><b>5개</b> 더보기 <span class="tcolor_o">(<b>4</b>/253)</span></div></a></li>
						</ol>
					</li>
					<li class="sub"><a class="text_st2"><div class="loadingIndicatorSub"></div>자기개발</a></li>
					<li class="sub"><a class="text_st2"><div class="loadingIndicatorSub"></div>인문</a></li>
					<li class="sub"><a class="text_st2"><div class="loadingIndicatorSub"></div>역사/문화</a></li>
				</ul>
				<!-- //전체 카테고리의 서브 카테고리 -->
			</li>
		</ul>
	</section>

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

</body>
</html>