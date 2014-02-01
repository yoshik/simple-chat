package controllers

import controllers.Riak._

import play.api._
import play.api.mvc._
import play.api.libs.ws._
import play.api.libs.json._

import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global

object User extends Controller {

  //GET

  def getUser(name:String) = Action.async {
    WS.url(userUrl(name)).get().map { response =>
      response.status match {
        case 200 => Ok{Json.obj("ok"->Json.parse(response.body))}
        case 404 => BadRequest(Json.obj("error"->"not exist"))
        case _ =>   BadRequest(Json.obj("error"->"unknown"))
      }
    }
  }

  //POST

  def setUser = Action.async { request =>
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
            val post = WS.url(userUrl(name)).post(data)
            try {
              Await.result(post,5000 millis)
            } catch {
              case e: TimeoutException => "timeout " + e
              case _ => "unknown"
            } finally {
              //nothing
            }
            val result = {
              var i = BadRequest(Json.obj("error"->"busy"))
              post onSuccess {
                case _ => i = Ok(Json.obj("ok"->"success"))
              }
              post onFailure {
                case _ => i = BadRequest(Json.obj("error"->"busy"))
              }
              i
            }
            result
          }
        }
      }
    ).getOrElse {
      future{BadRequest(Json.obj("error"->"wrong json"))}
    }
  }

}