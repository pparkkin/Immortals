package pparkkin.games.immortals.client.controller

import akka.actor._
import pparkkin.games.immortals.client.ui.GameFrame

case class Quit()

class GameController(connection: ActorRef) extends Actor with ActorLogging {
  override def preStart = {
    GameFrame.open(self)
  }

  def receive = {
    case Quit => System.exit(0)
  }
}

object GameController {
  def newInstance(factory: ActorRefFactory, connection: ActorRef): ActorRef = {
    factory.actorOf(Props(new GameController(connection)))
  }
}
