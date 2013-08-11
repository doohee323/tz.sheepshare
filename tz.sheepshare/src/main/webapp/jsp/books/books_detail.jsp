<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/include/Head.jspf" %>

<body>
<div id="wraper" data-role="page">
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
		<div class="content_wrap">
			<div class="content_wrap300">
				<!-- book title -->
				<h3 class="book_detail text_st2">아프니까 청춘이다</h3>
				<ul class="list_book_author clfix">
					<li>김난도</li>
					<li>쌤앤파커스</li>
					<li>2010.12.24</li>
				</ul>
				<!-- //book title -->
				<!-- book detail -->
				<div class="list_book_detail clfix">
					<div class="image"><a href="#"><img src="http://image.kyobobook.com/mimages/static/images/books/img_detail_01.jpg" alt="" /></a></div>
					<div class="detail_info">
						<ul>
							<li>정가 : <span class="delete">14,000원</span><a href="/pop/pop_sale_coupon.html"><span class="sale_coupon">할인쿠폰</span></a></li>
							<li>판매가 : <span class="tcolor_o txt_s16 text_st">12,600</span><span class="tcolor_o">원</span> <span class="tcolor_b">(<span class="arrow_down">10%</span>)</span></li>
							<li>마일리지 : <span>1,260원</span> <span class="tcolor_o">(10%적립)</span><a class="ico_sb" href="/pop/pop_books_mileage.html"><img src="http://image.kyobobook.com/mimages/static/images/common/ico_speech_bubble.png" alt="?" /></a></li>							
							<li>도서상태 : 품절</li>							
						</ul>
						<div class="btn_center">
							<a href="/books/purchase_step01.html" class="btn_direct_blue bder_radius03 tcolor_w" rel="external">바로구매</a>
							<a href="/pop/pop_direct_dream.html" class="btn_direct_white bder_radius03 tcolor_b">바로드림</a>
						</div>
					</div>
				</div>
				<!-- //book detail -->

				<div class="btn_sub">
					<span class="btn_share text_st_kor">공유하기</span>					
					<a class="btn_ebook">이 책의 e북</a>				
					<span class="btn_quantity">수량 : </span>
					<span class="select_quantity tcolor_b">
						<img src="http://image.kyobobook.com/mimages/static/images/common/ico_quantity.gif" alt="" class="select_img" />				
						<select>
							<option>1</option>
							<option>2</option>
							<option>11</option>
						</select>
					</span>										
				</div>
			</div>

			<!-- 20110614 신규 소스 -->
			<div class="list_btn_share" id="share_btn">
				<a>twitter</a>
				<a>facebook</a>
				<a>me2day</a>
				<a>yozm</a>
				<a>mail</a>
			</div>
			<!-- //20110614 신규 소스 -->

			<div class="box101">
				<ul class="list_deliver_info">
					<li class="txt_indent54"><span class="bl_so tcolor_o">배송비 : </span>해당 도서 포함 만원이상 구매시 <span class="tcolor_o">무료무료무료무료무료무료무료무료무료무료무료무료무료무료무료무료무료무료무료</span></li>
					<li class="txt_indent66"><span class="bl_sb tcolor_b">바로배송 : </span><!-- 20110623 수정 -->오늘(11일,수) 도착/서울·수도권 기준도착/서울·수도권 기준도착/서울·수도권 기준도착/서울·수도권 기준도착/서울·수도권 기준도착/서울·수도권 기준도착/서울·수도권 기준도착/서울·수도권 기준도착/서울·수도권 기준도착/서울·수도권 기준도착/서울·수도권 기준<!-- //20110623 수정 --><!-- 20110622 수정 --><a class="ico_sb txt_indent0" href="/pop/pop_books_arrivedate.html"><!-- //20110622 수정 --><img src="http://image.kyobobook.com/mimages/static/images/common/ico_speech_bubble.png" alt="?" /></a></li>
					<li class="txt_indent66"><span class="bl_sb tcolor_b">바로드림 : </span>주문 1시간 후 선택 영업점에서 바로드림주문 1시간 후 선택 영업점에서 바로드림주문 1시간 후 선택 영업점에서 바로드림</li>
				</ul>
			</div>

			<div class="btn_center">
				<a href="javascript:addCartItem('id', 'link', 'http://image.kyobobook.com/mimages/static/images/cartTestImg1.jpg');" class="btn_gray bder_radius02">장바구니 담기</a>
				<a class="btn_gray bder_radius02">내서재 담기</a>
			</div>

			<!-- facebook like -->
			<div class="list_facebook_like">
				<dl class="list_like_txt clfix">
					<dt><a href="#">Junhan Lee</a></dt>
					<dd>외 <span class="text_st_kor">38명</span>의 친구들이 좋아합니다.</dd>
					<dd><a href="#" class="btn_like"><img src="http://image.kyobobook.com/mimages/static/images/common/ico_like.gif" alt="좋아요" /></a></dd>
				</dl>
				<div class="list_like_image">
					<a><img src="http://image.kyobobook.com/mimages/static/images/common/img_like_01.jpg" alt="" /></a>
					<a><img src="http://image.kyobobook.com/mimages/static/images/common/img_like_02.jpg" alt="" /></a>
					<a><img src="http://image.kyobobook.com/mimages/static/images/common/img_like_03.jpg" alt="" /></a>
					<a><img src="http://image.kyobobook.com/mimages/static/images/common/img_like_04.jpg" alt="" /></a>
					<a><img src="http://image.kyobobook.com/mimages/static/images/common/img_like_02.jpg" alt="" /></a>
					<a><img src="http://image.kyobobook.com/mimages/static/images/common/img_like_03.jpg" alt="" /></a>
					<a><img src="http://image.kyobobook.com/mimages/static/images/common/img_like_01.jpg" alt="" /></a>
					<a><img src="http://image.kyobobook.com/mimages/static/images/common/img_like_05.jpg" alt="" /></a>
					<a><img src="http://image.kyobobook.com/mimages/static/images/common/img_like_03.jpg" alt="" /></a>
					<a><img src="http://image.kyobobook.com/mimages/static/images/common/img_like_02.jpg" alt="" /></a>
					<a><img src="http://image.kyobobook.com/mimages/static/images/common/img_like_04.jpg" alt="" /></a>
					<a><img src="http://image.kyobobook.com/mimages/static/images/common/img_like_05.jpg" alt="" /></a>
					<a><img src="http://image.kyobobook.com/mimages/static/images/common/img_like_02.jpg" alt="" /></a>
				</div>
			</div>
			<!-- //facebook like -->

			<!-- event -->
			<div id="eventArea">
				<ul id="eventSet">
					<li href="link1">
						<a href="" rel="external">
						<div class="box102">
							<dl class="list_event">
								<dt>&lt;스킨 다이어트&gt;출간기념 이벤트 1</dt>
								<dd>도서 구매시 선물 추첨! + 할인쿠폰 + 무료배송 혜택! 05.12. ~ 06. 20.</dd>
								<dd class="ico_event"><img src="http://image.kyobobook.com/mimages/static/images/common/ico_event.gif" alt="Event" /></dd>
							</dl>
						</div>
						</a>
					</li>
					<li href="link2" class="hide">
						<a href="" rel="external">
						<div class="box102">
							<dl class="list_event">
								<dt>&lt;스킨 다이어트&gt;출간기념 이벤트 2</dt>
								<dd>도서 구매시 선물 추첨! + 할인쿠폰 + 무료배송 혜택! 05.12. ~ 06. 20.</dd>
								<dd class="ico_event"><img src="http://image.kyobobook.com/mimages/static/images/common/ico_event.gif" alt="Event" /></dd>
							</dl>
						</div>
						</a>
					</li>
					<li href="link3" class="hide">
						<a href="/pop/pop_sale_coupon.html" rel="external">
						<div class="box102">
							<dl class="list_event">
								<dt>&lt;스킨 다이어트&gt;출간기념 이벤트 3</dt>
								<dd>도서 구매시 선물 추첨! + 할인쿠폰 + 무료배송 혜택! 05.12. ~ 06. 20.</dd>
								<dd class="ico_event"><img src="http://image.kyobobook.com/mimages/static/images/common/ico_event.gif" alt="Event" /></dd>
							</dl>
						</div>
						</a>
					</li>
				</ul>
				<!-- btn circle -->
				<ul id="btnEvent" class="btn_move_circle2">
				</ul>
				<!-- //btn circle -->
				
				<script>
					var eventSet = $('#eventSet li');
					var eventSetCnt = eventSet.length;
					var btnEvent = $('#btnEvent');
					var clickIndex = 0;
					
					// 버튼 만들기
					if(eventSetCnt > 0){
						btnEvent.append('<li><img src="http://image.kyobobook.com/mimages/static/images/common/btn_move_on.gif" alt="" /></li>');
						
						for(var i = 1 ; i < eventSetCnt ; i++){
							btnEvent.append('<li><img src="http://image.kyobobook.com/mimages/static/images/common/btn_move_off.gif" alt="" /></li>');
						}
					}
					
					// 이벤트 영역 클릭 핸들러
					eventSet.bind('click', function(){
						console.log(eventSet.eq(clickIndex).attr('href'));
						//location.href = eventSet.eq(clickIndex).attr('href');
					});
					
					// 버튼 클릭 핸들러
					$('#btnEvent li').bind('click', function(e){
						var index = $(this).index();
						var img = $(this).find('img');
						
						$('#btnEvent li img').attr('src', $(this).find('img').attr('src').replace('_on', '_off'));
						$(this).find('img').attr('src', $(this).find('img').attr('src').replace('_off', '_on'));
						
						eventSet.hide();
						eventSet.eq(index).show();
						clickIndex = index;
					});
				</script>
			</div>
			<!-- //event -->
		</div>
		
		<!-- isbn -->
		<ul class="list_isbn">
			<li>200p</li>
			<li>A5</li>
			<li>ISBN : 8960864471 9788960864474</li>
		</ul>
		<!-- //isbn -->
		
		<!-- 110707 꾸러미 추가 -->
		<div id="books_ggurumi" class="list_book_info">
			<h4><span class="title">구성 내역</span></h4>
			<ul class="list_ggurumi clfix">
				<!-- roof -->					
				<li>
					<h5 class="composition_tit text_st_kor">아이의 자존감</h5>
					<ul class="list_book_author clfix bdb0 mgb5">
						<li>EBS 아이의 사생활 제작팀</li>
						<li>지식채널</li>
						<li>2011.06</li>
					</ul>
					<div class="clfix">
						<div class="composition_img">
							<img src="http://image.kyobobook.com/mimages/static/images/books/img_book.jpg" alt="아이의 자존감" />
						</div>
						<div class="composition_data">
							<ul>
								<li>개별구매가:<span class="delete">16,800</span>원</li>
								<li><span class="tcolor_o"><span class="text_st">11,760</span>원</span><span class="tcolor_b">(<span class="arrow_down">30%</span>)</span> | 120원 <span class="tcolor_o">(1%적립)</span></li>
								<li><span class="text_st_kor">꾸러미구매가:</span><span class="delete">16,800</span>원</li>
								<li><span class="tcolor_o"><span class="text_st">10,920</span>원</span><span class="tcolor_b">(<span class="arrow_down">30%</span>)</span> | 120원 <span class="tcolor_o">(1%적립)</span></li>
							</ul>
						</div>
					</div>
				</li>
				<!-- //roof -->		
				<!-- roof -->					
				<li>
					<h5 class="composition_tit text_st_kor">아이의 자존감</h5>
					<ul class="list_book_author clfix bdb0 mgb5">
						<li>EBS 아이의 사생활 제작팀</li>
						<li>지식채널</li>
						<li>2011.06</li>
					</ul>
					<div class="clfix">
						<div class="composition_img">
							<img src="http://image.kyobobook.com/mimages/static/images/books/img_book.jpg" alt="아이의 자존감" />
						</div>
						<div class="composition_data">
							<ul>
								<li>개별구매가:<span class="delete">16,800</span>원</li>
								<li><span class="tcolor_o"><span class="text_st">11,760</span>원</span><span class="tcolor_b">(<span class="arrow_down">30%</span>)</span> | 120원 <span class="tcolor_o">(1%적립)</span></li>
								<li><span class="text_st_kor">꾸러미구매가:</span><span class="delete">16,800</span>원</li>
								<li><span class="tcolor_o"><span class="text_st">10,920</span>원</span><span class="tcolor_b">(<span class="arrow_down">30%</span>)</span> | 120원 <span class="tcolor_o">(1%적립)</span></li>
							</ul>
						</div>
					</div>
				</li>
				<!-- //roof -->		
				<!-- roof -->					
				<li>
					<h5 class="composition_tit text_st_kor">아이의 자존감</h5>
					<ul class="list_book_author clfix bdb0 mgb5">
						<li>EBS 아이의 사생활 제작팀</li>
						<li>지식채널</li>
						<li>2011.06</li>
					</ul>
					<div class="clfix">
						<div class="composition_img">
							<img src="http://image.kyobobook.com/mimages/static/images/books/img_book.jpg" alt="아이의 자존감" />
						</div>
						<div class="composition_data">
							<ul>
								<li>개별구매가:<span class="delete">16,800</span>원</li>
								<li><span class="tcolor_o"><span class="text_st">11,760</span>원</span><span class="tcolor_b">(<span class="arrow_down">30%</span>)</span> | 120원 <span class="tcolor_o">(1%적립)</span></li>
								<li><span class="text_st_kor">꾸러미구매가:</span><span class="delete">16,800</span>원</li>
								<li><span class="tcolor_o"><span class="text_st">10,920</span>원</span><span class="tcolor_b">(<span class="arrow_down">30%</span>)</span> | 120원 <span class="tcolor_o">(1%적립)</span></li>
							</ul>
						</div>
					</div>
				</li>
				<!-- //roof -->		
				<!-- roof -->					
				<li>
					<h5 class="composition_tit text_st_kor">아이의 자존감</h5>
					<ul class="list_book_author clfix bdb0 mgb5">
						<li>EBS 아이의 사생활 제작팀</li>
						<li>지식채널</li>
						<li>2011.06</li>
					</ul>
					<div class="clfix">
						<div class="composition_img">
							<img src="http://image.kyobobook.com/mimages/static/images/books/img_book.jpg" alt="아이의 자존감" />
						</div>
						<div class="composition_data">
							<ul>
								<li>개별구매가:<span class="delete">16,800</span>원</li>
								<li><span class="tcolor_o"><span class="text_st">11,760</span>원</span><span class="tcolor_b">(<span class="arrow_down">30%</span>)</span> | 120원 <span class="tcolor_o">(1%적립)</span></li>
								<li><span class="text_st_kor">꾸러미구매가:</span><span class="delete">16,800</span>원</li>
								<li><span class="tcolor_o"><span class="text_st">10,920</span>원</span><span class="tcolor_b">(<span class="arrow_down">30%</span>)</span> | 120원 <span class="tcolor_o">(1%적립)</span></li>
							</ul>
						</div>
					</div>
				</li>
				<!-- //roof -->		
				<!-- roof -->					
				<li>
					<h5 class="composition_tit text_st_kor">아이의 자존감</h5>
					<ul class="list_book_author clfix bdb0 mgb5">
						<li>EBS 아이의 사생활 제작팀</li>
						<li>지식채널</li>
						<li>2011.06</li>
					</ul>
					<div class="clfix">
						<div class="composition_img">
							<img src="http://image.kyobobook.com/mimages/static/images/books/img_book.jpg" alt="아이의 자존감" />
						</div>
						<div class="composition_data">
							<ul>
								<li>개별구매가:<span class="delete">16,800</span>원</li>
								<li><span class="tcolor_o"><span class="text_st">11,760</span>원</span><span class="tcolor_b">(<span class="arrow_down">30%</span>)</span> | 120원 <span class="tcolor_o">(1%적립)</span></li>
								<li><span class="text_st_kor">꾸러미구매가:</span><span class="delete">16,800</span>원</li>
								<li><span class="tcolor_o"><span class="text_st">10,920</span>원</span><span class="tcolor_b">(<span class="arrow_down">30%</span>)</span> | 120원 <span class="tcolor_o">(1%적립)</span></li>
							</ul>
						</div>
					</div>
				</li>
				<!-- //roof -->		
				<!-- roof -->					
				<li>
					<h5 class="composition_tit text_st_kor">아이의 자존감</h5>
					<ul class="list_book_author clfix bdb0 mgb5">
						<li>EBS 아이의 사생활 제작팀</li>
						<li>지식채널</li>
						<li>2011.06</li>
					</ul>
					<div class="clfix">
						<div class="composition_img">
							<img src="http://image.kyobobook.com/mimages/static/images/books/img_book.jpg" alt="아이의 자존감" />
						</div>
						<div class="composition_data">
							<ul>
								<li>개별구매가:<span class="delete">16,800</span>원</li>
								<li><span class="tcolor_o"><span class="text_st">11,760</span>원</span><span class="tcolor_b">(<span class="arrow_down">30%</span>)</span> | 120원 <span class="tcolor_o">(1%적립)</span></li>
								<li><span class="text_st_kor">꾸러미구매가:</span><span class="delete">16,800</span>원</li>
								<li><span class="tcolor_o"><span class="text_st">10,920</span>원</span><span class="tcolor_b">(<span class="arrow_down">30%</span>)</span> | 120원 <span class="tcolor_o">(1%적립)</span></li>
							</ul>
						</div>
					</div>
				</li>
				<!-- //roof -->					
			</ul>			
		</div>
		<!-- //110707 꾸러미 추가 -->

		<!-- book info -->
		<div class="list_book_info slideOperateClass">
			<h4><span class="title">책 소개</span></h4>
			<p>인생의 홀로서기를 시작하는 청춘을 위한 김난도 교수의 따뜻한 멘토링!</p>
			<!-- 팝업 로딩 20110613 -->
			<span href="/books/pop_books_intro.html" class="btn_view_more"><img src="http://image.kyobobook.com/mimages/static/images/common/btn_arrow_right.gif" alt=">" /></span>
			<!-- //팝업 로딩 20110613 -->
		</div>
		<!-- //book info -->

		<!-- book info -->
		<div class="list_book_info slideOperateClass">
			<h4>
				<span class="title">저자 소개</span>
				<a href="javascript:LayerSlideDown('extension01');" class="btn_normal bder_radius">관심저자 등록</a>
			</h4>

			<!-- 20110623 레이어 추가 -->
			<div class="pop_slidedown disnn" id="extension01">
				<h5 class="pop_slidedown_title text_st_kor">관심저자 등록</h5>
				<!-- pop content -->
				<div class="pop_slidedown_content">
					<ul class="list_bl_arrow02">
						<li class="txt_ls0 mg0">
							관심저자로 추가되었습니다.
						</li>
					</ul>
					<div class="btn_center">
						<a class="btn_slidedown bder_radius tcolor_b">확인</a>
						<a class="btn_slidedown bder_radius">관심저자 바로가기</a>
					</div>
				</div>
				<!-- //pop content -->
				<span class="btn_pop_close02 popSlidedownClose">닫기</span>
			</div>
			<!-- //20110623 레이어 추가 -->

			<p>저자 김난도 <br />좋은 선생이란 학생들을 꿈꾸게 만들고, 그 꿈을 이루도록 도와주는 사람이라고 믿는다.</p>
			<!-- 팝업 로딩 20110613 -->
			<span href="/books/pop_books_author.html" class="btn_view_more"><img src="http://image.kyobobook.com/mimages/static/images/common/btn_arrow_right.gif" alt=">" /></span>
			<!-- //팝업 로딩 20110613 -->
		</div>
		<!-- //book info -->

		<!-- book info -->
		<div class="list_book_info slideOperateClass">
			<h4><span class="title">목차</span></h4>
			<p>
				프롤로그 | 기억하라, 너는 눈부시게 아름답다<br />
				PART 1 그대 눈동자 속이 아니면 답은 어디에도 없다
			</p>
			<!-- 팝업 로딩 20110613 -->
			<span href="/books/pop_books_table.html" class="btn_view_more"><img src="http://image.kyobobook.com/mimages/static/images/common/btn_arrow_right.gif" alt=">" /></span>
			<!-- //팝업 로딩 20110613 -->
		</div>
		<!-- //book info -->

		<!-- book info -->
		<div class="list_book_info slideOperateClass">
			<h4><span class="title">출판사 서평</span></h4>
			<p>“시작하는 모든 존재는 늘 아프고 불안하다.
			하지만 기억하라, 그대는 눈부시게 아름답다!”</p>
			<!-- 팝업 로딩 20110613 -->
			<span href="/books/pop_books_review.html" class="btn_view_more"><img src="http://image.kyobobook.com/mimages/static/images/common/btn_arrow_right.gif" alt=">" /></span>
			<!-- //팝업 로딩 20110613 -->
		</div>
		<!-- //book info -->

		<!-- book info -->
		<div class="list_book_info slideOperateClass">

			<h4>
				<span class="title">이 책의 시리즈</span>
				<a href="javascript:LayerSlideDown('extension02');" class="btn_normal bder_radius">관심 시리즈 등록</a>
			</h4>

			<!-- 20110623 레이어 추가 -->
			<div class="pop_slidedown disnn" id="extension02">
				<h5 class="pop_slidedown_title text_st_kor">관심 시리즈 등록</h5>
				<!-- pop content -->
				<div class="pop_slidedown_content">
					<ul class="list_bl_arrow02">
						<li class="txt_ls0 mg0">
							관심 시리즈로 추가되었습니다.
						</li>
					</ul>
					<div class="btn_center">
						<a class="btn_slidedown bder_radius tcolor_b">확인</a>
						<a class="btn_slidedown bder_radius">관심 시리즈 바로가기</a>
					</div>
				</div>
				<!-- //pop content -->
				<span class="btn_pop_close02 popSlidedownClose">닫기</span>
			</div>
			<!-- //20110623 레이어 추가 -->

			<!-- 신규 소스 20110613 -->
			<div class="list_books_series">
				<a class="set_item">트렌드 코리아 2009 2010 2011 세트 (전3권)<img src="http://image.kyobobook.com/mimages/static/images/common/btn_arrow_right02.gif" alt=">" /></a>
				<a class="set_item">소비자는 무엇을 원하는가  <img src="http://image.kyobobook.com/mimages/static/images/common/btn_arrow_right02.gif" alt=">" /></a>
			</div>
			<!-- //신규 소스 20110613 -->

			<!-- 팝업 로딩 20110613 -->
			<span href="/books/pop_books_series.html" class="btn_view_more"><img src="http://image.kyobobook.com/mimages/static/images/common/btn_arrow_right.gif" alt=">" /></span>
			<!-- //팝업 로딩 20110613 -->
		</div>
		<!-- //book info -->

		<!-- 20110623 추가 -->
		<!-- book info -->
		<div class="list_book_info">
			<h4><span class="title">READ 지수란?</span></h4>
			<p class="mgb15">READ 지수는 개인이 각자의 텍스트 이해능력에 맞는 도서를 선정하여 읽을 수 있도록 도서의 난이도를 과학적으로 측정하여 부여한 지수입니다.</p>
			<span class="text_st_kor">READ 지수 : 1,250</span>
		</div>
		<!-- //book info -->
		<!-- //20110623 추가 -->

		<!-- book info -->
		<div class="list_book_info slideOperateClass">
			<h4>
				<span class="title">회원 리뷰</span>
				<span class="tcolor_o txt_s14">(총 <span class="text_st">36</span>건)</span>
			</h4>
			<dl class="info_grade">
				<dt><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt="" /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt="" /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt="" /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt="" /><img src="http://image.kyobobook.com/mimages/static/images/common/ico_star.png" alt="" /></dt>
				<dd class="first_child">내*영 님</dd>
				<dd>2011-06-13</dd>
			</dl>
			7<p class="review_title">지나간 청춘과 앞으로의 청춘을 위해</p>
			<p>아직 정착하지는 못했지만 사회 생활이란 걸 시작하고 생겨나는 크고 작은 문제와 고민에 휩싸여 방황의 언저리를 </p>
			<!-- 팝업 로딩 20110613 -->
			<span href="/books/pop_books_member_review.html" class="btn_view_more"><img src="http://image.kyobobook.com/mimages/static/images/common/btn_arrow_right.gif" alt=">" /></span>
			<!-- //팝업 로딩 20110613 -->
		</div>
		<!-- //book info -->
		
		<!-- sns -->
		<div class="sns_system">
			<img src="http://image.kyobobook.com/mimages/static/images/test_sns_review.gif" alt="" />
		</div>
		<!-- //sns -->

		<div class="btn_center">
			<a class="btn_normal02 bder_radius" href="/pop/pop_display_loc.html"><span>이 책의 영업점 진열위치</span></a>
		</div>
		
		<!-- 20110624 탑버튼 추가 -->
		<div class="btn_top">
			<span onclick="pageTop();">맨위로</span>
		</div>
		<!-- //20110624 탑버튼 추가 -->
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


