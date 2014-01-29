package controllers

import play.api._
import play.api.mvc._

object Developer extends Controller {

  def getUser = Action {
    Ok(views.html.developer.api.getUser())
  }

  def setUser = Action {
    Ok(views.html.developer.api.setUser())
  }

  def setMessage = Action {
    Ok(views.html.developer.api.setMessage())
  }

  def getMessage = Action {
    Ok(views.html.developer.api.getMessage())
  }

}