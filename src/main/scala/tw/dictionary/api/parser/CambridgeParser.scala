package tw.dictionary.api.parser
import common._
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import tw.dictionary.api.parser.model.Words

class CambridgeParser extends DictionaryParser {
  val DictionaryURL = """http://dictionary.cambridge.org/search/american-english/direct/?q="""
  val directNameInHeader = "Location"
  val urlPattern = """http:\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&amp;:/~\+#]*[\w\-\@?^=%&amp;/~\+#])?""".r

  lazy val wordParser = (word: String, doc: Document) => ???

  def LookUp(word: String) = {
    try {
      val doc = Jsoup.connect(DictionaryURL + word).get
      Some(wordParser(word, doc))
    } catch {
      case _ => None
    }
  }
  
  //Data Structure
  import tw.dictionary.api.parser.model.GoogleProtoBufFactory._
  import scala.collection.JavaConversions._
  
  def pronunciation(pronunciationTextF: (Document) => String, audioF: (Document) => String)(doc: Document) = Pronunciation(pronunciationTextF(doc), audioF(doc))
  def interpret(speechF: (Document) => String, explainF: (Element) => List[Words.Explain])(doc: Document) = Interpret(speechF(doc), explainF(doc))
  def explains(explainTextF:(Element)=>String, exampleF: (Element) => List[Words.Example])(element: Element) = element.select("div[class=def-block]").map{
    block=>
      Explain(explainTextF(block), exampleF(block))
  }.toList
  def examples(element: Element) = element.select("blockquote.examp").map(result => Example(result.text))
  
  //Parse Cambridge Dictionary
  def pronunciationText(doc:Document) = doc.select("span.pron").first.text
  def posgramText(doc:Document) = doc.select("span.posgram").first.text
  def audioURL(doc:Document) = urlPattern.findFirstIn(doc.select("img.sound").first.attr("onclick")).getOrElse("")
  
  val DefinitionTagName = "div[class=def-block]"
  
    
  def descriptionText(defBlock:Element) = defBlock.select("span.def").first.text
  def exampleText(defBlock:Element) = defBlock.select("blockquote.examp").first.text
}