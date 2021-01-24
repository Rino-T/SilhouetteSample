package domain.application.mail

trait MailService {
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

  def sendHtmlEmail(from: String, to: String, subject: String, htmlConten: String, loggerNote: String): Unit
}
