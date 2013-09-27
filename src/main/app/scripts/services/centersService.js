'use strict';
app.service('centersService', function() {

	var transManager;
	var $rootScope;
	var $scope;
	var $http;
	var $location;

	/**
	 * @param _transManager,
	 *            _$scope, _$http, _$location
	 * @desc 변수 초기화
	 */
	this.init = function(_$rootScope) {
		$rootScope = _$rootScope;
		$scope = $rootScope.$$childTail;
		$http = $rootScope.$http;
		$location = $rootScope.$location;
		transManager = $rootScope.transManager;
		transManager.init($http);
	};

	this.retrieveCenters = function(callback) {
		var url = '/uip_centers.json';
		var type = 'get';
		if (config.server == 'spring') {
			url = '/retrieveCenterList.ajax';
			type = 'post';
		}
		transManager.retrieve(config.url + url, type, {
			params : {
				code : ''
			}
		}, function(data) {
			if (config.server == 'spring')
				data = data.output1;
			$rootScope.centers = angular.copy(data);
			if (callback)
				callback(data);
		});
	};

	this.retrieveRegions = function(callback) {
		var url = '/uip_regions.json';
		var type = 'get';
		if (config.server == 'spring') {
			url = '/retrieveRegionList.ajax';
			type = 'post';
		}
		var code = $scope.center.code;
		transManager.retrieve(config.url + url, type, {
			params : {
				code : ''
			}
		}, function(data) {
			if (config.server == 'spring')
				data = data.output1;
			for (var i = 0; i < $rootScope.centers.length; i++) {
				if ($rootScope.centers[i].code == (code + '')) {
					$rootScope.regions = angular.copy(data);
				}
			}
			if (callback)
				callback(data);
		});
	};

	this.saveCenter = function(callback) {
		var params = {};
		params = this.setParam($rootScope.centers, params, 'uip_center');
		params = this.setParam($rootScope.regions, params, 'uip_region');

		var url = '/uip_centers/11.json';
		var type = 'PATCH';
		if (config.server == 'spring') {
			url = '/saveCenterRegion.ajax';
			type = 'post';
		}
		transManager.save(config.url + url, type, params, function(data) {
			if (config.server == 'spring')
				data = data.output1;
			if (callback)
				callback(data);
			$rootScope.centers = angular.copy($scope.centers);
		});
	};

	this.addCenter = function(code, name, chief, address, phone) {
		var chkExist = false;
		if (code) {
			for (var i = 0; i < $rootScope.centers.length; i++) {
				if ($rootScope.centers[i].code == (code + '')) {
					$rootScope.centers[i].name = name;
					$rootScope.centers[i].chief = chief;
					$rootScope.centers[i].address = address;
					$rootScope.centers[i].phone = phone;
					$scope.newCenter = $rootScope.centers[i];

					$rootScope.centers[i].rowStatus = 'UPDATE';
					chkExist = true;
					break;
				}
			}
		}
		if (!chkExist) {
			var topID = $rootScope.centers.length + 1;
			$rootScope.centers.push({
				code : topID,
				name : name,
				chief : chief,
				address : address,
				phone : phone
			});
			var nlength = $rootScope.centers.length - 1;
			$rootScope.centers[nlength].rowStatus = 'INSERT';
			$scope.centers = angular.copy($rootScope.centers);
		}
		$scope.$apply();
		return $scope;
	};

	this.deleteCenter = function(code) {
		var centers = $rootScope.centers;
		for (var i = centers.length - 1; i >= 0; i--) {
			if (centers[i].code == (code + '')) {
				$rootScope.centers[i].rowStatus = 'DELETE';
				$scope.centers.splice(i, 1);
				break;
			}
		}
		var regions = $rootScope.regions;
		for (var i = regions.length - 1; i >= 0; i--) {
			if (regions[i].code == (code + '')) {
				$rootScope.regions[i].rowStatus = 'DELETE';
				$scope.regions.splice(i, 1);
				break;
			}
		}
	};

	this.getCenter = function(code) {
		var centers = $rootScope.centers;
		for (var i = 0; i < centers.length; i++) {
			if (centers[i].code == (code + '')) {
				centers[i].rowStatus = 'NORMAL';
				return centers[i];
			}
		}
		return null;
	};

	this.setParam = function(source, target, obj) {
		var datas = angular.copy(source);
		if (datas && datas.length > 0) {
			for (var i = datas.length - 1; i >= 0; i--) {
				if (!datas[i].rowStatus || datas[i].rowStatus == 'NORMAL') {
					datas.splice(i, 1);
				}
			}
		}
		if (Object.keys(datas).length > 0) {
			target[obj] = datas;
		}
		return target;
	};
});
