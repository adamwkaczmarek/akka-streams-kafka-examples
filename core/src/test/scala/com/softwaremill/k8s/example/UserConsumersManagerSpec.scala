package com.softwaremill.k8s.example

import akka.Done
import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer.DrainingControl
import akka.kafka.{CommitterSettings, ConsumerSettings, ProducerSettings}
import akka.kafka.scaladsl.Producer
import akka.kafka.testkit.scaladsl.TestcontainersKafkaLike
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import akka.stream.testkit.javadsl.TestSink
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
import org.scalatest.BeforeAndAfterAll
import akka.stream.testkit.scaladsl.StreamTestKit.assertAllStagesStopped

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class UserConsumersManagerSpec extends SpecBase with TestcontainersKafkaLike {

  "UserConsumer atLeastOnce delivery" should "read message from user topic" in assertAllStagesStopped {
    //given
    val topic = createTopic()
    val userDao = new UserDao()
    val consumerSettings = consumerDefaults.withGroupId(createGroupId()).withStopTimeout(Duration.Zero)
    val committerSettings = CommitterSettings(system)
    val consumersManager = new UserConsumersManager(userDao, consumerSettings, committerSettings)

    //when
    val control: DrainingControl[Done] = consumersManager.consumerAtlLeastOneDelivery(topic).run()
    awaitProduce(produceString(topic, List("user1", "user2")))

    //then
    control.drainAndShutdown().futureValue shouldBe Done

  }
}
