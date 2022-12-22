package com.neyasno.energychecker

import cats.effect.IO
import com.neyasno.energychecker.EnergyAvailable.EnergyAvailableResponse
import munit.CatsEffectSuite
import org.http4s._

import java.util.UUID

class EnergyAvailableTest extends CatsEffectSuite with ctx {

  test("EnergyChecker returns status code 200") {
    assertIO(retEnergyAvailable.map(_.status), Status.Ok)
  }

  test("EnergyChecker returns blackout info") {
    val response = retEnergyAvailable.flatMap(_.as[EnergyAvailableResponse])
    assertIO(response.map(_.blackoutInfo.deviceId), deviceId)
    assertIO(response.map(_.blackoutInfo.shutdown), false)
    assertIO(response.map(_.blackoutInfo.isManual), false)
  }

  test("EnergyChecker manual returns blackout info") {
    val response = retEnergyAvailableManual.flatMap(_.as[EnergyAvailableResponse])
    assertIO(response.map(_.blackoutInfo.deviceId), deviceId)
    assertIO(response.map(_.blackoutInfo.shutdown), false)
    assertIO(response.map(_.blackoutInfo.isManual), true)
  }
}

trait ctx {
  val deviceId = UUID.randomUUID()
  val id = UUID.randomUUID()
  val energyAvailable = EnergyAvailable.impl

  val retEnergyAvailable: IO[Response[IO]] = {
    val getEA = Request[IO](Method.POST, Uri.unsafeFromString(s"/energy/update/${deviceId.toString}"))
    EnergyCheckerRoutes.energyAvailableRoutes(energyAvailable).orNotFound(getEA)
  }

  val retEnergyAvailableManual: IO[Response[IO]] = {
    val getEA = Request[IO](Method.POST, Uri.unsafeFromString(s"/energy/update/${deviceId.toString}/manual/$id"))
    EnergyCheckerRoutes.energyAvailableRoutes(energyAvailable).orNotFound(getEA)
  }
}
