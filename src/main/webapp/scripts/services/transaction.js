'use strict';
var transManager = {};
transManager.exec = function(url, type, params, callback, $http, $templateCache) {

	transManager.retrieve = function() {

		var config = {
			headers : {
				'Access-Control-Allow-Origin' : 'http://192.168.219.112:3000',
				'Access-Control-Allow-Credentials' : 'true'
			}
		};

		debugger;

		 $.ajax({
	            type: 'GET',
	            url: url,
	            contentType: 'application/json; charset=utf-8',
	            crossDomain: true,
	            dataType: 'jsonp',
	            success: function(data){
	            	debugger;
	            },
	            error : function(xhr, ajaxOptions, thrownError) {
	            	debugger;
	            }
	        });
		
//		$http({
//			method: 'JSONP',
//			url: url,
//		    headers: {
//				'Access-Control-Allow-Origin' : 'http://192.168.219.112:3000',
//				'Access-Control-Allow-Credentials' : 'true',
//				'Content-Type': 'application/json',
//				'Accept': 'application/json'
//		    }
//		}).success(function(data, status, headers, config) {
//			debugger;
//				callback(data);
//			}).error(function(data, status, headers, config) {
//				debugger;
//				data = status;
//				callback(data);
//			});
		
//		if(type == 'get') {
//			$http.get(url, config).success(function(data, status, headers, config) {
//				callback(data);
//			}).error(function(data, status) {
//				data = status;
//				callback(data);
//			});
//		} else {
//			$http.post(url, JSON.stringify(params), config).success(function(data, status, headers, config) {
//				callback(data);
//			}).error(function(data, status) {
//				data = status;
//				callback(data);
//			});
//		}
	};

	transManager.save = function() {
		$http.post(url, JSON.stringify(params)).success(function(data, status, headers, config) {
			callback(data);
		}).error(function(data, status) {
			data = status;
			callback(data);
		});
	};
	return transManager;
};