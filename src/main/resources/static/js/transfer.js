$(document).ready(function () {
    $("#pay").on('click', function () {
        $('#transMainForm').ajaxSubmit({
            type:"POST",
            url:"transfer",
            dataType:"json",
            data:{
                'scardnum':$("#scardnum").attr("value"),
                'rcardnum':$("#rcardnum").attr("value"),
                'money':$("#money").attr("value"),
                'bpw':$("#bpw").attr("value"),
                'note':$("#note").attr("value"),
            },
            success:function (data) {
                if (data.Status === '1'){
                    alert(data.msg);
                    window.location.href="/home";
                    return false;
                } else {
                    alert(data.msg);
                    window.location.href="/transfer";
                    return false;
                }
            },
            error:function (jqXHR) {
                console.log("接口异常" + jqXHR.status);
            }
        });
    });


    $("#back").on('click', function () {
        window.location.href = "/home";
        return false;
    });
});