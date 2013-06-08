package pparkkin.games.immortals.server

import akka.actor._
import pparkkin.games.immortals.tcp.End

case class Start()

class ImmortalsGame extends Actor with ActorLogging {
  def receive = {
    case Start =>
      log.info("Game start.")
      //self ! End
    case End =>
      log.info("Game end.")
      context.system.shutdown()
  }
}

object ImmortalsGame {
  def newInstance(system: ActorSystem): ActorRef = {
    system.actorOf(Props(new ImmortalsGame), "Game")
  }

}
