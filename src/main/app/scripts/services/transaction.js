'use strict';

function JSON_CALLBACK (data) {
	debugger;
}
var transManager = {};
transManager.exec = function(url, type, params, callback, $http, $httpBackend, $templateCache) {

	transManager.retrieve = function() {

		var config = {
			headers : {
				'Accept' : 'application/json',
				'Content-type': 'application/json',
//				'X-Requested-With' : 'XMLHttpRequest',
//				'Access-Control-Allow-Origin' : 'http://local.tz.com:7001'
//				'Access-Control-Allow-Credentials' : 'false'
			}
		};

		debugger;

//		url += '?callback=JSON_CALLBACK'; 
//		
//		 $.ajax({
//	            type: 'GET',
//	            url: url,
//	            contentType: 'application/json; charset=utf-8',
//	            crossDomain: true,
//	            dataType: 'jsonp',
//	            success: function(data){
//	            	debugger;
//	            },
//	            error : function(xhr, ajaxOptions, thrownError) {
//	            	debugger;
//	            }
//	        });
//		
//		$http.jsonp(url).success(function(data, status, headers, config) {
//			debugger;
//			callback(data);
//		}).error(function(data, status, headers, config) {
//			debugger;
//			data = status;
//			callback(data);
//		});
//		
//		function JSON_CALLBACK(data) {
//			debugger;
//		}
		
//		$http({
//			method: 'JSONP',
//			url: url,
//		    headers: {
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
		
//		$http({
//			method: 'JSONP',
//			url: url,
//		    headers: {
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
		
		if(type == 'get') {
			$http.get(url, config).success(function(data, status, headers, config) {
				debugger;
				callback(data);
			}).error(function(data, status) {
				debugger;
				data = status;
				callback(data);
			});
		} else {
			$http.post(url, JSON.stringify(params), config).success(function(data, status, headers, config) {
				debugger;
				callback(data);
			}).error(function(data, status) {
				debugger;
				data = status;
				callback(data);
			});
		}
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