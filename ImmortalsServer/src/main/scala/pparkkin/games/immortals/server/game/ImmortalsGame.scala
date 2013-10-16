package pparkkin.games.immortals.server.game

import akka.actor._
import pparkkin.games.immortals.messages.{Welcome, Join}

case class Start()
case class End()

class ImmortalsGame(controller: ActorRef) extends Actor with ActorLogging {
  def receive = {
    case Start =>
      log.debug("Game start.")
    case Join(player) =>
      log.debug(s"New player $player joined.")
      sender ! Welcome(player, "Hoshino Game")
    case End =>
      log.debug("Game end.")
  }
}

object ImmortalsGame {
  def newInstance(system: ActorRefFactory, controller: ActorRef): ActorRef = {
    system.actorOf(Props(new ImmortalsGame(controller)), "Game")
  }

}
