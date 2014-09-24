$(function(){
	$('.only-on-js').show();

	$('.master-checkbox').change(function(){
		var colIndex = $(this).parents('th, td').index();
		var checked = this.checked;

		var childBoxes = $(this).parents('table').find('td:nth-child(' + (colIndex + 1) + ') input[type="checkbox"]');

		childBoxes.prop('checked', checked);
	});
});
