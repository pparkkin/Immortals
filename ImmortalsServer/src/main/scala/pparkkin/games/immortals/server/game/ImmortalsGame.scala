package pparkkin.games.immortals.server.game

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import akka.actor._
import pparkkin.games.immortals.messages.{Welcome, Join}

case class Start()
case class End()
case class Tick()

class ImmortalsGame(controller: ActorRef) extends Actor with ActorLogging {
  val board = GameOfLife.randomBoard(20, 20)
  private var tick: Option[Cancellable] = None

  def receive = {
    case Start =>
      log.debug("Game start.")
      tick = Some(context.system.scheduler.schedule(0.milliseconds,
        500.milliseconds, self, Tick))
    case Join(player) =>
      log.debug(s"New player $player joined.")
      sender ! Welcome(player, "Hoshino Game")
    case End =>
      tick.map(_.cancel())
        .getOrElse(log.warning("No tick when stopping game."))
      log.debug("Game end.")
    case Tick =>
      log.debug("Tick.")
  }
}

object ImmortalsGame {
  def newInstance(system: ActorRefFactory, controller: ActorRef): ActorRef = {
    system.actorOf(Props(new ImmortalsGame(controller)), "Game")
  }

}
