$(function(){
	var memberElement = $('#member');
	var outsiderElement = $('#outsider');

	$('#move-to-member').click(function(){
		$('#outsider :selected').detach().appendTo('#member');
	});

	$('#remove-from-member').click(function(){
		$('#member :selected').detach().appendTo('#outsider');
	});

	$('#form-edit-group').submit(function(){
		$('#member option').each(function(){
			$(this).prop('selected', true);
		});
	});
});
