package tw.dictionary.api.parser

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import tw.dictionary.api.utils.FileUtiles
import org.jsoup.Jsoup

@RunWith(classOf[JUnitRunner])
class CambridgeParserTest extends FunSuite with ShouldMatchers{
  
  val testFilePath = "./data/CambridgeSearchTest.html"
  lazy val testFullContent = FileUtiles.readFileToString(testFilePath)

  val doc = Jsoup parse testFullContent
  
  val parser = new CambridgeParser
  
  test("Test parse content in the html"){
    //pronunciation
    parser pronunciationText doc should be("""/test/""")
    
    //posgram
    parser posgramText doc should be("n [C]")
    
    //audioURL
    parser audioURL doc should be("""http://dictionary.cambridge.org/media/american-english/us_pron/t/tes/test_/test.mp3""")
    
    
    val defBlock = doc.select("div[class=def-block]").first
    //description
    parser descriptionText defBlock should be("a set of questions or practical activities that show what someone knows or what someone or something can do or is like:")
    
    //example
    parser exampleText defBlock should be("a spelling test") 
  }
  
  
  
}