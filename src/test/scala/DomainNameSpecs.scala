package test

import org.specs._
import scala.util.parsing.combinator._
import scala.util.parsing.combinator.syntactical._

import  bizondemand.utils.models.DomainName

class DomainNameSpecs extends Specification {
	
	"A domain name" should {
		 "parse localhost" in {
		 	val expected = DomainName("localhost"  :: Nil)		 			
		 	val r = DomainName.run("localhost")	
		 	//r.getOrElse(fail( r.toString)) must be_== (expected)			
		 	r.getOrElse(fail( r.toString)) must be_== ("localhost")
		 }
		 
		 "parse loca123" in {
		 	val r = DomainName.run("local123")
		 	r.getOrElse(fail(r.toString)) must be_==("local123")	
		 }
		 
		 "not parse 123local" in {
		 	val r = DomainName.run("123local")
		 	r.getOrElse(fail(r.toString)) must be_==("local123")	
		 }
		
		
		// "parse localhost.localdomain" in {
		// 			val expected = DomainName("localhost"  :: "localdomain" :: Nil)
		// 			
		// 			val r = DomainName.run("localhost.localdomain")	
		// 			println("Returned value:" +r)
		// 			//r.getOrElse(fail( r.toString)) must be_== (expected)			
		// 		}
	}
	
}