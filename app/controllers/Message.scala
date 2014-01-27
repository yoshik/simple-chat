package controllers

import controllers.Riak._
import controllers.Auth._

import play.api._
import play.api.mvc._
import play.api.libs.ws._
import play.api.libs.json._

import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global

object Message extends Controller {

  //POST

  def setMessage = Action.async { request =>
    (
      for {
        json <- request.body.asJson
        name <- (json \ "username").asOpt[String]
        pass <- (json \ "password").asOpt[String]
        mes  <- (json \ "message" ).asOpt[String]
      } yield {
        if(name == None || pass == None || mes == None){
          future{BadRequest(Json.obj("error"->"not enough json"))}
        }
        else if(Auth.auth(name,pass)){
          val data = Json.obj("name" -> name,"pass" -> pass,"mes" -> mes)
          WS.url(messageUrl).post(data).map { response =>
            response.status match {
              case 201 => Ok{Json.obj("ok"->"success")}
              case _ => BadRequest(Json.obj("error"->"busy"))
            }
          }
        }else{
          future{BadRequest(Json.obj("error"->"can't auth"))}
        }
      }
    ).getOrElse {
      future{BadRequest(Json.obj("error"->"wrong json"))}
    }
  }

}