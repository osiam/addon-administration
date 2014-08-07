$(function(){
	
	$("#button-add-email").click(function(){
		var allEmailBlocks = $("[id^='email-block-']");
		var displayedEmailCount = allEmailBlocks.size();
		var lastEmailBlock = allEmailBlocks.last();
		
		var myIndex = displayedEmailCount;
		var newBlock = cloneBlock('#email-template', 'email-block-' + myIndex, myIndex);
		
		lastEmailBlock.after(newBlock);
		newBlock.show();
	});
	
	$("#button-add-phoneNumber").click(function(){
		var allPhoneNumberBlocks = $("[id^='phoneNumber-block-']");
		var displayedPhoneNumberCount = allPhoneNumberBlocks.size();
		var lastPhoneNumberBlock = allPhoneNumberBlocks.last();
		
		var myIndex = displayedPhoneNumberCount;
		var newBlock = cloneBlock('#phoneNumber-template', 'phoneNumber-block-' + myIndex, myIndex);
		
		lastPhoneNumberBlock.after(newBlock);
		newBlock.show();
	});
	
	function cloneBlock(searchString, newId, newIndex){
		var templateBlock = $(searchString);
		var newBlock = templateBlock.clone();

		newBlock.attr('id', newId);
		newBlock.find('*').each(function(e, element) {
			$.each(this.attributes, function(a, attr){
				var name = attr.name;
				var value = attr.value;
				
				if(value.indexOf("__ID__") > -1){
					var newValue = value.replace(/__ID__/g, newIndex);
					$(element).attr(name, newValue);
				}
				
			});
		});
		
		return newBlock;
	};
	
});