package tw.dictionary.api.parser
import common._
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import tw.dictionary.api.parser.model.Words
object CambridgeParser{
	def main(args: Array[String]) {
		val parser = new CambridgeParser
		parser.LookUp("test")
	}
}
class CambridgeParser extends DictionaryParser {
  val DictionaryURL = """http://dictionary.cambridge.org/search/american-english/direct/?q="""
  val urlPattern = """http:\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&amp;:/~\+#]*[\w\-\@?^=%&amp;/~\+#])?""".r

  lazy val wordParser:(String, Document) => Words.Word = 
    word(
      pronunciation
      (
        pronunciationText,
        audioURL
      ),
      interpret
      (
        posgramText,
        explains
        (
          descriptionText,
          examples
        )
      )
    )_

  def LookUp(word: String) = {
    try {
      val doc = Jsoup.connect(DictionaryURL + word).get
      val searched = wordParser(word, doc)
      val moreResult = Some(getMoreResult(searched, resultList(doc)))
      println("second:" + moreResult.get)
      moreResult
    } catch {
      case _ => None
    }
  }

  //Data Structure
  import tw.dictionary.api.parser.model.GoogleProtoBufFactory._
  import scala.collection.JavaConversions._

  def word(pronunciationF: Document => Words.Pronunciation, interpretF: Document => List[Words.Interpret])(text: String, doc: Document) =
    Word(text, pronunciationF(doc), interpretF(doc))
    
  def pronunciation(pronunciationTextF: (Document) => String, audioF: (Document) => String)(doc: Document) = Pronunciation(pronunciationTextF(doc), audioF(doc))
  
  def interpret(speechF: (Document) => String, explainF: (Element) => List[Words.Explain])(doc: Document) = List(Interpret(speechF(doc), explainF(doc)))
  
  def explains(explainTextF: (Element) => String, exampleF: (Element) => List[Words.Example])(element: Element) = element.select("div[class=def-block]").map {
    block =>
      Explain(explainTextF(block), exampleF(block))
  }.toList
  
  def examples(element: Element) = element.select("blockquote.examp").map(result => Example(result.text)).toList

  //Parse Cambridge Dictionary
  def pronunciationText(doc: Document) = doc.select("span.pron").first.text
  def posgramText(doc: Document) = doc.select("span.posgram").first.text
  def audioURL(doc: Document) = urlPattern.findFirstIn(doc.select("img.sound").first.attr("onclick")).getOrElse("")

  val DefinitionTagName = "div[class=def-block]"
  def descriptionText(defBlock: Element) = defBlock.select("span.def").first.text
  def exampleText(defBlock: Element) = defBlock.select("blockquote.examp").first.text
  
  def resultList(doc:Document) = doc.select("ul[class=result-list]").first.select("li").map(result=>(result.select("span.hw").first.text, result)).toList
  
  //Get More resule
  def getMoreResult(searched:Words.Word, results:List[(String,Element)]) = {
    
    val interprets = results.filter(_._1 == searched.getWord).flatMap{
    	word=>
    	  val url = word._2.select("a").first.attr("href")
    	  val doc = Jsoup.connect(url).get
    	  wordParser(word._1, doc).getInterpretsList()
    }
    
    
    Word(searched.getWord(),searched.getPronunciation(), searched.getInterpretsList().toList ::: interprets.toList)
	 
  }
}