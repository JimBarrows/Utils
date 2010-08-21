package bizondemand {
	package utils {

		import scala.util.parsing.combinator._

		/**Convience trait for parsers, copied right from dpp's book.
		*/
		trait RunParser {
			this: RegexParsers =>
			
			type RootType
			
			def root: Parser[RootType]
			
			def run(in:String): ParseResult[RootType] = parseAll(root, in)
		}
	}
}