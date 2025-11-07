$(function () {
    $(".summernote-editor").summernote({
        //높이 옵션
        height: 250,//시작 높이(px)
        minHeight: 200,//최소 높이(px)
        maxHeight: 400,//최대 높이(px)

        placeholder: "타인에 대한 무분별한 비방 시 예고 없이 삭제될 수 있습니다",

        //메뉴 설정(toolbar)
        toolbar: [
            ["font", ["style", "fontname", "fontsize", "forecolor", "backcolor"]],
            ["style", ["bold", "italic", "underline", "strikethrough"]],
            ["attach", ["picture"]],
            ["tool", ["ol", "ul", "table", "hr", "fullscreen"]]
        ],
		
		//커스텀 훅(hook)
		callbacks : {
			onImageUpload : function(files) {
				console.log("파일 업로드 시도중...");
				console.log(files);
				
				var form = new FormData();//폼 태그 대신 사용할 도구
				for(var i=0; i < files.length; i++) {
					form.append("attach", files[i]);
				}
				
				$.ajax({
					processData:false,
					contentType:false,
					url:"/rest/post/temps",
					method:"post",
					data:form,
					success:function(response){
						for(var i=0; i < response.length; i++) {
							var img = $("<img>").attr("src", contextPath+"/attachment/download?attachmentNo="+response[i])
												.attr("data-pk", response[i])
												.addClass("custom-image");
							$(".summernote-editor").summernote("insertNode", img[0]);
						}
					}
				});
			}
		}
    });
});