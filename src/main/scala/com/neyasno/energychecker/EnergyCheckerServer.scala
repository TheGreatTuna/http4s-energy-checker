package com.neyasno.energychecker

import cats.effect.IO
import com.comcast.ip4s._
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.Logger

object EnergyCheckerServer {

  def run: IO[Nothing] = {
    val energyUpdateAlg = EnergyAvailable.impl

    // Combine Service Routes into an HttpApp.
    // Can also be done via a Router if you
    // want to extract a segments not checked
    // in the underlying routes.
    val httpApp = EnergyCheckerRoutes.energyAvailableRoutes(energyUpdateAlg).orNotFound

    // With Middlewares in place
    val finalHttpApp = Logger.httpApp(logHeaders = true, logBody = true)(httpApp)

    //    for {
    //      client <- EmberClientBuilder.default[IO].build
    EmberServerBuilder.default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8080")
      .withHttpApp(finalHttpApp)
      .build
  }.useForever
}
