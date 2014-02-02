var user={};

Zepto(function($){

  var template={};
  var view={};

  view.index=function(){
    if(!template.index){
      template.index = Hogan.compile($('#index').text());
    }
    $('#view').html(template.index.render());
    $("#index-signup").on("click",function(){view.signup();});
    $("#index-signin").on("click",function(){view.signin();});
  };

  view.signup=function(){
    if(!template.signup){
      template.signup = Hogan.compile($('#signup').text());
    }
    $('#view').html(template.signup.render());
    $("#signup-submit").on("click",function(){
      data={};
      data.username=$('#signup-username').val();
      data.password=$('#signup-password').val();
      $.ajax({
        url: "/registration",
        type: "POST",
        cache: false,
        contentType: "application/json; charset=UTF-8",
        data: JSON.stringify(data),
        success: function(message) {
          $("#signup-result").text(message.ok);
          user.name=data.username;
          user.pass=data.password;
        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
          $("#signup-result").text(JSON.parse(XMLHttpRequest.responseText).error);
        }
      });
    });
    $("#signup-cancel").on("click",function(){view.index();});
  };

  view.signin=function(){
    if(!template.signin){
      template.signin = Hogan.compile($('#signin').text());
    }
    $('#view').html(template.signin.render());
    $("#signin-submit").on("click",function(){
      data={};
      data.username=$('#signin-username').val();
      data.password=$('#signin-password').val();
      $.ajax({
        url: "/registration",
        type: "POST",
        cache: false,
        contentType: "application/json; charset=UTF-8",
        data: JSON.stringify(data),
        success: function(message) {
          $("#signin-result").text(message.ok);
          user.name=data.username;
          user.pass=data.password;
        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
          $("#signin-result").text(JSON.parse(XMLHttpRequest.responseText).error);
        }
      });
    });
    $("#signin-cancel").on("click",function(){view.index();});
  };

  view.index();

});