'use strict';
app.service('centersService', function($http, $location) {

	var $scope = {};
	var centersService = {};
	var baseUrl = '/pattern/pt42/masterdetail';

	centersService.init = function(scope) {
		$scope = scope;
	};

	centersService.retrieveCenters = function(callback) {
		transManager.exec(baseUrl + '/retrieveCenterList.ajax', {
			params : {
				code : ''
			}
		}, function(data) {
			$scope.dataset = {};
			$scope.dataset.centers = angular.copy(data.output1);
			if (callback)
				callback(data.output1);
		}, $http).retrieve();
	};

	centersService.retrieveRegions = function($scope, callback) {
		var code = $scope.center.code;
		transManager.exec(baseUrl + '/retrieveRegionList.ajax', {
			params : {
				centerCode : code
			}
		}, function(data) {
			for ( var i = 0; i < $scope.dataset.centers.length; i++) {
				if ($scope.dataset.centers[i].code == (code + '')) {
					$scope.dataset.regions = angular.copy(data.output1);
				}
			}
			
			if (callback)
				callback(data.output1);
		}, $http).retrieve();
	};

	centersService.saveCenter = function(callback) {
		debugger;
		var params = {};
		var centers = angular.copy($scope.dataset.centers);
		var regions = angular.copy($scope.dataset.regions);
		
		if(centers && centers.length > 0) {
			for ( var i = centers.length - 1; i >= 0; i--) {
				if (!centers[i].rowStatus || centers[i].rowStatus == 'NORMAL') {
					centers.splice(i, 1);
				}
			}
		} 
		if(regions && regions.length > 0) {
			for ( var i = centers.length - 1; i >= 0; i--) {
				if (!regions[i].rowStatus || regions[i].rowStatus == 'NORMAL') {
					regions.splice(i, 1);
				}
			}
		}
		
		params.centers = centers;
		params.regions = regions;
		
		transManager.exec(baseUrl + '/saveCenterRegion.ajax', params, function(data) {
			if (callback)
				callback(data.output1);
			$scope.dataset.centers = angular.copy($scope.centers);
			// alert(data.output1[0].ErrorCode + "/" + data.output1[0].ErrorMsg);
		}, $http).save();
	};

	centersService.getCenters = function() {
		return $scope.centers;
	};
	
	centersService.addCenter = function(code, name, chief, address, phone) {
		var chkExist = false;
		if (code) {
			for ( var i = 0; i < $scope.centers.length; i++) {
				if ($scope.centers[i].code == (code + '')) {
					$scope.centers[i].name = name;
					$scope.centers[i].chief = chief;
					$scope.centers[i].address = address;
					$scope.centers[i].phone = phone;
					$scope.newCenter = $scope.centers[i];
					
					$scope.dataset.centers[i] = angular.copy($scope.centers[i]);
					$scope.dataset.centers[i].rowStatus = 'UPDATE';
					chkExist = true;
					break;
				}
			}
		}
		if (!chkExist) {
			var topID = $scope.centers.length + 1;
			$scope.centers.push({
				code : topID,
				name : name,
				chief : chief,
				address : address,
				phone : phone
			});
			var nlength = $scope.centers.length - 1;
			$scope.dataset.centers.push($scope.centers[nlength]);
			$scope.dataset.centers[nlength].rowStatus = 'INSERT';
			$scope.newCenter = $scope.centers[nlength];
		}
		$scope.$apply();
		return $scope;
	};

	centersService.deleteCenter = function(code) {
		var centers = $scope.centers;
		for ( var i = centers.length - 1; i >= 0; i--) {
			if (centers[i].code == (code + '')) {
				$scope.dataset.centers[i].rowStatus = 'DELETE';
				centers.splice(i, 1);
				break;
			}
		}
		var regions = $scope.regions;
		for ( var i = regions.length - 1; i >= 0; i--) {
			if (regions[i].code == (code + '')) {
				$scope.dataset.regions[i].rowStatus = 'DELETE';
				break;
			}
		}
	};

	centersService.getCenter = function(code) {
		var centers = null;
		try {
			centers = $scope.centers;
		} catch (e) {
			$location.path("/centers");
			return;
		}

		for ( var i = 0; i < centers.length; i++) {
			if (centers[i].code == (code + '')) {
				centers[i].rowStatus = 'NORMAL';
				return centers[i];
			}
		}
		return null;
	};

	return centersService;
});
