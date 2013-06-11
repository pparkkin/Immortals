package pparkkin.games.immortals.tcp

import akka.actor.{ActorRef, ActorRefFactory}
import java.net.InetSocketAddress

trait TCPActorFactory {
  def newActor(factory: ActorRefFactory, address: InetSocketAddress, dataProcessor: ActorRef): ActorRef
}
