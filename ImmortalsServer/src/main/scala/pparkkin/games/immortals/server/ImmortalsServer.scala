package pparkkin.games.immortals.server

import akka.actor.ActorSystem

object ImmortalsServer extends App {
  val system = ActorSystem("ImmortalsServer")
  val game = ImmortalsGame.newInstance(system)
  game ! Start

}
