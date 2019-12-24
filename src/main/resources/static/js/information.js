$(document).ready(function () {
    $("#go").on('click', function () {
        $('#pageMainForm').ajaxSubmit({
            type:"POST",
            url:"information",
            dataType:"json",
            data:{
                'pagenum':$("#pagenum").attr("value")
            },
            success:function (data) {
                console.log("跳转成功");
            },
            error:function (jqXHR) {
                console.log("接口异常" + jqXHR.status);
            }
        })
    });

    $("#back").on('click', function () {
        window.location.href="/home";
        return false;
    });
});