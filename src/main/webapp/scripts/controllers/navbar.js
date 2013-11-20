'use strict';

angular.module('sheepwebApp')
.controller('NavbarCtrl', function ($scope, $location) {
	
    $scope.goTo = function ( baseUrl, center ) {
    	var path = baseUrl;
    	if(baseUrl == '/') {
    	} else if(center) {
    		path += center.id;
    	} else if($scope.uip_centers[0]) {
    		path += $scope.uip_centers[0].id;
    	}
	  	$location.path( path );
	}	
});

