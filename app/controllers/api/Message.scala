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

  //GET

  def timeline = Action.async {
    val postHeader = """{
      "inputs": "message",
      "query": [
        {
          "map": {
            "language": "javascript",
            "source": "function(riak){return [JSON.parse(riak.values[0].data)];}"
          }
        },
        {
          "reduce": {
            "language": "javascript",
            "source": "function(riak) {var result = new Array();for(var i in riak){result.push(riak[i]);}result.sort(function(a,b){if(a.date < b.date ) return 1;if( a.date > b.date ) return -1;return 0;});return result;}"
          }
        }
      ]
    }"""
    WS.url(riakMapReduceUrl).post(Json.parse(postHeader)).map { response =>
      response.status match {
        case 200 => Ok{Json.obj("ok"->Json.parse(response.body))}
        case 404 => BadRequest(Json.obj("error"->"not exist"))
        case _ =>   BadRequest(Json.obj("error"->"unknown error"))
      }
    }
  }

  //POST

  def post = Action.async { request =>
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
          val timestamp: Long = System.currentTimeMillis
          val data = Json.obj("name" -> name,"pass" -> pass,"mes" -> mes,"date"->timestamp)
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