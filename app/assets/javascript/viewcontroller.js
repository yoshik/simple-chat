var user={};

function relationTime (time) {
  var ints = {second: 1,minute: 60,hour: 3600,day: 86400,week: 604800,month: 2592000,year: 31536000};
  time = +new Date(time*1);
  var gap = ((+new Date()) - time) / 1000,amount, measure;
  if(gap<2){return 'just now'}
  for (var i in ints) {if (gap > ints[i]) { measure = i; }}
  amount = gap / ints[measure];
  amount = gap > ints.day ? (Math.round(amount * 100) / 100) : Math.round(amount);
  return Math.floor(amount) + ' ' + measure + (amount > 1 ? 's' : '') + ' ago';
}

function setTime(){
  $('.time').each(function(){
    $(this).text(relationTime($(this).attr('data-unixtime')));
  });
}

Zepto(function($){

  var template={};
  var view={};
  var timer;

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
          user.name=data.username;
          user.pass=data.password;
          view.timeline();
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
        url: "/signin",
        type: "POST",
        cache: false,
        contentType: "application/json; charset=UTF-8",
        data: JSON.stringify(data),
        success: function(message) {
          $("#signin-result").text(message.ok);
          user.name=data.username;
          user.pass=data.password;
          view.timeline();
        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
          $("#signin-result").text(JSON.parse(XMLHttpRequest.responseText).error);
        }
      });
    });
    $("#signin-cancel").on("click",function(){view.index();});
  };

  view.timeline=function(){
    if(!template.timeline){
      template.timeline = Hogan.compile($('#timeline').text());
    }
    if(!template.cell){
      template.cell = Hogan.compile($('#cell').text());
    }
    if(!timer){
      timer = setInterval("setTime()",1000);
    }

    $('#view').html(template.timeline.render());
    data={};
    $.ajax({
      url: "/timeline",
      type: "GET",
      cache: false,
      contentType: "application/json; charset=UTF-8",
      data: JSON.stringify(data),
      success: function(message) {
        $('#timeline-list').html(template.cell.render(message));
      },
      error: function(XMLHttpRequest, textStatus, errorThrown){
        $("#signin-result").text(JSON.parse(XMLHttpRequest.responseText).error);
      }
    });

    $("#navbar-post").on("click",function(){
      data={};
      data.message=$('#navbar-input').val();
      data.username=user.name;
      data.password=user.pass;

      $.ajax({
        url: "/post",
        type: "POST",
        cache: false,
        contentType: "application/json; charset=UTF-8",
        data: JSON.stringify(data),
        success: function(message) {
          $("#signin-result").text(message.ok);
          view.timeline();
        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
          $("#signin-result").text(JSON.parse(XMLHttpRequest.responseText).error);
        }
      });
    });

  };

  view.index();

});