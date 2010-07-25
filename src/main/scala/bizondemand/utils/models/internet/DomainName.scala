package bizondemand {
	package utils {
		package models.internet {

			import logging.Log
			import scala.util.parsing.combinator._
			import scala.util.parsing.combinator.syntactical._
			
			
			
			
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
			
				lazy val tld = subdomains.last
				
				lazy val site = subdomains.dropRight(1).last
				
				override def toString = subdomains.mkString("",".","")
			
			}
			
			object DomainName extends RegexParsers with RunParser with Log{
					
				
				//<letter> ::= any one of the 52 alphabetic characters A through Z in
				//upper case and a through z in lower case
				lazy val letter :Parser[Any] = {
					trace("letter")
					elem("letter", Character.isLetter)		
				}
			
				//<digit> ::= any one of the ten digits 0 through 9
				lazy val digit :Parser[Any]= {
					trace("digit") 
					elem("digit", Character.isDigit)	
				}
				
				val dash ="-"
				
				lazy val dot: Parser[Any] = {
					trace("dot")
					elem("dot", (c => {c == '.'}))
				}
					
				//<let-dig> ::= <letter> | <digit>
				lazy val let_dig : Parser[Any] =  {
					trace("let_dig")
					letter | digit 	
				}
				
				//<let-dig-hyp> ::= <let-dig> | "-"
				lazy val let_dig_hyp:Parser[Any] = {
					debug("let_dig_hyp") 
					let_dig ^^ { case ld => {
						debug("let_dig_hyp ld: " + ld)
						ld
					}} | 
					"-" ^^^ dash		
				}
				
				//<ldh-str> ::= <let-dig-hyp> | <let-dig-hyp> <ldh-str>
				lazy val ldh_str:Parser[Any] = {
					debug("ldh_str")
					let_dig_hyp ^^ {case s => {
						debug("ldh_str s: " + s.toString)
						s.toString
					}} | 
					let_dig_hyp ~ ldh_str ^^ {
						case r ~ s => {
							debug("ldh_str let_dig_hyp + ldh_str: " +r.toString + s.toString)
							r.toString + s.toString
						}
					}	
				}
			
				//<label> ::= <letter> [ [ <ldh-str> ] <let-dig> ]
				lazy val label :Parser[String] = {
					debug("label") 
					letter ~ (ldh_str*) ^^ { case let ~ ldh => {
						debug("label let: " + let + " ldh: " + ldh.mkString )
						let.toString + ldh.mkString 
					}}		
				}
				
				//<subdomain> ::= <label> | <subdomain> "." <label>
				lazy val subdomain:Parser[List[String]] = {
					debug("subdomain")		
					label ~ opt(dot ~ subdomain) ^^ {			
						case l ~ None => {
							debug("subdomain l ~ None: " + l.toString + " ~ None" + " -- " + (l.toString::Nil).toString )
							l.toString::Nil
						}	
						case l ~ Some(d ~ s) => {
							debug("subdomain l ~ dot ~ s: " + l + " ~ " + d + " ~ " +s)
							l :: s
						}	
					}
				}
					
				//<domain> ::= <subdomain> | " "
				lazy val domain : Parser[DomainName] = {
					debug("domain")
					subdomain  ^^ { case s => {
						debug("domain " + s.toString)
						DomainName( s )
					}} | 
					" " ^^ {case s => {
						debug("domain " + s.toString)
						DomainName(Nil)
					}}			
				}
				
				
				type RootType = DomainName
				
				def root = domain
				
			}
		}
	}
}