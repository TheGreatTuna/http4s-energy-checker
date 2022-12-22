package com.neyasno.energychecker

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.dsl.io._

object EnergyCheckerRoutes {

  def energyAvailableRoutes(E: EnergyAvailable): HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
      case POST -> Root / "energy" / "update" / UUIDVar(deviceId) =>
        for {
          update <- E.update(EnergyAvailable.EnergyAvailableRequest(deviceId))
          resp <- Ok(update)
        } yield resp

      case POST -> Root / "energy" / "update" / UUIDVar(deviceId) / "manual" / UUIDVar(id) =>
        for {
          update <- E.updateManual(deviceId, id)
          resp <- Ok(update)
        } yield resp
    }
  }
}
