'use strict';

//<contentitem content="Center"/>

app.directive("contentitem", function($compile) {
	var getTemplate = function(aType) {
		var template = '<button id="add' + aType + '" class="btn btn-primary" type="button"'
		 +' data-ng-disabled="!new'+ aType +'.name || !new'+ aType +'.chief ||'
		 +'!new'+ aType +'.address"'
		+ '    >Add ' + aType + '</button>';
		return template;
	};

	var linker = function(scope, element, attr) {
		element.html(getTemplate(attr["content"])).show();
		element.bind('click', function() {
			eval('scope.add' + attr["content"] + '()');
		});

		$compile(element.contents())(scope);
	};

	return {
		restrict : 'E',
		link : linker,
		replace : true
	};
});
