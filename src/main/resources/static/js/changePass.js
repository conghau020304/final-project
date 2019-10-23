$(document).ready(function(){

    $("#change").click(function() {
       if($('#rePassword').val() != $('#newPassword').val()){
            $.notify("Re password not equals new password!", "error");
            $('#rePassword').css('background', 'yellow');
       }else{
            $('#rePassword').css('background', 'transparent');
            var jsonVar = {
                newPassword : $('#rePassword').val(),
                oldPassword: $('#oldPassword').val()
            }
            $.ajax({
                type:"POST",
                url:"http://localhost:8080/user/action-change-password/",
                data: JSON.stringify(jsonVar),
                contentType: "application/json",
                success: function(data){
                   if(data === "ok"){
                    $('#oldPassword').css('background', 'transparent');
                    $('#oldPassword').val('');
                    $('#newPassword').val('');
                    $('#rePassword').val('');
                    setTimeout(function(){
                        var temp = confirm("Change success! Do you want logout now!");
                        if(temp){
                            window.location = "http://localhost:8080/logout";
                        }else{
                            return false;
                        }
                    }, 1400);

                   }else if(data === "error"){
                       $.notify("Old password incorrect!", "error");
                       $('#oldPassword').css('background', 'yellow');
                   }
                },
                error: function(err) {
                    console.log(err);
                }
            });
       }
    });
});