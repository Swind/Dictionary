package tw.dictionary.api.parser

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite

@RunWith(classOf[JUnitRunner])
class CambridgeParserTest extends FunSuite{
	test("Get test interpret"){
	  val parser = new CambridgeParser
	  val word = parser.LookUp("test")
	  
	  
	}
}