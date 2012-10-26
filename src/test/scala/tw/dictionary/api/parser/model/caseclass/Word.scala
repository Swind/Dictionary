package tw.dictionary.api.parser.model.caseclass


/*
 * 
 * Data Struct
 * 
 * Word
 * 		|
 * 		[Interpret] 

 * 				|---SpeechEnum
 * 				|---[Explain]
 * 						|
 * 						|--- Content
 * 						|--- [Example]
 * 								|
 * 								|--- Content
 */
case class Word(val word:String, val interprets:List[Interpret])
case class Interpret(val speech:String, val explains:List[Explain])
case class Explain(val content:String, val examples:List[Example])
case class Example(val content:String)

