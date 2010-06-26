package bizondemand.utils.models

import _root_.bizondemand.utils.logging.Log
import scala.util.parsing.combinator._
import scala.util.parsing.combinator.syntactical._

import bizondemand.utils.RunParser


/**Using the actual RFC's create a domain name class to accurately represent a domain name.
The spec from RFC-1034 Section 3.5 Prefererred Name Syntax
The following syntax will result in fewer problems with many
applications that use domain names (e.g., mail, TELNET).

<domain> ::= <subdomain> | " "

<subdomain> ::= <label> | <subdomain> "." <label>

<label> ::= <letter> [ [ <ldh-str> ] <let-dig> ]

<ldh-str> ::= <let-dig-hyp> | <let-dig-hyp> <ldh-str>

<let-dig-hyp> ::= <let-dig> | "-"

<let-dig> ::= <letter> | <digit>

<letter> ::= any one of the 52 alphabetic characters A through Z in
upper case and a through z in lower case

<digit> ::= any one of the ten digits 0 through 9

Note that while upper and lower case letters are allowed in domain
names, no significance is attached to the case.  That is, two names with
the same spelling but different case are to be treated as if identical.

The labels must follow the rules for ARPANET host names.  They must
start with a letter, end with a letter or digit, and have as interior
characters only letters, digits, and hyphen.  There are also some
restrictions on the length.  Labels must be 63 characters or less.

For example, the following strings identify hosts in the Internet:

A.ISI.EDU  XX.LCS.MIT.EDU  SRI-NIC.ARPA



Read more: http://www.faqs.org/rfcs/rfc1034.html#ixzz0rDFc3BjL
*/
case class DomainName(subdomains:List[String]) {


}

object DomainName extends RegexParsers with RunParser{
		
	
	//<letter> ::= any one of the 52 alphabetic characters A through Z in
	//upper case and a through z in lower case
	lazy val letter :Parser[Any] = {
		println("letter")
		elem("letter", Character.isLetter)		
	}

	//<digit> ::= any one of the ten digits 0 through 9
	lazy val digit :Parser[Any]= {
		println("digit") 
		elem("digit", Character.isDigit)	
	}
	
	val dash ="-"
	
	lazy val dot: Parser[Any] = {
		println("dot")
		elem("dot", (c => {c == '.'}))
	}
		
	//<let-dig> ::= <letter> | <digit>
	lazy val let_dig : Parser[Any] =  {
		println("let_dig")
		letter | digit 	
	}
	
	//<let-dig-hyp> ::= <let-dig> | "-"
	lazy val let_dig_hyp:Parser[Any] = {
		println("let_dig_hyp") 
		let_dig ^^ { case ld => {
			println("let_dig_hyp ld: " + ld)
			ld
		}} | 
		"-" ^^^ dash		
	}
	
	//<ldh-str> ::= <let-dig-hyp> | <let-dig-hyp> <ldh-str>
	lazy val ldh_str:Parser[Any] = {
		println("ldh_str")
		let_dig_hyp ^^ {case s => {
			println("ldh_str s: " + s.toString)
			s.toString
		}} | 
		let_dig_hyp ~ ldh_str ^^ {
			case r ~ s => {
				println("ldh_str let_dig_hyp + ldh_str: " +r.toString + s.toString)
				r.toString + s.toString
			}
		}	
	}

	//<label> ::= <letter> [ [ <ldh-str> ] <let-dig> ]
	lazy val label :Parser[String] = {
		println("label") 
		letter ~ (ldh_str*) ^^ { case let ~ ldh => {
			println("label let: " + let + " ldh: " + ldh.mkString )
			let.toString + ldh.mkString 
		}}		
	}
	
	//<subdomain> ::= <label> | <subdomain> "." <label>
	lazy val subdomain:Parser[List[String]] = {
		println("subdomain")
		// label ^^ {case l => {
		// 			println("subdomain label: " + l.toString)
		// 			l.toString::Nil
		// 		}} |
		label ~ rep(dot ~ subdomain) ^^ {			
			case l ~ Nil => {
				println("#####subdomain l ~ Nil: " + l.toString + " ~ Nil" + " -- " + l.toString::Nil )
				l.toString::Nil
			}	
			case l ~ r => {
				println("subdomain l ~ r: " + l + " ~ " + r.mkString + " -- " + (l.toString :: r :: Nil).asInstanceOf[List[String]])
				(l :: r :: Nil).asInstanceOf[List[String]]
			}	
		}
	}
		
	//<domain> ::= <subdomain> | " "
	lazy val domain : Parser[DomainName] = {
		println("domain")
		subdomain  ^^ { case s => {
			println("domain " + s.toString)
			DomainName( s )
		}} | 
		" " ^^ {case s => {
			println("domain " + s.toString)
			DomainName(Nil)
		}}			
	}
	
	
	type RootType = String
	
	def root = label
	
}