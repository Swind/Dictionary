package tw.dictionary.api.parser

import java.util.Dictionary
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import scala.collection.JavaConversions._
import tw.dictionary.api.parser.model._

object YahooParser {
  val DictionaryURL = """http://tw.dictionary.yahoo.com/dictionary?p="""

  val InterpretElementTag = "div[class=def clr nobr]"
  val SpeechTagName = "div[class=caption]"
  val ExplainElementName = "div.list ol li"
  val ExplainTitleName = "p.interpret"
  val ExampleTagName = "p.example"
  def main(args: Array[String]) {
    val parser = new YahooParser
    println(parser.LookUp("Search"))

  }
}

class YahooParser {
  import tw.dictionary.api.parser.YahooParser._
  import tw.dictionary.api.parser.model.GoogleProtoBufFactory._
  
  def LookUp(word: String) = {
    val doc = Jsoup.connect(DictionaryURL + word).get
    Word(word, parseInterpret(doc))
  }
  
  def SelectAndMapToList[T](elem: Element, selectName: String)(mapAction: (Element) => T): List[T] = elem.select(selectName).map(mapAction).toList
  
  def parseInterpret(doc: Document) = SelectAndMapToList(doc, InterpretElementTag) {
    elem => Interpret(getSpeech(elem), parseExplain(elem))
  }
  def getSpeech(interpretElement: Element) = interpretElement.select(SpeechTagName).first.text
  
  
  def parseExplain(interpretElement: Element): List[Words.Explain] = SelectAndMapToList(interpretElement, ExplainElementName) {
    elem => Explain(getFirstMatchText(elem, ExplainTitleName), parseExample(elem))
  }
  def getFirstMatchText(element: Element, pattern: String) = element.select(pattern).first.text

  def parseExample(explainElement: Element):List[Words.Example] = SelectAndMapToList(explainElement, ExampleTagName) {
    elem => Example(elem.text)
  }

  

  

}