package tw.dictionary.api.utils
import java.io.FileInputStream
import tw.dictionary.api.parser.model.Words
import java.io.FileOutputStream
import tw.dictionary.api.parser.YahooParser

object ReadFormDic {
  def main(args: Array[String]) {
    
	  val tmpPath = "./data/tmp.dic"
	  val parser = new YahooParser
	  
	  //Write to tmp file
	  /*
	  val writeStream = new FileOutputStream(tmpPath)
	  parser.LookUp("a").writeDelimitedTo(writeStream)
	  parser.LookUp("test").writeDelimitedTo(writeStream)
	  parser.LookUp("search").writeDelimitedTo(writeStream)
	  writeStream.close()
	  */
	  
	  val readStream = new FileInputStream(tmpPath)
	  println(Words.Word.parseDelimitedFrom(readStream).getWord())
	  println(Words.Word.parseDelimitedFrom(readStream).getWord)
	  println(Words.Word.parseDelimitedFrom(readStream).getWord)
	  println(Words.Word.parseDelimitedFrom(readStream))
	  readStream.close()
  }
}