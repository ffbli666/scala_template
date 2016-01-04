package controllers

import models._
import play.api._
import play.api.mvc._
import play.api.libs.json._

class User extends Controller {

    def create = Action(parse.json) { implicit request =>
        val result = UserModel.create(request.body)
        if (!result) {
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
              "msg" : "ok"
            }
            """))
        }
    }

    def search = Action { implicit request =>
        val result = UserModel.search
        val list = result.map(res =>  Json.parse(res.toJson))
        Ok(Json.parse("""
            {
              "status" : 200,
              "msg" : "ok",
              "result" : """ + Json.toJson(list) + """
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
              "result" : """ + result.toJson() + """
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
              "result" : """ + result.toJson() + """
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
