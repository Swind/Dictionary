package tw.dictionary.api.utils
import java.io.FileInputStream
import tw.dictionary.api.parser.model.Words
import java.io.FileOutputStream
import tw.dictionary.api.parser.YahooParser
import tw.dictionary.api.parser.model.Words.Word
import java.io.InputStream

object ReadFormDic {
  def main(args: Array[String]) {

    val tmpPath = "./data/Yahoo.dic"
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

    readFromStream(readStream) {
      word =>
        println(word.getWord)
    }

    readStream.close()
  }

  def readFromStream[T](stream: InputStream)(handler: (Word) => T):Unit = {
    val word = Words.Word.parseDelimitedFrom(stream)
    if (word != null) {
      handler(word)
      readFromStream(stream)(handler)
    }
  }

}