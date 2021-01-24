package controllers.viewmodels.request.auth.signUp

import controllers.viewmodels.request.validator.JsonValidator
import domain.models.auth.CredentialsSignUpData
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.{BodyParser, PlayBodyParsers}
import play.api.mvc.Results.BadRequest

import scala.concurrent.ExecutionContext
import scala.util.control.NonFatal

case class SignUpRequest(
    name: String,
    email: String,
    password: String
) {
  require(name.nonEmpty, "名前は必須")
  require(email.nonEmpty, "Emailは必須")
  require(password.length >= 8, "パスワードは8文字以上")

  def toCredentialsSignUpData: CredentialsSignUpData = CredentialsSignUpData(name, email, password)
}

object SignUpRequest {
  implicit def jsonFormat: OFormat[SignUpRequest] = Json.format[SignUpRequest]
}

trait SignUpRequestValidator extends JsonValidator {
  implicit def parse: PlayBodyParsers

  def signUpRequestValidate(implicit ec: ExecutionContext): BodyParser[SignUpRequest] = validateJson[SignUpRequest] {
    case e: IllegalArgumentException => BadRequest(e.getMessage)
    case NonFatal(e)                 => throw e
  }
}
