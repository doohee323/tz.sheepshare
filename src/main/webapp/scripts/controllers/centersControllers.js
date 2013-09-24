'use strict';
app.controller('CentersController', function($scope, $http, centersService) {
	init();

	function init() {
		centersService.init($scope);
		centersService.retrieveCenters(function (data) {
			$scope.centers = data;
		});
		
		$scope.$watch('centers', function(newValue, oldValue) {
			if (newValue === newValue) {
				// alert(1);
			} else {
				// alert(-1);
			}
		}, true);
	}

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

app.controller('CenterController', function($scope, $routeParams, $location, centersService) {
	$scope.newCenter = {};

	init();

	function init() {
		var code = $routeParams.code;
		if (code) {
			$scope.newCenter = centersService.getCenter(code);
		}
	}

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

app.controller('CenterRegionsController', function($scope, $routeParams, centersService) {
	$scope.center = {};
	$scope.regionsTotal = 0.00;

	init();

	function init() {
		var code = $routeParams.code;
		if (code) {
			$scope.center = centersService.getCenter(code);
		}
	}

});

app.controller('RegionsController', function($scope, centersService) {
	$scope.centers = [];

	init();

	function init() {
		$scope.centers = centersService.getCenters();
	}
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

app.controller('RegionChildController', function($scope, centersService) {
	$scope.orderby = 'regionCode';
	$scope.reverse = false;
	$scope.regionsTotal = 0;

	init();

	function init() {
		centersService.retrieveRegions($scope, function (data) {
			$scope.center.regions = data;
			if ($scope.center && $scope.center.regions) {
				$scope.regionsTotal = $scope.center.regions.length;
			}
		});
	}

	$scope.setOrder = function(orderby) {
		if (orderby === $scope.orderby) {
			$scope.reverse = !$scope.reverse;
		}
		$scope.orderby = orderby;
	};

});

var addCenter = function($scope, centersService, cu) {
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

