package com.softwaremill.k8s.example

import akka.NotUsed
import akka.kafka.testkit.scaladsl.KafkaSpec
import akka.kafka.testkit.internal.TestFrameworkInterface
import akka.stream.scaladsl.Flow
import org.scalatest.{FlatSpecLike, Matchers, Suite}
import org.scalatest.concurrent.{Eventually, IntegrationPatience, ScalaFutures}

abstract class SpecBase(kafkaPort: Int)
    extends KafkaSpec(kafkaPort)
    with FlatSpecLike
    with TestFrameworkInterface.Scalatest
    with Matchers
    with ScalaFutures
    with IntegrationPatience
    with Eventually {

  this: Suite =>

  protected def this() = this(kafkaPort = -1)

  def businessFlow[T]: Flow[T, T, NotUsed] = Flow[T]
}
