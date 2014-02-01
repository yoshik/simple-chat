Zepto(function($){
  var index = Hogan.compile($('#index').text());
  var signup = Hogan.compile($('#signup').text());
  $('#view').html(index.render());

  $("#screen-index").show();

  $("#btn-index-signup").on("click",function(){
    var signup = Hogan.compile($('#signup').text());
    $('#view').html(signup.render());
  });

});