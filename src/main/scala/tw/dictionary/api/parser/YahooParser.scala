package tw.dictionary.api.parser

import scala.collection.JavaConversions._

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.jsoup.Jsoup

import tw.dictionary.api.parser.model._

object YahooParser {
  val DictionaryURL = """http://tw.dictionary.yahoo.com/dictionary?p="""

  val PronunciationName = "div.pronunciation"
  val AudioName = "cite#audio1"
  val AudioURLAttrName = "value"

  val InterpretElementTag = "div[class=def clr nobr]"
  val SpeechTagName = "div[class=caption]"
  val ExplainElementName = "div.list ol li"
  val ExplainTitleName = "p.interpret"
  val ExampleTagName = "p.example"
  val httpRegex = """(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&amp;:/~\+#]*[\w\-\@?^=%&amp;/~\+#])?""".r
    
  def main(args: Array[String]) {
    val parser = new YahooParser
    println(parser.LookUp("search"))
  }
}

class YahooParser extends DictionaryParser {
  import tw.dictionary.api.parser.YahooParser._
  import tw.dictionary.api.parser.model.GoogleProtoBufFactory._

  val wordParser =
    word(
      pronunciation
      (
        pronunciationText,
        audioText
      ),
      interprets
      (
        speechText,
        explains
        (
          explainText,
          examples
        )
      )
    )_

  def LookUp(searchWord: String) = {
    try {
      
      val doc = Jsoup.connect(DictionaryURL + searchWord).get
      Some(wordParser(searchWord, doc))
      
    } catch {
      case _ => None
    }
  }

  def word(pronunciationF: Document => Words.Pronunciation, interpretF: Document => List[Words.Interpret])(text: String, doc: Document) =
    Word(text, pronunciationF(doc), interpretF(doc))

  /**
   * Pronunciation
   */
  def pronunciation(pronunciationTextF: (Document) => String, audioF: (Document) => String)(doc: Document) = Pronunciation(pronunciationTextF(doc), audioF(doc))
  //pronunciation text
  def pronunciationText(doc: Document) = doc.select(PronunciationName).first.text
  //audio URL
  def audioText(doc: Document) = 
  {
	httpRegex.findFirstIn(doc.select("body > script").get(1).toString) match
	{
	  case Some(audioURL) => audioURL
	  case None => ""
	}
  }

  /**
   * interprets
   */
  def interprets(speechF: (Element) => String, explainF: (Element) => List[Words.Explain])(doc: Document) = SelectAndMapToList(doc, InterpretElementTag) {
    elem => Interpret(speechF(elem), explainF(elem))
  }
  def speechText(interpretElement: Element) = interpretElement.select(SpeechTagName).first.text

  /**
   * Explain
   */
  def explainText(element: Element) = getFirstMatchText(element, ExplainTitleName)
  def explains(explainTextF: (Element) => String, exampleF: (Element) => List[Words.Example])(element: Element) = SelectAndMapToList(element, ExplainElementName) {
    elem => Explain(explainTextF(elem), exampleF(elem))
  }

  /**
   * Example
   */
  def examples(element: Element) = SelectAndMapToList(element, ExampleTagName) {
    elem => Example(elem.text)
  }

  /**
   * Utils
   */
  private def SelectAndMapToList[T](elem: Element, selectName: String)(mapAction: (Element) => T): List[T] = elem.select(selectName).map(mapAction).toList
  private def getFirstMatchText(element: Element, pattern: String) = element.select(pattern).first.text

}
