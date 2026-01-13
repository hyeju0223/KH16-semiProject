
$(document).ready(function() {
	
	// + / - 버튼 핸들러
	$(".btn-qty").click(function() {
		var $input = $(this).closest(".quantity-selector").find(".input-qty");
		var currentVal = parseInt($input.val());
		var minVal = parseInt($input.attr("min"));
		var maxStock = parseInt($input.attr("max"));

		if ($(this).hasClass("btn-qty-up")) {
			if (currentVal < maxStock) {
				$input.val(currentVal + 1);
			} else {
				alert("재고 수량(" + maxStock + "개)을 초과할 수 없습니다.");
			}
		} 
		else if ($(this).hasClass("btn-qty-down")) {
			if (currentVal > minVal) {
				$input.val(currentVal - 1);
			} else {
				alert("수량은 " + minVal + "개 이상이어야 합니다.");
			}
		}
	});
	
	// 수동 입력 핸들러
	$(".input-qty").on("change", function(){
		var $input = $(this);
		var currentVal = parseInt($input.val());
		var maxStock = parseInt($input.attr("max"));
		var minVal = parseInt($input.attr("min"));
		
		if (currentVal < minVal) {
			alert("수량은 " + minVal + "개 이상이어야 합니다.");
			$input.val(minVal);
		} else if (currentVal > maxStock) {
			alert("재고 수량(" + maxStock + "개)을 초과할 수 없습니다.");
			$input.val(maxStock);
		}
	});

});