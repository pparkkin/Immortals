package pparkkin.games.immortals.server.game

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import akka.actor._
import pparkkin.games.immortals.messages._
import scala.util.Random
import pparkkin.games.immortals.datatypes.Players
import pparkkin.games.immortals.messages.Welcome
import pparkkin.games.immortals.messages.Update
import pparkkin.games.immortals.messages.Join
import pparkkin.games.immortals.messages.PlayerPositions
import scala.Some

case class Start()
case class End()
case class Tick()

class ImmortalsGame(controller: ActorRef, name: String) extends Actor with ActorLogging {
  val BOARD_WIDTH = 20
  val BOARD_HEIGHT = 20
  private var board = GameOfLife.randomBoard(BOARD_WIDTH, BOARD_HEIGHT)
  private var tick: Option[Cancellable] = None
  private var players = Players.empty
  private var playerConns = Map.empty[String, ActorRef]

  def receive = {
    case Start =>
      log.debug("Game start.")
      tick = Some(context.system.scheduler.schedule(0.milliseconds,
        500.milliseconds, self, Tick))
    case Join(player) =>
      log.debug(s"New player $player joined.")
      sender ! Welcome(player, name, board)
    case WelcomeAck(player) =>
      log.debug(s"Received welcome ack from $player.")
      players = players + (player -> randomPosition(BOARD_WIDTH, BOARD_HEIGHT))
      playerConns = playerConns + (player -> sender)
      playerConns.map((e) => e._2 ! PlayerPositions(players))
    case End =>
      tick.map(_.cancel())
        .getOrElse(log.warning("No tick when stopping game."))
      log.debug("Game end.")
    case Tick =>
      log.debug("Tick.")
      board = GameOfLife.tick(board)
      players.foreach((e) => board = board.updated(e._2._1, e._2._2, true))
      playerConns.map((e) => e._2 ! Update(board))
    case Move(player, move) =>
      log.debug("Move.")
      players = players + (player -> updatePosition(players(player), move))
      playerConns.map((e) => e._2 ! PlayerPositions(players))
    case m =>
      log.info(s"Unknown message $m.")
  }

  def randomPosition(x: Int, y: Int) = (Random.nextInt(x), Random.nextInt(y))

  def updatePosition(pos: (Int, Int), move: (Int, Int)) = {
    val x = math.min(math.max(pos._1+move._1, 0), BOARD_WIDTH-1)
    val y = math.min(math.max(pos._2+move._2, 0), BOARD_HEIGHT-1)
    (x, y)
  }
}

object ImmortalsGame {
  def newInstance(system: ActorRefFactory, controller: ActorRef, name: String): ActorRef = {
    system.actorOf(Props(new ImmortalsGame(controller, name)), "Game")
  }

}
