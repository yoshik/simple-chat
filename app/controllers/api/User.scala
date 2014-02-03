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

object User extends Controller {

  //GET

  def user(name:String) = Action.async {
    WS.url(userUrl(name)).get().map { response =>
      response.status match {
        case 200 => {
          val json = Json.parse(response.body)
          val res = Json.obj("name"-> json \ "name")
          Ok{Json.obj("ok"->res)}
        }
        case 404 => BadRequest(Json.obj("error"->"not exist"))
        case _ =>   BadRequest(Json.obj("error"->"unknown error"))
      }
    }
  }

  //POST

  def registration = Action.async { request =>
    (
      for {
        json <- request.body.asJson
        name <- (json \ "username").asOpt[String]
        pass <- (json \ "password").asOpt[String]
      } yield {
        WS.url(userUrl(name)).get().map { response =>
          if(name == None){
            BadRequest(Json.obj("error"->"no username"))
          }else if(pass == None){
            BadRequest(Json.obj("error"->"no password"))
          }else if(response.status != 404){
            BadRequest(Json.obj("error"->"already exist"))
          }else{
            val data = Json.obj("name" -> name,"pass" -> pass)
            val post = WS.url(userUrl(name)).post(data).map { response =>
              response.status match {
                case 201 => true
                case 204 => true
                case _ => false
              }
            }
            Await.result(post,5000 millis)
            post.value match{
              case Some(s:Try[Boolean]) => {
                if(s.getOrElse(false)){
                  Ok(Json.obj("ok"->"success"))
                }else{
                  BadRequest(Json.obj("error"->"busy"))
                }
              }
              case _ => BadRequest(Json.obj("error"->"busy"))
            }
          }
        }
      }
    ).getOrElse {
      future{BadRequest(Json.obj("error"->"wrong json"))}
    }
  }

}