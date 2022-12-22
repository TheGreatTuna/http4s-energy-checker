package com.neyasno.energychecker

import cats.effect.{IO, IOApp}

object Main extends IOApp.Simple {
  def run:IO[Unit] = EnergyCheckerServer.run
}
