package com.softwaremill.k8s.example

import akka.Done
import akka.actor.ActorSystem
import akka.kafka.{CommitterSettings, ConsumerSettings, Subscriptions}
import akka.kafka.scaladsl.Consumer.DrainingControl
import akka.kafka.scaladsl.{Committer, Consumer}
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.{ExecutionContext, Future}

class UserConsumersManager(userDao: UserDao, consumerSettings: ConsumerSettings[String, String], committerSettings: CommitterSettings)(
    implicit val system: ActorSystem,
    ec: ExecutionContext
) extends StrictLogging {

  def consumerAtlLeastOneDelivery(topic: String) = {
    Consumer
      .committableSource(consumerSettings, Subscriptions.topics(topic))
      .mapAsync(1) { msg =>
        processing(msg.record.key, msg.record.value).map(_ => {
          val offset = msg.committableOffset
          logger.info(s"Comiting offset $offset")
          offset
        })
      }
      .toMat(Committer.sink(committerSettings))(DrainingControl.apply)
  }

  def processing(key: String, value: String): Future[Done] = {
    logger.info(s"Processing record : $value")
    Future.successful(Done)
  }
}
