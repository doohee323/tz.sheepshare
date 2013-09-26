'use strict';

var headers = {
		'Accept' : 'application/json',
		'Content-type' : 'application/json',
};

var transManager = {};
transManager.exec = function(url, type, params, callback, $http, $httpBackend, $templateCache) {
	var input = JSON.stringify(params);

	transManager.retrieve = function() {
		$http({
			method : type,
			url : url,
			params : input,
			data : input,
			headers : headers
		}).success(function(data, status, headers, config) {
			debugger;
			callback(data);
		}).error(function(data, status, headers, config) {
			debugger;
			data = status;
			callback(data);
		});

//		if (type == 'get') {
//			$http.get(url, config).success(function(data, status, headers, config) {
//				debugger;
//				callback(data);
//			}).error(function(data, status) {
//				debugger;
//				data = status;
//				callback(data);
//			});
//		} else {
//			$http.post(url, JSON.stringify(params), config).success(function(data, status, headers, config) {
//				debugger;
//				callback(data);
//			}).error(function(data, status) {
//				debugger;
//				data = status;
//				callback(data);
//			});
//		}

		// url += '?callback=JSON_CALLBACK';
		//		
		// $.ajax({
		// type: 'GET',
		// url: url,
		// contentType: 'application/json; charset=utf-8',
		// crossDomain: true,
		// dataType: 'jsonp',
		// success: function(data){
		// debugger;
		// },
		// error : function(xhr, ajaxOptions, thrownError) {
		// debugger;
		// }
		// });
		//		
		// $http.jsonp(url).success(function(data, status, headers, config) {
		// debugger;
		// callback(data);
		// }).error(function(data, status, headers, config) {
		// debugger;
		// data = status;
		// callback(data);
		// });
		//		
		// function JSON_CALLBACK(data) {
		// debugger;
		// }

	};

	transManager.save = function() {
		$http({
			method : type,
			url : url,
			data : input,
			headers : headers
		}).success(function(data, status, headers, config) {
			debugger;
			callback(data);
		}).error(function(data, status, headers, config) {
			debugger;
			data = status;
			callback(data);
		});
	};
	return transManager;
};