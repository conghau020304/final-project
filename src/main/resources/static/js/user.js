$(document).ready(function(){
    $('#tab5').addClass('active');
    $("#update").hide();
    assignDataToTable();
    assignDataRole();

    function assignDataToTable() {
        $.ajax({
          type:"GET",
          contentType: "application/json",
          url:"http://localhost:8080/admin/api/user",
          success: function(data) {
            console.log(data);
            var users = JSON.parse(JSON.stringify(data));
            dom(users);
            },
          error: function(err) {
            console.log(err);
            }
        });
    }
    function assignDataRole(){
        $.ajax({
          type:"GET",
          contentType: "application/json",
          url:"http://localhost:8080/admin/api/roles",
          success: function(data) {
            var roles = JSON.parse(JSON.stringify(data));
            domRole(roles);
            },
          error: function(err) {
            console.log(err);
            }
        });
    }

    function domRole(roles){
        $.each(roles, function (key, entry) {
            $('#roles').append($('<option></option>').attr('value', entry.id).text(entry.name));
         })
    }

    function dom(users){
        $("#users-table").empty();
        if(users.length > 0){

            for (var i in users) {
                var temp;
                var tempRole1 = "";
                var tempRole2 = "";
                if(users[i].enabled == 1){
                    temp = "<lable class='label label-success'> enable </label>";
                }else{
                    temp = "<lable class='label label-default'> disable </label>";
                }
                var roles = users[i].roles;
                if(roles.length > 0){
                    for(var j in roles){
                        if(roles[j].name === "ROLE_ADMIN"){
                            tempRole1 = "<span><lable class='label label-primary'>"+ roles[j].name +"</label></span>";
                        }
                        if(roles[j].name === "ROLE_MEMBER"){
                            tempRole2 = "<span><lable class='label label-info'>"+ roles[j].name +"</label> </span>";
                        }
                    }

                }
                var tempRole;
                if(tempRole1 != ""){
                   tempRole = tempRole1 + "&nbsp" + tempRole2;
                }else{
                   tempRole = tempRole2;
                }
                $("#users-table").
                append("<tr> \
                    <td> " +  users[i].id + "</td> \
                    <td class = 'text-left'>" +  users[i].username + "</td> \
                    <td class = 'text-left'>" +  users[i].password.substring(0, 20) + "</td> \
                    <td>" +  temp  + "</td> \
                    <td class='text-left'> " +  tempRole + "</td> \
                    <td><button id='delete' class='btn btn-link'> \ <i class='fas fa-trash-alt'></i> \ </button> \ </td> \
                    <td><button id='edit' class='btn btn-link'> \ <i class='far fa-edit'></i> \ </button> \ </td> \
                </tr>");
             }
        }else{
            $("#users-table").
                append("<tr> \
                   <td colspan ='6'><h4 style='color:red'> Table is empty!</h4></td> \
               </tr>");
        }
    }


    $("#save").click(function() {
        var detail = [];
        if($('#roles :selected').text().trim() === "ROLE_ADMIN"){
            $("#roles option").each(function()
            {
                var temp = {
                    id: $(this).val(),
                    name: $(this).text().trim()
                }
                detail.push(temp);
            });
        }else{
            var temp2 = {
                id: $('#roles :selected').val(),
                name: $('#roles :selected').text().trim()
             }
             detail.push(temp2);
        }
        var jsonVar = {
            username: $("#username").val(),
            password: $("#password").val(),
            enabled: $("#enabled").val(),
            roles: detail

        };
        $.ajax({
            type:"POST",
            url:"http://localhost:8080/admin/api/user",
            data: JSON.stringify(jsonVar),
            contentType: "application/json",
            success: function(data){
                assignDataToTable();
                clearData();
                $.notify("Insert success!", "success");
            },
            error: function(err) {
                console.log(err);
                var errMess = JSON.parse(JSON.stringify(err.responseJSON));
                validate(errMess);
            }
        });

    });

    function validate(err){
        if(err.username != null){
          $.notify(err.username, "error");
          $('#username').focus();
          $('#username').css('background', 'yellow');
        }else{
          $('#username').css('background', 'transparent');
        }
        if(err.password != null){
          $.notify(err.password, "error");
          $('#password').focus();
          $('#password').css('background', 'yellow');
        }else{
          $('#password').css('background', 'transparent');
        }
    }

    function showError404(err){
        var errMess = JSON.parse(JSON.stringify(err.responseJSON));
        if(errMess.message != null){
             $.notify(errMess.message, "error");
        }
    }

    $("#reset").click(function() {
        clearData();
    });

     function clearData(){
       $("#update").hide();
       $("#save").show();
       $("#id").val("");
       $("#username").val("");
       $("#password").val("");
       $('#username').css('background', 'transparent');
       $('#password').css('background', 'transparent');
     }

      $('table').on('click', 'button[id="delete"]', function(e){
         var id = $(this).closest('tr').children('td:first').text();
         var name = $(this).closest('tr').children('td:nth-child(2)').text();

         var r = confirm("Confirm delete user " + name + "!");
         if(r){
             $.ajax({
                  type:"DELETE",
                  url:"http://localhost:8080/admin/api/user/" + id,
                  success: function(data){
                      assignDataToTable();
                      $.notify("Delete success!", "success");
                  },
                  error: function(err) {
                      console.log(err);
                      showError404(err);
                  }
              });
          }else{
            return false;
          }
      });

      $('table').on('click','button[id="edit"]',function(e){
        var id = $(this).closest('tr').children('td:first').text();

        // get data
        $.ajax({
            type:"GET",
            url:"http://localhost:8080/admin/api/user/" + id,
            success: function(data){
               console.log(data);
                $('#id').prop('readonly', true);
                $("#update").show();
                $("#save").hide();
                var user = JSON.parse(JSON.stringify(data));
                $('#id').val(user.id);
                $('#username').val(user.username);
                $('#password').val(user.password.substring(0, 20));
                $('#enabled').val(user.enabled);
                if(user.roles.length == 2)
                    $('#roles').val('18').change();
                else
                    $('#roles').val(user.roles[0].id).change();
            },
            error: function(err){
               console.log(err);
               showError404(err);
            }
        });
      });

    $('#update').on('click', function(){
        var id = $("#id").val();
        var jsonVar = {
            id: $("#id").val(),
            username: $("#username").val(),
            password: $("#password").val(),
            enabled: $("#enabled").val()
        };
        $.ajax({
            type: "PUT",
            data: JSON.stringify(jsonVar),
            contentType: "application/json",
            url:"http://localhost:8080/admin/api/user/" + id,
            success: function(data){
                $.notify("Update success!", "success");
                assignDataToTable();
                clearData();
            },
            error: function(err){
                console.log(err);
                var errMess = JSON.parse(JSON.stringify(err.responseJSON));
                validate(errMess);
            }
        });
    });

   $("#btn-search").click(function() {
         var tk = $('#tk').val();
         $.ajax({
             type:"GET",
             url:"http://localhost:8080/admin/api/user?tk=" + tk,
             success: function(data){
                 var products = JSON.parse(JSON.stringify(data));
                 dom(products);
             },
             error: function(err) {
                console.log(err);
             }
         });

     });

});