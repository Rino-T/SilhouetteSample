package domain.usecases.core

import scala.concurrent.Future

abstract class AsyncUseCase[TInput <: Input[TOutput], TOutput <: Output] {
  def handle(input: TInput): Future[TOutput]
}
