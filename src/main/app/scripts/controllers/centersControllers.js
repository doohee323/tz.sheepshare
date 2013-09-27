'use strict';

app.controller('CentersController', function($rootScope, $scope, $http, $location, config, transManager, centersService) {

	$rootScope.$http = $http;
	$rootScope.$location = $location;

	$rootScope.centers = {};
	$rootScope.regions = {};
	$rootScope.config = config;
	$rootScope.transManager = transManager;
	$rootScope.centersService = centersService;

	centersService.init($rootScope);
	centersService.retrieveCenters(function(data) {
		$scope.centers = data;
		$rootScope.centers = angular.copy(data);
	});

	$scope.$watch('centers', function(newValue, oldValue) {
		if (newValue === newValue) {
			// alert(1);
		} else {
			// alert(-1);
		}
	}, true);

	$scope.addCenter = function() {
		addCenter($scope, centersService, 'C');
	};

	$scope.deleteCenter = function(code) {
		centersService.deleteCenter(code);
	};

	$scope.saveCenter = function() {
		centersService.saveCenter();
	};
});

app.controller('CenterController', function($rootScope, $scope, $location, $routeParams, transManager, centersService) {
	centersService.init($rootScope);

	$scope.newCenter = {};
	$scope.newCenter = centersService.getCenter($routeParams.code);

	$scope.addCenter = function() {
		addCenter($scope, centersService, 'U');
	};

	$scope.listCenter = function() {
		$location.path("/centers");
	};

	$scope.saveCenter = function() {
		centersService.saveCenter();
	};
});

app.controller('CenterRegionsController', function($scope, centersService) {
	$scope.center = {};
	$scope.regionsTotal = 0.00;

	$scope.center = centersService.getCenter($routeParams.code);
});

app.controller('NavbarController', function($scope, $location) {
	$scope.getClass = function(path) {
		if ($location.path().substr(0, path.length) == path) {
			return true;
		} else {
			return false;
		}
	};
});

app.controller('RegionChildController', function($scope, $http, $location, transManager, centersService) {
	centersService.init(transManager, $scope, $http, $location);

	$scope.orderby = 'regionCode';
	$scope.reverse = false;
	$scope.regionsTotal = 0;

	centersService.retrieveRegions($scope, function(data) {
		$scope.center.regions = data;
		if ($scope.center && $scope.center.regions) {
			$scope.regionsTotal = $scope.center.regions.length;
		}
	});

	$scope.setOrder = function(orderby) {
		if (orderby === $scope.orderby) {
			$scope.reverse = !$scope.reverse;
		}
		$scope.orderby = orderby;
	};
});

function addCenter($scope, centersService, cu) {
	var code = $scope.newCenter.code;
	var name = $scope.newCenter.name;
	var chief = $scope.newCenter.chief;
	var address = $scope.newCenter.address;
	var phone = $scope.newCenter.phone;
	centersService.addCenter(code, name, chief, address, phone);

	if (cu == '') {
		$scope.newCenter.code = '';
		$scope.newCenter.name = '';
		$scope.newCenter.chief = '';
		$scope.newCenter.address = '';
		$scope.newCenter.phone = '';
	}
};
