package controllers

import play.api._

object Riak {

  def rootUrl:String = {
    Play.current.configuration.getString("riak.root").getOrElse("http://localhost:8098/")
  }

  def riakBucketUrl (bucket:String) :String = {
    rootUrl+"buckets/"+bucket+"/keys"
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

  def messageUrl = {
    val bucket = "message"
    riakBucketUrl(bucket)
  }

}