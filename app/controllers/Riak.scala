package controllers

import play.api._

object Riak {

  def rootUrl:String = {
    Play.current.configuration.getString("riak.root").getOrElse("http://127.0.0.1:8087/")
  }

  def riakBucketKeyUrl (bucket:String,key:String) :String = {
    rootUrl+"buckets/"+bucket+"/keys/"+key
  }

  def riakMapReduceUrl:String = {
    rootUrl+"mapred"
  }

  //Bucket

  def userUrl(name:String) = {
    val bucket = "user"
    riakBucketKeyUrl(bucket,name)
  }

}