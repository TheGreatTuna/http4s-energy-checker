package com.neyasno.energychecker.domain

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import java.util.UUID

case class BlackoutInfo(id: UUID,
                        deviceId: UUID,
                        updatedAt: DateTime,
                        isManual: Boolean = false,
                        shutdown: Boolean = false,
                        description: String = ""
                       )
object BlackoutInfo {
  private lazy val dtf = DateTimeFormat.forPattern("yyyy-MM-dd'T'hh:mm:ss")

  implicit val encodeDateTime: Encoder[DateTime] = Encoder.instance(d => dtf.print(d).asJson)
  implicit val decodeDateTime: Decoder[DateTime] = Decoder[String].map(d => dtf.parseDateTime(d))
  implicit val blackoutInfoEncoder: Encoder[BlackoutInfo] = deriveEncoder[BlackoutInfo]
  implicit val blackoutInfoDecoder: Decoder[BlackoutInfo] = deriveDecoder[BlackoutInfo]

}
