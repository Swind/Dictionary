package tw.dictionary.api.utils

import java.io.BufferedReader
import java.io.FileOutputStream
import java.io.FileReader
import java.util.concurrent.Executors
import akka.dispatch.Await
import akka.dispatch.ExecutionContext
import tw.dictionary.api.parser.YahooParser
import akka.dispatch.Future
import akka.util.Timeout
import akka.util.duration._

object DownloadFromWeb {
  val wordsListPath = "./data/GEPTmediumhigh.txt"
  val dictionaryPath = "./data/Yahoo.dic"

  val es = Executors.newFixedThreadPool(5)
  implicit val ec = ExecutionContext.fromExecutorService(es)  
  
  def main(args: Array[String]) {
	  val wordStream = new BufferedReader(new FileReader(wordsListPath))
	  val dicStream = new FileOutputStream(dictionaryPath, false)
	  val parser = new YahooParser
	  
	  val wordsList = futureQueryWords(handleAllLine(wordStream), parser) 
	  
	  wordsList.foreach(_.writeDelimitedTo(dicStream))
	  
	  dicStream.close()
  }
  
  def futureQueryWords(wordsList:List[String], parser:YahooParser) = Await.result(Future.traverse(wordsList)(word => Future(parser.LookUp(word))), 30 minutes)  
   def handleAllLine(reader: BufferedReader, wordsList: List[String] = List[String]()): List[String] = {
      val line = reader.readLine()
      if (line != null) 
        handleAllLine(reader, line::wordsList)
      else
        wordsList
    }
}