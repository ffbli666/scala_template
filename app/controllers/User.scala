/*
ref : http://mongodb.github.io/mongo-scala-driver/1.0/scaladoc/index.html#org.mongodb.scala.MongoCollection
 */
package controllers

import models._
import play.api._
import play.api.mvc._
import play.api.libs.json._

class ApiUser extends Controller {

    def create = Action(parse.json) { implicit request =>
        UserModel.create(request.body);
        Ok(Json.parse("""
        {
          "status" : "ok",
          "msg" : "success"
        }
        """))
    }

    def search = Action { implicit request =>
          Ok("Got request [" + request + "]")
    }

    def get(id: String) = Action { implicit request =>
        val result = UserModel.get(id);
        Ok(Json.parse("""
        {
          "status" : "ok",
          "msg" : "success",
          "result" : """ + result.toJson() + """
        }
        """))
    }

    def update(id: String) = Action(parse.json) { implicit request =>
        var result = UserModel.update(id, request.body)
        Ok(Json.parse("""
        {
          "status" : "ok",
          "msg" : "success",
          "result" : """ + result.toJson() + """
        }
        """))
    }

    def delete(id: String) = Action { implicit request =>
        UserModel.delete(id);
        Ok(Json.parse("""
        {
          "status" : "ok",
          "msg" : "success",
          "result" : """" + id + """"
        }
        """))
    }
}
