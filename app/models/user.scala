package models

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

case class User (
    firstname: String,
    lastname: String,
    email: String,
    gender: String,
    password: String
)

case class UserUpdate (
    firstname: String,
    lastname: String,
    gender: String,
    password: String
)

object UserModel {
    val mongoClient: MongoClient = MongoClient("mongodb://localhost")
    val database: MongoDatabase = mongoClient.getDatabase("htc")
    val collection: MongoCollection[Document] = database.getCollection("user")

    val userForm: Form[User] = Form {
        mapping(
          "lastname"  -> nonEmptyText,
          "firstname" -> nonEmptyText,
          "email"     -> email,
          "gender"    -> nonEmptyText,
          "password"  -> nonEmptyText
        )(User.apply)(User.unapply)
    }

    def create (data: JsValue) {
        val user = userForm.bind(data).get
        val doc: Document = Document (
            "lastname"  -> user.lastname,
            "firstname" -> user.firstname,
            "email"     -> user.email,
            "gender"    -> user.gender,
            "password"  -> user.password
        )
        collection.insertOne(doc).results()
    }

    def get(id: String) : Document = {
        val result = collection.find(equal("_id", new ObjectId(id))).first().results()
        return result.head
    }

    val userUpdateForm: Form[UserUpdate] = Form {
        mapping(
          "lastname"  -> nonEmptyText,
          "firstname" -> nonEmptyText,
          "gender"    -> nonEmptyText,
          "password"  -> nonEmptyText
        )(UserUpdate.apply)(UserUpdate.unapply)
    }

    def update(id: String, data: JsValue) : Document = {
        val user = userUpdateForm.bind(data).get
        collection.findOneAndUpdate(equal("_id", new ObjectId(id))
                        , combine(
                            Updates.set("lastname"   , user.lastname),
                            Updates.set("firstname"  , user.firstname),
                            Updates.set("gender"     , user.gender),
                            Updates.set("password"   , user.password)
                        )).results()
        return this.get(id);
    }

    def delete(id: String) : Document = {
        val result = collection.findOneAndDelete(equal("_id", new ObjectId(id))).results()
        return result.head;
    }
}