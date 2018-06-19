$(function() {
	$('#navbar').affix({
		offset: {
			top: 200
		}
	});
        
	$('#lista_entradas_dia').easyPaginate({
		paginateElement: 'article',
		elementsPerPage: 1,
		effect: 'climb',
                firstButton: false,
                lastButton: false
	});
});