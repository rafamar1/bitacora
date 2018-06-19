$(function() {
	$('#navbar').affix({
		offset: {
			top: 200
		}
	});
/*
	$("pre.html").snippet("html", {style:'matlab'});
	$("pre.css").snippet("css", {style:'matlab'});
	$("pre.javascript").snippet("javascript", {style:'matlab'});*/

	$('#lista_entradas_dia').easyPaginate({
		paginateElement: 'article',
		elementsPerPage: 1,
		effect: 'climb'
	});
});