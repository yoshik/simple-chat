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

}