package controllers

import models.User
import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.libs.json._

import org.mongodb.scala._
import org.mongodb.scala.model.Aggregates._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Projections._
import org.mongodb.scala.model.Sorts._
import org.mongodb.scala.model.Updates._
import org.mongodb.scala.model._
import org.bson.types.ObjectId

import mongo.Helpers._

class ApiUser extends Controller {
    val mongoClient: MongoClient = MongoClient("mongodb://localhost")
    val database: MongoDatabase = mongoClient.getDatabase("htc")
    val collection: MongoCollection[Document] = database.getCollection("user")

    val userForm: Form[User] = Form {
        mapping(
          "lastname"  -> nonEmptyText,
          "firstname" -> nonEmptyText,
          "email"     -> nonEmptyText,
          "gender"    -> nonEmptyText,
          "password"  -> nonEmptyText
        )(User.apply)(User.unapply)
    }

    def create = Action(parse.json) { implicit request =>
        val user = userForm.bind(request.body).get
        val doc: Document = Document(
            "lastname"  -> user.lastname,
            "firstname" -> user.firstname,
            "email"     -> user.email,
            "gender"    -> user.gender,
            "password"  -> user.password
        )
        collection.insertOne(doc).results()
        Ok("Got request [" + user + "]")
    }

    def search = Action {implicit request =>
          Ok("Got request [" + request + "]")
    }

    def get(id: String) = Action {implicit request =>
        val ret = collection.find(equal("_id", new ObjectId(id))).first().results()
        Ok(Json.parse("""
        {
          "status" : "ok",
          "msg" : "success",
          "result" : """ + ret.head.toJson() + """
        }
        """))
    }

    def update(id: String) = Action {implicit request =>
          Ok("Got request [" + request.body + "]")
    }

    def delete(id: String) = Action {implicit request =>
          Ok("Got request [" + request + "]")
    }
}
