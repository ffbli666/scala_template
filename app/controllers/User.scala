package controllers

import models._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import scala.util.parsing.json._

class User extends Controller {

    def create = Action(parse.json) { implicit request =>
        val result = UserModel.create(request.body)
        if (result == null) {
            Status(400)(Json.parse("""
            {
              "status" : 400,
              "msg" : "bad request"
            }
            """))
        }
        else {
            Ok(Json.parse("""
            {
              "status" : 200,
              "msg" : "ok",
              "result" : """ + JSONObject(result) + """
            }
            """))
        }
    }

    def search = Action { implicit request =>
        val result = UserModel.search
        Ok(Json.parse("""
            {
              "status" : 200,
              "msg" : "ok",
              "result" : """ + Json.toJson(result) + """
            }
            """))
    }

    def get(id: String) = Action { implicit request =>
        val result = UserModel.get(id);
        if (result == null) {
            Status(404)(Json.parse("""
            {
              "status" : 404,
              "msg" : "not found"
            }
            """))
        }
        else {
            Ok(Json.parse("""
            {
              "status" : 200,
              "msg" : "ok",
              "result" : """ + JSONObject(result) + """
            }
            """))
        }
    }

    def update(id: String) = Action(parse.json) { implicit request =>
        var result = UserModel.update(id, request.body)
        if (result == null) {
            Status(404)(Json.parse("""
            {
              "status" : 404,
              "msg" : "not found"
            }
            """))
        }
        else {
            Ok(Json.parse("""
            {
              "status" : 200,
              "msg" : "ok",
              "result" : """ + JSONObject(result) + """
            }
            """))
        }
    }

    def delete(id: String) = Action { implicit request =>
        val result = UserModel.delete(id);
        if (result == null) {
            Status(404)(Json.parse("""
            {
              "status" : 404,
              "msg" : "not found"
            }
            """))
        }
        else {
            println(result);
            Ok(Json.parse("""
            {
              "status" : 200,
              "msg" : "ok",
              "result" : """" + id + """"
            }
            """))
        }
    }
}
