package tw.dictionary.api.parser

import tw.dictionary.api.parser.model.Words.Word

trait DictionaryParser {
	def LookUp(word: String):Option[Word]
}