package tw.dictionary.api.parser.model

object GoogleProtoBufFactory {
  
	def Word(word:String, interprets:List[Words.Interpret]) = {
		val builder = Words.Word.newBuilder
		builder.setWord(word)
		interprets.foreach(builder.addInterprets)
		builder.build
	}
	
	def Interpret(speech:String, explains:List[Words.Explain]) = {
		val builder = Words.Interpret.newBuilder
		builder setSpeech speech
		explains.foreach(builder.addExplains)
		builder.build
	}
	
	def Explain(content:String, examples:List[Words.Example]) = {
		val builder = Words.Explain.newBuilder
		builder setContent content
		examples.foreach(builder.addExamples)
		builder.build
	}
	
	def Example(content:String) = Words.Example.newBuilder.setContent(content).build
}