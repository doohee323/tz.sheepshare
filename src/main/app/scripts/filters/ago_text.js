'use strict';

angular.module('sheepwebApp')
  .filter('agoText', function () {
    return function (input) {
    	moment.lang('en');
    	return moment(input).fromNow();
    };
  });
