package test

import org.specs.Specification
import bizondemand.utils.models.internet.{Parameter, Url, DomainName, Http}
import bizondemand.utils.models.internet.Protocol._

import java.net.URLEncoder

class ParameterSpecs extends Specification {
	
 	"The Parameter class" should {
		"return key=value as it's string value" in {
			Parameter("key", "value").toString must be_==( URLEncoder.encode("key") + "=" + URLEncoder.encode("value"))
		}
		
		"return URL encoded values" in {
			Parameter("key", "!value").toString must be_==( URLEncoder.encode("key") + "=" + URLEncoder.encode("!value"))
		}
		
		"accept integers as a value" in {
			Parameter("key", 1).toString must be_==( URLEncoder.encode("key") + "=" + URLEncoder.encode(1.toString))
		}
		
		"accept longs as a value" in {
			Parameter("key", 1l).toString must be_==( URLEncoder.encode("key") + "=" + URLEncoder.encode(1l.toString))
		}
		
		"accept float as a value" in {
			Parameter("key", 1.0).toString must be_==( URLEncoder.encode("key") + "=" + URLEncoder.encode(1.0.toString))
		}
		
		"accept double as a value" in {
			Parameter("key", 1.0d).toString must be_==( URLEncoder.encode("key") + "=" + URLEncoder.encode(1.0d.toString))
		}
	}
	
}