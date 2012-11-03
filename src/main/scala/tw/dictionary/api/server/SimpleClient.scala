package tw.dictionary.api.server

import cc.spray.can.client.{HttpDialog, HttpClient}
import cc.spray.can.model.{HttpResponse, HttpRequest}
import cc.spray.io.IoWorker
import akka.dispatch.Future
import akka.actor.{ActorSystem, Props}
import tw.dictionary.api.parser.model.Words.Word

object GoogleQueryExample extends App {
  implicit val system = ActorSystem()
  def log = system.log

  // every spray-can HttpClient (and HttpServer) needs an IoWorker for low-level network IO
  // (but several servers and/or clients can share one)
  val ioWorker = new IoWorker(system).start()

  // create and start the spray-can HttpClient
  val httpClient = system.actorOf(
    props = Props(new HttpClient(ioWorker)),
    name = "http-client"
  )

  val queries = Seq("test")

  val requests = queries.map(q => HttpRequest(uri = "/search/" + q.replace(" ", "+")))

  log.info("Running {} word queries over a single connection using pipelined requests...", requests.length)
  timed(HttpDialog(httpClient, "localhost",8080).send(requests).end)
    .onSuccess(printResult andThen secondRun)
    .onFailure(printError andThen shutdown)

  def secondRun: PartialFunction[Any, Unit] = {
    case _ =>
      log.info("Running google queries as separate requests (in parallel) ...")
      timed(Future.sequence(requests.map(r => HttpDialog(httpClient, "localhost",8080).send(r).end)))
        .onSuccess(printResult andThen shutdown)
        .onFailure(printError andThen shutdown)
  }

  def printResult: PartialFunction[(Seq[HttpResponse], Long), Unit] = {
    case (responses, time) =>
      log.info(responses.map(response => Word.parseFrom(response.body).toString).mkString("Result bytes: ", ", ", "."))
      val rate = queries.size * 1000 / time
      log.info("Completed: {} requests in {} ms at a rate of  {} req/sec\n", queries.size, time, rate)
  }

  def printError: PartialFunction[Throwable, Unit] = {
    case e: Exception => log.error("Error: {}", e)
  }

  def shutdown: PartialFunction[Any, Unit] = {
    case _ =>
      system.shutdown()
      ioWorker.stop()
  }

  def timed[T](block: => Future[T]) = {
    val startTime = System.currentTimeMillis
    block.map(_ -> (System.currentTimeMillis - startTime))
  }
}