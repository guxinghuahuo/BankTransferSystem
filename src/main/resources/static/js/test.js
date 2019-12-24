$(document).ready(function () {
    $("#test_input").on('click', function () {
        $('#testMainForm').ajaxSubmit({
            type:"POST",
            url:"test",
            dataType:"json",
            data:{
                'test_entity':$("#test_entity").attr("value"),
                'test_number':$("#test_number").attr("value")
            },
            async:false,
            success:function (data) {
                window.location.href="login"
            },
            error:function (jqXHR) {
                console.log("接口异常" + jqXHR.status);
            }
        });
        return false;
    });

});
