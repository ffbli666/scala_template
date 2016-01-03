package models

import play.api.libs.json._

case class User(
    firstname: String,
    lastname: String,
    email: String,
    gender: String,
    password: String)

object User {

  implicit val userFormat = Json.format[User]
}