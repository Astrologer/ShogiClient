package web.shogi

import web.core.{BussClient, ObjectsStore, GameEngine, MouseListener}
import web.util.Networking

object ShogiEngine extends BussClient with ObjectsStore with GameEngine with MouseListener with Networking
