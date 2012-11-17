package tw.dictionary.api.parser

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class CambridgeParserTest extends FunSuite with ShouldMatchers{
  
  test("Get word url from Cambridge dictionary"){
    val parser = new CambridgeParser
    val word = "test"
  }
  
  
}