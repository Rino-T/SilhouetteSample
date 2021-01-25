package domain.application

sealed abstract class ServiceError(val errorCode: ErrorCode, val args: Any*) {
  protected val stackTrace: Array[StackTraceElement] = {
    val traces = Thread.currentThread().getStackTrace
    traces.drop(traces.lastIndexWhere(t => t.getClassName == getClass.getName) + 1)
  }

  override def toString: ErrorCode = {
    s"""${getClass.getName}($errorCode, [${args.mkString(", ")}])
       |${stackTrace.map(s => s"  at $s").mkString("\n")}
    """.stripMargin
  }
}

case class SystemError(cause: Throwable) extends ServiceError(ServiceErrorCodes.SystemError)

abstract class ApplicationError(errorCode: ErrorCode, args: Any*) extends ServiceError(errorCode, args: _*)

case class NotFoundError(entityType: String, id: Any)
    extends ApplicationError(ServiceErrorCodes.NotFound, entityType, id)

case class ConflictedError(value: Any) extends ApplicationError(ServiceErrorCodes.Conflicted, value)

object ServiceErrorCodes {
  val SystemError = "error.system"
  val NotFound    = "error.notFound"
  val Conflicted  = "error.conflicted"
}
