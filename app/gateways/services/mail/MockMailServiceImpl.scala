package gateways.services.mail

import domain.application.mail.MailService

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class MockMailServiceImpl @Inject() (implicit ec: ExecutionContext) extends MailService {

  override def sendHtmlEmail(
      from: String,
      to: String,
      subject: String,
      htmlContent: String,
      loggerNote: String
  ): Unit = {
    println(s"""
        |from: $from
        |to: $to
        |subject: $subject
        |----------------------------------
        |
        |$htmlContent
        |
        |----------------------------------
        |loggerNote: $loggerNote
        |""".stripMargin)
  }
}
