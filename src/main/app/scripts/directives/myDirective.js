'use strict';

// <commbutton data="Center"/>

app.directive("commbutton", function($compile) {
	var getTemplate = function(aType, aData) {
		var template = '<button id="' + aType + aData + '" class="btn btn-primary" type="button"';
		if (aType == 'add') {
			template += ' data-ng-disabled="!new' + aData + '.name || !new' + aData + '.chief || !new' + aData
					+ '.address"';
		// } else {
		// 	template += ' data-ng-disabled="true"';
		}
		template += '>' + aType + ' ' + aData + '</button>';
		return template;
	};

	var linker = function(scope, element, attr) {
		element.html(getTemplate(attr["type"], attr["data"])).show();
		element.bind('click', function() {
			eval('scope.' + attr["type"] + attr["data"] + '()');
			return;
		});
		$compile(element.contents())(scope);
	};

	return {
		restrict : 'E',
		link : linker,
		replace : true
	};
});
