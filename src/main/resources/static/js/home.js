$(document).ready(function () {
    $("#query").on('click',function(message){
        window.location.href='/information?pagenum=1';
        return false;
    });

    $("#transfer").on('click',function(message){
        window.location.href='/transfer';
        return false;
    });
});