package domain.models.auth

import java.util.UUID
import scala.concurrent.Future

trait AuthTokenRepository {
  def find(id: UUID): Future[Option[AuthToken]]
}
