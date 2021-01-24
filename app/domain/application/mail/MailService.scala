package domain.application.mail

import com.amazonaws.regions.Regions
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder
import com.amazonaws.services.simpleemail.model.{Body, Content, Destination, Message, SendEmailRequest}
import play.api.Logging

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Failure

/** AWS SESを利用
  * @see https://docs.aws.amazon.com/ja_jp/ses/latest/DeveloperGuide/examples-send-using-sdk.html
  */
class MailService(implicit ec: ExecutionContext) extends Logging {
  private val from = "noreply@mysitenamehere.com" // FIXME

  def sendActivateAccountEmail(email: String, url: String): Unit = {
    sendHtmlEmail(
      from,
      email,
      "Account confirmation",
      "<html><body><p>Please <a href='" + url + "' rel='nofollow'>click here</a> to confirm your account.</p><p>If you didn't create an account using this e-mail address, please ignore this message.</p></body></html>",
      "Account confirmation"
    )
  }

  private def sendHtmlEmail(
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
