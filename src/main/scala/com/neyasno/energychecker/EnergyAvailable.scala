package com.neyasno.energychecker

import cats.effect.IO
import cats.implicits._
import com.github.nscala_time.time.Imports._
import com.neyasno.energychecker.EnergyAvailable.EnergyAvailableRequest
import com.neyasno.energychecker.domain.BlackoutInfo
import io.circe.{Decoder, Encoder}
import org.http4s._
import org.http4s.circe._
import io.circe.generic.semiauto._

import java.util.UUID


trait EnergyAvailable {
  def update(request: EnergyAvailableRequest): IO[EnergyAvailable.EnergyAvailableResponse]
  def updateManual(deviceId: UUID, id: UUID): IO[EnergyAvailable.EnergyAvailableResponse]
}

object EnergyAvailable {
  final case class EnergyAvailableRequest(deviceId: UUID) extends AnyVal
  object EnergyAvailableRequest {
    implicit val eaRequestDecoder: Decoder[EnergyAvailableRequest] = deriveDecoder[EnergyAvailableRequest]
    implicit val eaRequestEntityDecoder: EntityDecoder[IO, EnergyAvailableRequest] = jsonOf
  }


  final case class EnergyAvailableResponse(blackoutInfo: BlackoutInfo) extends AnyVal

  object EnergyAvailableResponse {
    implicit val eaResponseEncoder: Encoder[EnergyAvailableResponse] = deriveEncoder[EnergyAvailableResponse]
    implicit val eaResponseDecoder: Decoder[EnergyAvailableResponse] = deriveDecoder[EnergyAvailableResponse]
    implicit val eaResponseEntityEncoder: EntityEncoder[IO, EnergyAvailableResponse] = jsonEncoderOf
    implicit val eaResponseEntityDecoder: EntityDecoder[IO, EnergyAvailableResponse] = jsonOf
  }

  final case class BlackoutError(e: Throwable) extends RuntimeException

  def impl: EnergyAvailable =  new EnergyAvailable {
    override def update(request: EnergyAvailableRequest): IO[EnergyAvailableResponse] =
      EnergyAvailableResponse(BlackoutInfo(
        id = UUID.randomUUID(),
        deviceId = request.deviceId,
        updatedAt = DateTime.now(),
        isManual = false,
        shutdown = false)).pure[IO]

    override def updateManual(deviceId: UUID, id: UUID): IO[EnergyAvailableResponse] =
      EnergyAvailableResponse(BlackoutInfo(
        id = id,
        deviceId = deviceId,
        updatedAt = DateTime.now(),
        isManual = true,
        shutdown = false)).pure[IO]

  }
}
