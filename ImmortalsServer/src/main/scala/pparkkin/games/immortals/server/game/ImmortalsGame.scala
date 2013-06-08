package pparkkin.games.immortals.server.game

import akka.actor._
import pparkkin.games.immortals.tcp.End
import pparkkin.games.immortals.server.controller.Exit

case class Start()

class ImmortalsGame(controller: ActorRef) extends Actor with ActorLogging {
  def receive = {
    case Start =>
      log.info("Game start.")
      //self ! End
    case End =>
      log.info("Game end.")
      controller ! Exit
  }
}

object ImmortalsGame {
  def newInstance(system: ActorRefFactory, controller: ActorRef): ActorRef = {
    system.actorOf(Props(new ImmortalsGame(controller)), "Game")
  }

}
