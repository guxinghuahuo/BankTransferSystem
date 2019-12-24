
$(document).ready(function () {


    /*==================================================================
   [ Focus input ]*/
    $('.input100').each(function(){
        $(this).on('blur', function(){
            if($(this).val().trim() !== "") {
                $(this).addClass('has-val');
            }
            else {
                $(this).removeClass('has-val');
            }
        })
    });


    /*==================================================================
    [ Validate ]*/
    var input = $('.validate-input .input100');

    $("#login_input").on('click',function(message){
        var check = true;
        for(var i=0; i<input.length; i++) {
            if(validate(input[i]) === false){
                showValidate(input[i]);
                check=false;
            }
        }

        if (check === true) {
            $('#loginMainForm').ajaxSubmit({
                type:"POST",
                url:"index",
                dataType:"json",
                data:{
                    'client_phone':$("#client_phone").attr("value"),
                    'client_loginpw':$("#client_loginpw").attr("value")
                },
                async:false,
                success:function (data) {
                    if (data.Status === '-1'){
                        alert(data.msg);
                        window.location.href="/index";
                    } else if (data.Status === '1') {
                        alert(data.msg);
                        window.location.href="/home";
                    }
                },
                error:function (jqXHR) {
                    alert("失败了");
                    console.log("接口异常" + jqXHR.status);
                    window.location.href="/index";
                }
            })
        }
        else {
            alert("输入的内容不合法！");
        }
        return false;
    });


    $('.validate-form .input100').each(function(){
        $(this).focus(function(){
            hideValidate(this);
        });
    });

    function validate (input) {
        if($(input).attr('type') === 'client_phone' || $(input).attr('name') === 'client_phone') {
            if($(input).val().trim().match(/^[1]([3-9])[0-9]{9}$/) == null) {
                return false;
            }
        }
        else {
            if($(input).val().trim() === ''){
                return false;
            }
        }
    }

    function showValidate(input) {
        var thisAlert = $(input).parent();

        $(thisAlert).addClass('alert-validate');
    }

    function hideValidate(input) {
        var thisAlert = $(input).parent();

        $(thisAlert).removeClass('alert-validate');
    }



});