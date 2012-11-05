package tw.dictionary.api.server

import cc.spray.can.model._
import cc.spray.can.server.HttpServer
import cc.spray.util.DateTime
import cc.spray.io.ConnectionClosedReason
import akka.util.duration._
import akka.actor._
import akka.pattern.ask
import tw.dictionary.api.parser.model.Words.Word
import common._
import tw.dictionary.api._
import tw.dictionary.api.parser.YahooParser
class TestService extends Actor with ActorLogging {
  import HttpMethods._

  val yahooParser = new YahooParser

  protected def receive = {

    case HttpRequest(GET, "/", _, _, _) =>
      sender ! index

    case HttpRequest(GET, "/ping", _, _, _) =>
      sender ! response("PONG!")

    case HttpRequest(GET, "/stats", _, _, _) =>
      val client = sender
      context.actorFor("../http-server").ask(HttpServer.GetStats)(1.second).onSuccess {
        case x: HttpServer.Stats => client ! statsPresentation(x)
      }

    /**
      *
      *  Crash and timeout
      *
      */

    case HttpRequest(GET, "/timeout", _, _, _) =>
      log.info("Dropping request, triggering a timeout")

    case HttpRequest(GET, "/timeout/timeout", _, _, _) =>
      log.info("Dropping request, triggering a timeout")

    case HttpRequest(GET, "/stop", _, _, _) =>
      sender ! response("Shutting down in 1 second ...")
      context.system.scheduler.scheduleOnce(1.second, new Runnable { def run() { context.system.shutdown() } })

    /**
      *
      * Service
      *
      *      case class HttpRequest(
      *                  method: HttpMethod = HttpMethods.GET,
      *                  uri: String = "/",
      *                  headers: List[HttpHeader] = Nil,
      *                  content: Option[HttpContent] = None,
      *                  protocol: HttpProtocol = `HTTP/1.1`)
      */

    case HttpRequest(GET, uri, _, _, _) if uri.startsWith("/search") => {
    	val word = uri.split("/").last
    	sender ! ProtobufResponse(yahooParser.LookUp(word).get, 200)
    }

    /**
      *
      * 	Error Handling
      *
      */
    case _: HttpRequest => sender ! response("Unknown resource!", 404)

    case HttpServer.RequestTimeout(HttpRequest(_, "/timeout/timeout", _, _, _)) =>
      log.info("Dropping RequestTimeout message")

    case HttpServer.RequestTimeout(request) =>
      sender ! HttpResponse(status = 500).withBody {
        "The " + request.method + " request to '" + request.uri + "' has timed out..."
      }

    case x: HttpServer.Closed =>
      context.children.foreach(_ ! CancelStream(sender, x.reason))
  }

  ////////////// Header and Response //////////////

  //Text Response
  val defaultHeaders = List(HttpHeader("Content-Type", "text/plain"))

  def response(msg: String, status: Int = 200) =
    HttpResponse(status, defaultHeaders, msg.getBytes("ISO-8859-1"))

  //Google protocol buffer Response
  val protobufHeaders = List(HttpHeader("Content-Type", "application/x-protobuf"))

  def ProtobufResponse(word: Word, status: Int = 200) = HttpResponse(
    headers = defaultHeaders,
    body = word.toByteArray,
    status = status)

  //Index Response
  lazy val index = HttpResponse(
    headers = List(HttpHeader("Content-Type", "text/html")),
    body =
      <html>
        <body>
          <h1>Say hello to <i>spray-can</i>!</h1>
          <p>Defined resources:</p>
          <ul>
            <li><a href="/ping">/ping</a></li>
            <li><a href="/search">/search</a></li>
            <li><a href="/stats">/stats</a></li>
            <li><a href="/crash">/crash</a></li>
            <li><a href="/timeout">/timeout</a></li>
            <li><a href="/timeout/timeout">/timeout/timeout</a></li>
            <li><a href="/stop">/stop</a></li>
          </ul>
        </body>
      </html>.toString.getBytes("ISO-8859-1"))

  //Stats Response
  def statsPresentation(s: HttpServer.Stats) = HttpResponse(
    headers = List(HttpHeader("Content-Type", "text/html")),
    body =
      <html>
        <body>
          <h1>HttpServer Stats</h1>
          <table>
            <tr><td>uptime:</td><td>{ s.uptime.printHMS }</td></tr>
            <tr><td>totalRequests:</td><td>{ s.totalRequests }</td></tr>
            <tr><td>openRequests:</td><td>{ s.openRequests }</td></tr>
            <tr><td>maxOpenRequests:</td><td>{ s.maxOpenRequests }</td></tr>
            <tr><td>totalConnections:</td><td>{ s.totalConnections }</td></tr>
            <tr><td>openConnections:</td><td>{ s.openConnections }</td></tr>
            <tr><td>maxOpenConnections:</td><td>{ s.maxOpenConnections }</td></tr>
            <tr><td>requestTimeouts:</td><td>{ s.requestTimeouts }</td></tr>
            <tr><td>idleTimeouts:</td><td>{ s.idleTimeouts }</td></tr>
          </table>
        </body>
      </html>.toString.getBytes("ISO-8859-1"))

  case class CancelStream(peer: ActorRef, reason: ConnectionClosedReason)
}