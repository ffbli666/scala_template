/*
ref : http://mongodb.github.io/mongo-scala-driver/1.0/scaladoc/index.html#org.mongodb.scala.MongoCollection

 */

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
    gender: String
)

case class UserUpdate (
    firstname: String,
    lastname: String,
    gender: String
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
          "gender"    -> nonEmptyText
        )(User.apply)(User.unapply)
    }

    def create (data: JsValue) : Document = {
        val bind = userForm.bind(data)
        if (bind.hasErrors) {
            println(bind.errors)
            return null
        }
        val user = bind.get
        val doc: Document = Document (
            "lastname"  -> user.lastname,
            "firstname" -> user.firstname,
            "email"     -> user.email,
            "gender"    -> user.gender
        )
        collection.insertOne(doc).results()
        val result = collection.find().sort(descending("_id")).limit(1).results()
        return result.head
    }

    def get (id: String) : Document = {
        val result = collection.find(equal("_id", new ObjectId(id))).first().results()
        if(result.isEmpty) null
        else result.head
    }

    def search : Seq[Document] = {
        val count = 10
        collection.find().sort(descending("_id")).limit(count).results()
    }

    val userUpdateForm: Form[UserUpdate] = Form {
        mapping(
          "lastname"  -> nonEmptyText,
          "firstname" -> nonEmptyText,
          "gender"    -> nonEmptyText
        )(UserUpdate.apply)(UserUpdate.unapply)
    }

    def update(id: String, data: JsValue) : Document = {
        val bind = userUpdateForm.bind(data)
        if (bind.hasErrors) {
            println(bind.errors);
            return null
        }
        val user = bind.get
        val find = collection.findOneAndUpdate(equal("_id", new ObjectId(id))
                        , combine(
                            Updates.set("lastname"   , user.lastname),
                            Updates.set("firstname"  , user.firstname),
                            Updates.set("gender"     , user.gender)
                        )).results()
        if (find.isEmpty) null
        else this.get(id)
    }

    def delete(id: String) : Document = {
        val result = collection.findOneAndDelete(equal("_id", new ObjectId(id))).results()
        if(result.isEmpty) null
        else result.head
    }
}