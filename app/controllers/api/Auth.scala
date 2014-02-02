package controllers

import controllers.Riak._

import play.api._
import play.api.mvc._
import play.api.libs.ws._
import play.api.libs.json._

import scala.util._
import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global

object Auth extends Controller {

  def auth(name:String,pass:String):Boolean = {
    val get = WS.url(userUrl(name)).get().map { response =>
      val json = Json.parse(response.body)
      val jsonPass = (json \ "pass").asOpt[String]
      jsonPass.getOrElse("") == pass
    }
    Await.result(get,5000 millis)
    get.value match{
      case Some(s:Try[Boolean]) => s.get
      case _ => false
    }
  }
}