package domain.models.user

case class Role(
    id: RoleId,
    name: RoleName
)

case class RoleId(value: Int) {
  require(value >= 0)
}

case class RoleName(value: String) extends AnyVal
