'use strict';

angular.module('sheepwebApp')
  .controller('CentersCtrl', function ($scope, $location, $routeParams, config, CenterService) {
	  
	$scope.$location = $location;
    $scope.newCenter = {};
    var currentid = 0;
	
	CenterService.get({}, function(data) {
	 	$scope.uip_centers = data.uip_centers;
	    if($location.$$path != '/centers') {
	    	currentid = $routeParams.id;
			lookupDs(currentid, function (row){
				$scope.newCenter = $scope.uip_centers[row];
			});
	    }
	});

    $scope.addCenter = function () {
        $scope.newCenter.id = '';
    	var params = {uip_center : $scope.newCenter};
    	if(config.server == 'spring') params = $scope.newCenter; // java
    	CenterService.save(params, function (data) {
    		console.log(data);
    		$scope.uip_centers.unshift(data.uip_center);
    	})
    };
    $scope.updateCenter = function (center) {
    	var params = {uip_center : $scope.newCenter,
    				 id : $scope.newCenter.id};
    	if(config.server == 'spring') params = params.uip_center; // java
    	delete params['key'];delete params['$$hashKey'];delete params['objectKey'];  // remove useless coluems for error fix
    	CenterService.update(params, function (data) {
    		console.log(data);
    		currentid = center.id;
    	})
    };
    $scope.deleteCenter = function (center) {
    	CenterService.delete({"id" : center.id}, function (data) {
    		console.log(data);
    		currentid = center.id;
    	})
    };

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
