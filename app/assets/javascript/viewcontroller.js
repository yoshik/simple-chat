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
    $("#signup-submit").on("click",function(){view.index();});
    $("#signup-cancel").on("click",function(){view.index();});
  };

  view.signin=function(){
    if(!template.signin){
      template.signin = Hogan.compile($('#signin').text());
    }
    $('#view').html(template.signin.render());
    $("#signin-submit").on("click",function(){view.index();});
    $("#signin-cancel").on("click",function(){view.index();});
  };

  view.index();

});