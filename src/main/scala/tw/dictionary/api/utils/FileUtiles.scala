package tw.dictionary.api.utils
import scala.io.Source

object FileUtiles {
  def readFileToList(filePath:String) = Source.fromFile(filePath,"UTF-8").getLines
  def readFileToString(filePath:String) = readFileToList(filePath).mkString("")
}