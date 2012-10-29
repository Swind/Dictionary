package tw.dictionary.api.utils
import java.io.BufferedReader
import java.io.FileReader
import java.io.FileWriter
import java.io.FileOutputStream
import tw.dictionary.api.parser.YahooParser
import tw.dictionary.api.parser.model.Words


object DownloadFromWeb {
  val wordsListPath = "./data/GEPTmediumhigh.txt"
  val dictionaryPath = "./data/Yahoo.dic"

  def main(args: Array[String]) {
	  val wordStream = new BufferedReader(new FileReader(wordsListPath))
	  val dicStream = new FileOutputStream(dictionaryPath, false)
	  val parser = new YahooParser
	  
	  handleAllLine(wordStream)
	  {
	    line=>
	      if(!line.isEmpty())
	      {
	        try{
	          println(line)
	    	  parser.LookUp(line).writeDelimitedTo(dicStream)
	        }
	        catch{
	          case e:Exception => e.printStackTrace()
	        }
	      }
	  }
	  
	  dicStream.close()
  }
   def handleAllLine(reader: BufferedReader)(handler: (String) => Unit): Unit = {
      val line = reader.readLine()
      if (line != null) {
        handler(line)
        handleAllLine(reader)(handler)
      }
    }
}