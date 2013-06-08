package pparkkin.games.immortals.server

import akka.actor.ActorSystem
import pparkkin.games.immortals.tcp.{TCPDataProcessor, TCPListener}

object ImmortalsServer extends App {
  val system = ActorSystem("ImmortalsServer")
  val game = ImmortalsGame.newInstance(system)
  val dp = TCPDataProcessor.newInstance(system, game)
  val listener = TCPListener.newInstance(system, 1204, dp)
  game ! Start

}
