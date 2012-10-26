package tw.dictionary.api.parser.model

object SpeechEnum extends Enumeration{
	val noun = Value("n.")
	val pronoun = Value("pron.")
	val adjective = Value("adj.")
	val verb = Value("v.")
	val adverb = Value("adverb.")
	val preposition = Value("prep.")
	val conjunction = Value("conj.")
	val interjection = Value("int.")
}
