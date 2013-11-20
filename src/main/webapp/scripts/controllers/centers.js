'use strict';

angular.module('sheepwebApp')
  .controller('CentersCtrl', function ($scope, $location, $routeParams, config, CenterService) {
	$scope.$location = $location;
    $scope.newCenter = {};

	CenterService.get({}, function(data) {
		debugger
	 	$scope.uip_centers = data.uip_centers;
	    if($location.$$path != '/centers') {
	    	var id = $routeParams.id;
			lookupDs(id, function (row){
				$scope.newCenter = $scope.uip_centers[row];
			});
	    }
	});

    $scope.addCenter = function () {
        $scope.newCenter.id = '';
    	var params = {uip_center : $scope.newCenter};
    	CenterService.save(params, function (data) {
            //debugger
    		$scope.uip_centers.unshift(data.uip_center);
    		console.log(data);
    	})
    }
    $scope.updateCenter = function (center) {
    	var params = {uip_center : $scope.newCenter,
    				 id : $scope.newCenter.id};
    	CenterService.update(params, function (data) {
    		console.log(data);
    		lookupDs(center.id, function (row){
				$scope.uip_centers[row] = center;
    		});
    	})
    }
    $scope.deleteCenter = function (center) {
    	CenterService.delete({"id" : center.id}, function (data) {
    		console.log(data);
    		lookupDs(center.id, function (row){
    			$scope.uip_centers.splice(row, 1);
    		});
    		$scope.newregion = {};
    	})
    }

    $scope.initCenter = function () {
    	$scope.newCenter = {};
    }

	var lookupDs = function ( id, callback ) {
    	for (var i = $scope.uip_centers.length - 1; i >= 0; i--) {
    		if ($scope.uip_centers[i].id == (id + '')) {
				callback(i);
				break;
			}
		}
	}

  });
