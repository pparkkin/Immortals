package pparkkin.games.immortals.server.game

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import akka.actor._
import pparkkin.games.immortals.messages.{PlayerPositions, Update, Welcome, Join}
import scala.util.Random
import pparkkin.games.immortals.datatypes.Players

case class Start()
case class End()
case class Tick()

class ImmortalsGame(controller: ActorRef, name: String) extends Actor with ActorLogging {
  val BOARD_WIDTH = 20
  val BOARD_HEIGHT = 20
  private var board = GameOfLife.randomBoard(BOARD_WIDTH, BOARD_HEIGHT)
  private var tick: Option[Cancellable] = None
  private var players = Players.empty

  def receive = {
    case Start =>
      log.debug("Game start.")
      tick = Some(context.system.scheduler.schedule(0.milliseconds,
        500.milliseconds, self, Tick))
    case Join(player) =>
      log.debug(s"New player $player joined.")
      players = players + (player -> randomPosition(BOARD_WIDTH, BOARD_HEIGHT))
      sender ! Welcome(player, name)
      sender ! Update(board)
      controller ! PlayerPositions(players)
    case End =>
      tick.map(_.cancel())
        .getOrElse(log.warning("No tick when stopping game."))
      log.debug("Game end.")
    case Tick =>
      log.debug("Tick.")
      board = GameOfLife.tick(board)
      controller ! Update(board)
  }

  def randomPosition(x: Int, y: Int) = (Random.nextInt(x), Random.nextInt(y))
}

object ImmortalsGame {
  def newInstance(system: ActorRefFactory, controller: ActorRef, name: String): ActorRef = {
    system.actorOf(Props(new ImmortalsGame(controller, name)), "Game")
  }

}
