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

  val PronunciationName= "div.pronunciation"
  val AudioName = "cite#audio1"
  val AudioURLAttrName = "value"
    
  val InterpretElementTag = "div[class=def clr nobr]"
  val SpeechTagName = "div[class=caption]"
  val ExplainElementName = "div.list ol li"
  val ExplainTitleName = "p.interpret"
  val ExampleTagName = "p.example"
}

class YahooParser extends DictionaryParser{
  import tw.dictionary.api.parser.YahooParser._
  import tw.dictionary.api.parser.model.GoogleProtoBufFactory._
  
  def LookUp(word: String) = {
    println(word)
    try
    {
    	val doc = Jsoup.connect(DictionaryURL + word).get
    	println(doc.toString)
    	Word(word, parsePronunciation(doc), parseInterpret(doc))
    }
    catch
    {
      case _ => emptyWord(word)
    }
  }
  
  def SelectAndMapToList[T](elem: Element, selectName: String)(mapAction: (Element) => T): List[T] = elem.select(selectName).map(mapAction).toList

  //pronunciation
  def parsePronunciation(doc: Document) = Pronunciation(doc.select(PronunciationName).first.text, parseAudioURL(doc))
  
  //audio URL
  def parseAudioURL(doc: Document) = doc.select(AudioName).first.toString
  
 
  
  //Interpret
  def parseInterpret(doc: Document) = SelectAndMapToList(doc, InterpretElementTag) {
    elem => Interpret(getSpeech(elem), parseExplain(elem))
  }
  def getSpeech(interpretElement: Element) = interpretElement.select(SpeechTagName).first.text
  
  //Explain
  def parseExplain(interpretElement: Element): List[Words.Explain] = SelectAndMapToList(interpretElement, ExplainElementName) {
    elem => Explain(getFirstMatchText(elem, ExplainTitleName), parseExample(elem))
  }
  def getFirstMatchText(element: Element, pattern: String) = element.select(pattern).first.text

  //Example
  def parseExample(explainElement: Element):List[Words.Example] = SelectAndMapToList(explainElement, ExampleTagName) {
    elem => Example(elem.text)
  }

  

  

}
