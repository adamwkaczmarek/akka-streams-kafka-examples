package com.softwaremill.k8s.example

import com.typesafe.scalalogging.StrictLogging

class UserDao extends StrictLogging {

  def saveUser(user: User): Unit =
    logger.info(s"Saving user ${user}")

}
