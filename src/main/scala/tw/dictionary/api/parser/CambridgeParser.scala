package tw.dictionary.api.parser
import common._
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class CambridgeParser extends DictionaryParser {
  val DictionaryURL = """http://dictionary.cambridge.org/search/american-english/direct/?q="""
  val directNameInHeader = "Location"

  lazy val wordParser = (word: String, doc: Document) => ???

  def LookUp(word: String) = {
    try {
      val doc = Jsoup.connect(DictionaryURL + word).get
      Some(wordParser(word, doc))
    } catch {
      case _ => None
    }
  }
  
  
}