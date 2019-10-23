$(document).ready(function(){

    $("#update").hide();
    assignDataToTable();

    function assignDataToTable() {
        $.ajax({
          type:"GET",
          contentType: "application/json",
          url:"http://localhost:8080/admin/api/user",
          success: function(data) {
            var users = JSON.parse(JSON.stringify(data));
            dom(users);
            },
          error: function(err) {
            console.log(err);
            }
        });
    }

    function dom(users){
        $("#users-table").empty();
        if(users.length > 0){
            for (var i in users) {
                $("#users-table").
                append("<tr> \
                            <td>" +  users[i].id + "</td> \
                            <td>" +  users[i].username + "</td> \
                            <td>" +  users[i].password + "</td> \
                            <td>" +  users[i].enabled  + "</td> \
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
/*      var detail = [];
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
*/
        var jsonVar = {
            username: $("#username").val(),
            password: $("#password").val(),
            enabled: $("#enabled").val()
           // roles: detail

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
                $.notify("ERROR", "error");
            }
        });

    });

    $("#reset").click(function() {
        clearData();
    });

     function clearData(){
       $("#update").hide();
       $("#save").show();
       $("#id").val("");
       $("#username").val("");
       $("#password").val("");
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
                  }
              });
          }else{
            return false;
          }
      });

      $('table').on('click','button[id="edit"]',function(e){
        var id = $(this).closest('tr').children('td:first').text();
        $('#id').prop('readonly', true);
        $("#update").show();
        $("#save").hide();
        // get data
        $.ajax({
            type:"GET",
            url:"http://localhost:8080/admin/api/user/" + id,
            success: function(data){
                var user = JSON.parse(JSON.stringify(data));
                $('#id').val(user.id);
                $('#username').val(user.username);
                $('#password').val(user.password);
                $('#enabled').val(user.enabled);
            },
            error: function(err){
               console.log(err);
               $.notify("ERROR", "error");
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
                 $.notify("ERROR", "error");
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