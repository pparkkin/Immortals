package pparkkin.games.immortals.server.game

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import akka.actor._
import pparkkin.games.immortals.messages.{Update, Welcome, Join}

case class Start()
case class End()
case class Tick()

class ImmortalsGame(controller: ActorRef, name: String) extends Actor with ActorLogging {
  private var board = GameOfLife.randomBoard(20, 20)
  private var tick: Option[Cancellable] = None

  def receive = {
    case Start =>
      log.debug("Game start.")
      tick = Some(context.system.scheduler.schedule(0.milliseconds,
        500.milliseconds, self, Tick))
    case Join(player) =>
      log.debug(s"New player $player joined.")
      sender ! Welcome(player, name)
    case End =>
      tick.map(_.cancel())
        .getOrElse(log.warning("No tick when stopping game."))
      log.debug("Game end.")
    case Tick =>
      log.debug("Tick.")
      board = GameOfLife.tick(board)
      controller ! Update(board)
  }
}

object ImmortalsGame {
  def newInstance(system: ActorRefFactory, controller: ActorRef, name: String): ActorRef = {
    system.actorOf(Props(new ImmortalsGame(controller, name)), "Game")
  }

}
