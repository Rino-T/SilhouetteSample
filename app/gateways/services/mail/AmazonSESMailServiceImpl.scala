package gateways.services.mail

import com.amazonaws.regions.Regions
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder
import com.amazonaws.services.simpleemail.model._
import domain.application.mail.MailService
import play.api.Logging

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Failure

/** AWS SESを利用
  * @see https://docs.aws.amazon.com/ja_jp/ses/latest/DeveloperGuide/examples-send-using-sdk.html
  */
class AmazonSESMailServiceImpl @Inject() (implicit ec: ExecutionContext) extends MailService with Logging {
  def sendHtmlEmail(
      from: String,
      to: String,
      subject: String,
      htmlContent: String,
      loggerNote: String
  ): Unit = {
    Future {
      val client = AmazonSimpleEmailServiceClientBuilder
        .standard()
        .withRegion(Regions.AP_NORTHEAST_1)
        .build()

      val sbj     = new Content().withCharset("UTF-8").withData(subject)
      val body    = new Body().withHtml(new Content().withCharset("UTF-8").withData(htmlContent))
      val message = new Message().withBody(body).withSubject(sbj)

      val request = new SendEmailRequest()
        .withDestination(new Destination().withToAddresses(to))
        .withMessage(message)
        .withSource(from)

      client.sendEmail(request)
    }.onComplete {
      case Failure(e) => logger.error(s"Error on sending $loggerNote email to $to", e)
      case _          => logger.info(s"$loggerNote email has been sent to $to")
    }
  }
}
