package controllers.viewmodels.request.validator

import play.api.libs.json.{Json, Reads}
import play.api.mvc.Results.BadRequest
import play.api.mvc.{BodyParser, PlayBodyParsers, Result}

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success, Try}

trait JsonValidator {

  def validateJson[A: Reads](
      fail: Throwable => Result
  )(implicit ec: ExecutionContext, parser: PlayBodyParsers): BodyParser[A] = {
    parser.json.validate { jsValue =>
      Try(jsValue.validate[A]) match {
        case Success(value)     => value.asEither.left.map(_ => BadRequest(Json.toJson("parse error")))
        case Failure(exception) => Left(fail(exception))
      }
    }
  }
}
