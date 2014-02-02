package controllers

import play.api._
import play.api.mvc._

object Developer extends Controller {

  def index = Action {
    Ok(views.html.app.developer())
  }

  def user = Action {
    Ok(views.html.developer.api.user())
  }

  def registration = Action {
    Ok(views.html.developer.api.registration())
  }

  def post = Action {
    Ok(views.html.developer.api.post())
  }

  def timeline = Action {
    Ok(views.html.developer.api.timeline())
  }

  def signin = Action {
    Ok(views.html.developer.api.signin())
  }
}