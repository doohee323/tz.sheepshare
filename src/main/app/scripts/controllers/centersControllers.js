'use strict';

app.controller('CentersController', function($rootScope, $scope, $http, $location, $routeParams, config, transManager, centersService) {

	$rootScope.centers = {};
	$rootScope.regions = {};

	var params = getParams(this.constructor);
	if(!$rootScope.$http) {
		for(var p in params) {
			if(!angular.equals(params[p], "$rootScope")) {
				$rootScope[params[p]] = eval(params[p]);
			}
		}
	}

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
		addCenter($scope, centersService, 'INSERT');
	};

	$scope.deleteCenter = function(code) {
		centersService.deleteCenter(code);
	};

	$scope.saveCenter = function() {
		centersService.saveCenter();
	};
});

app.controller('CenterController', function($rootScope, $scope, $http, $location, $routeParams, config, transManager, centersService) {
	debugger;
	var params = getParams(this.constructor);
	if(!$rootScope.$http) {
		for(var p in params) {
			if(!angular.equals(params[p], "$rootScope")) {
				$rootScope[params[p]] = eval(params[p]);
			}
		}
	}
	
	centersService.init($rootScope);

	$scope.newCenter = {};
	$scope.newCenter = centersService.getCenter($routeParams.code);

	$scope.addCenter = function() {
		addCenter($scope, centersService, 'UPDATE');
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

function addCenter($scope, centersService, rowStatus) {
	var code = $scope.newCenter.code;
	var name = $scope.newCenter.name;
	var chief = $scope.newCenter.chief;
	var address = $scope.newCenter.address;
	var phone = $scope.newCenter.phone;
	centersService.addCenter(code, name, chief, address, phone);

	if (rowStatus == '') {
		$scope.newCenter.code = '';
		$scope.newCenter.name = '';
		$scope.newCenter.chief = '';
		$scope.newCenter.address = '';
		$scope.newCenter.phone = '';
	}
};

function getParams(func){
    var str=func.toString();
    var len = str.indexOf("(");
    return str.substr(len+1,str.indexOf(")")-len -1).replace(/ /g,"").split(',');
}
