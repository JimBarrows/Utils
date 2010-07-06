package test

import org.specs.Specification
import bizondemand.utils.models.internet.{Parameter, Url, DomainName, Http}
import bizondemand.utils.models.internet.Protocol._


class UrlSpecs extends Specification {

	val localhost = DomainName("localhost"  :: Nil)
	val localdomain = DomainName("localdomain"  :: Nil)
	val localhostLocaldomain = DomainName("localhost"  :: "localdomain" :: Nil)
	
 	"An url class" should {
		"represent http://localhost" in {
			val url = Url(Http(), None, None, localhost, None, None, None)
			 	url.toString must beEqualTo("http://localhost")
    	}

    	"represent http://localhost:8080" in {
      		Url(Http(), None, None, localhost, Some(8080), None, None).toString must beEqualTo("http://localhost:8080")
    		}

    	"represent http://localhost/some/path/to/some/resource.html" in {
      		Url(Http(), None, None, localhost, None, Some("some" :: "path" :: "to" :: "some" :: "resource.html" :: Nil), None)
      			.toString must beEqualTo("http://localhost/some/path/to/some/resource.html")
    	}

    "represent http://localhost/some/path/to/some/resource.html?param1=val1" in {
		Url(Http(), None, None,
    		localhost,
        	None,
        	Some("some" :: "path" :: "to" :: "some" :: "resource.html" :: Nil),
        	Some(Parameter("param1", "val1") :: Nil)).toString must beEqualTo("http://localhost/some/path/to/some/resource.html?param1=val1")
    }
    
    "represent http://localhost/some/path/to/some/resource.html?param1=val1&param2=val2" in {
    	Url(Http(),
    		None, None,
    		localhost,
    		None,
        	Some("some" :: "path" :: "to" :: "some" :: "resource.html" :: Nil),
        	Some(Parameter("param1", "val1") :: Parameter("param2", "val2") :: Nil)).toString must
        		beEqualTo("http://localhost/some/path/to/some/resource.html?param1=val1&param2=val2")
    }

    "be able to a param after creation" in {
    	val original = Url(Http(), None, None, localhost, None, None, None)
    	val modified = original.addParam(Parameter("key1", "val1"))
    	modified.toString must beEqualTo("http://localhost?key1=val1")
    	modified.toString must beDifferent(original.toString)
    }

    "be able to add an empty list of params to an empty list of params" in {
    	val original = Url(Http(), None, None, localhost, None, None, None)
    	val modified = original.addAllParams(Nil)
    	modified.toString must beEqualTo("http://localhost")
    }

    "be able to add a list of params to an empty List of params" in {
    	val original = Url(Http(), None, None, localhost, None, None, None)
    	val modified = original.addAllParams(Parameter("key1", "val1") :: Parameter("key2", "val2") :: Nil)
    	modified.toString must beEqualTo("http://localhost?key1=val1&key2=val2")
    	modified.toString must beDifferent(original.toString)
    }

    "be able to add a list of params to a List of params" in {
    	val original = Url(Http(), None, None, localhost, None, None, Some(Parameter("key1", "val1") :: Parameter("key2", "val2") :: Nil))
    	val modified = original.++&(Parameter("key3", "val3") :: Parameter("key4", "val4") :: Nil)
    	modified.toString must beEqualTo("http://localhost?key1=val1&key2=val2&key3=val3&key4=val4")
    	modified.toString must beDifferent(original.toString)
    }


    "be able to a path after creation" in {
    	val original = Url(Http(), None, None, localhost, None, None, None)
    	val modified = original.addPath("dir1")
    	modified.toString must beEqualTo("http://localhost/dir1")
    	modified.toString must beDifferent(original.toString)
    }

    "be able to add an empty list of paths to an empty list of paths" in {
     	val original = Url(Http(), None, None, localhost, None, None, None)
    	val modified = original.addAllPathParts(Nil)
    	modified.toString must beEqualTo("http://localhost")
    }

    "be able to add a list of paths to an empty List of paths" in {
    	val original = Url(Http(), None, None, localhost, None, None, None)
    	val modified = original.addAllPathParts("dir1" :: "dir2" :: Nil)
    	modified.toString must beEqualTo("http://localhost/dir1/dir2")
    	modified.toString must beDifferent(original.toString)
    }

    "be able to add a list of paths to a List of paths" in {
      val original = Url(Http(), None, None, localhost, None, Some("dir1" :: "dir2" :: Nil), None)
      val modified = original.++/("dir3" :: "dir4" :: Nil)
      modified.toString must beEqualTo("http://localhost/dir1/dir2/dir3/dir4")
      modified.toString must beDifferent(original.toString)
    }

    "be able to render http://username@domain" in {
      Url(Http(), Some("username"), None, localhost, None, None, None).toString must beEqualTo("http://username@localhost")
    }

    "be able to render http://username:password@domain" in {
      Url(Http(), Some("username"), Some("password"), localhost, None, None, None).toString must beEqualTo("http://username:password@localhost")
    }

    "be able to prepend a name to a Url " in {
      val original = Url(Http(), Some("username"), Some("password"), localdomain, None, None, None)
      val modified = original +< ("localhost")
      modified.toString must beEqualTo("http://username:password@localhost.localdomain")
      modified.toString must beDifferent(original.toString)
    }

    "be able to parse http://username:password@localhost.localdomain:80/my/path/to/here.xml?key1=param1&key2=param2" in {
      val actual = Url("http://username:password@localhost.localdomain:80/my/path/to/here.xml?key1=param1&key2=param2")
      val expected = new Url(Http(),
        Some("username"),
        Some("password"),
        localhostLocaldomain,
        Some(80),
        Some("my" :: "path" :: "to" :: "here.xml" :: Nil),
        Some(Parameter("key1", "param1") :: Parameter("key2", "param2") :: Nil))
      actual.toString must be_== (expected.toString)
    }

     "be able to parse http://username:password@localhost.localdomain:80/my/path/to/here.xml?key1=param1" in {
      val actual = Url("http://username:password@localhost.localdomain:80/my/path/to/here.xml?key1=param1")
      val expected = new Url(Http(),
        Some("username"),
        Some("password"),
        localhostLocaldomain,
        Some(80),
        Some("my" :: "path" :: "to" :: "here.xml" :: Nil),
        Some(Parameter("key1", "param1") ::  Nil))
      actual.toString must be_== (expected.toString)
    }

     "be able to parse http://username:password@localhost.localdomain:80/my/path/to/here.xml" in {
      val actual = Url("http://username:password@localhost.localdomain:80/my/path/to/here.xml")
      val expected = new Url(Http(),
        Some("username"),
        Some("password"),
        localhostLocaldomain,
        Some(80),
        Some("my" :: "path" :: "to" :: "here.xml" :: Nil),
        None)
      actual.toString must be_== (expected.toString)
    }

     "be able to parse http://username:password@localhost.localdomain:80/my" in {
      val actual = Url("http://username:password@localhost.localdomain:80/my")
      val expected = new Url(Http(),
        Some("username"),
        Some("password"),
        localhostLocaldomain,
        Some(80),
        Some("my" :: Nil),
        None)
      actual.toString must be_== (expected.toString)
    }

     "be able to parse http://username:password@localhost.localdomain:80" in {
      val actual = Url("http://username:password@localhost.localdomain:80")
      val expected = new Url(Http(),
        Some("username"),
        Some("password"),
        localhostLocaldomain,
        Some(80),
        None,
        None)
      actual.toString must be_== (expected.toString)
    }

    "be able to parse http://username:password@localhost.localdomain" in {
      val actual = Url("http://username:password@localhost.localdomain")
      val expected = new Url(Http(),
        Some("username"),
        Some("password"),
        localhostLocaldomain,
        None,
        None,
        None)
      actual.toString must be_== (expected.toString)
    }

    "be able to parse http://username:password@localhost" in {
      val actual = Url("http://username:password@localhost")
      val expected = new Url(Http(),
        Some("username"),
        Some("password"),
        localhost,
        None,
        None,
        None)
      actual.toString must be_== (expected.toString)
    }

    "be able to parse http://username@localhost" in {
      val actual = Url("http://username@localhost")
      val expected = new Url(Http(),
        Some("username"),
        None,
        localhost,
        None,
        None,
        None)
      actual.toString must be_== (expected.toString)
    }

    "be able to parse http://localhost" in {
      val actual = Url("http://localhost")
      val expected = new Url(Http(),
        None,
        None,
        localhost,
        None,
        None,
        None)
      actual.toString must be_== (expected.toString)
    }
    
    "be able to determine if 2 Url's are the same" in {
   		val actual = Url("http://localhost")
     	val expected = new Url(Http(),
        	None,
	        None,
    	    localhost,
        	None,
	        None,
    	    None) 	
    	actual must be_==( expected)
    }
    
    "be able to accept protocol, domainName, path and parameters only" in {
    	val expected = Url("http://localhost/path/to/something?param1=val1")
     	val actual = Url(Http(),        	
    	    localhost,
        	"path"::"to"::"something"::Nil,	        
    	    Parameter("param1", "val1")::Nil
    	    ) 	
    	actual must be_==( expected)
    }
    
    "be able to accept protocol, domainName, and path  only" in {
    	val expected = Url("http://localhost/path/to/something")
     	val actual = Url(Http(),        	
    	    localhost,
        	"path"::"to"::"something"::Nil
    	    ) 	
    	actual must be_==( expected)
    }
    
     "be able to accept protocol, domainName, and path  only" in {
    	val expected = Url("http://localhost")
     	val actual = Url(Http(),        	
    	    localhost
    	    ) 	
    	actual must be_==( expected)
    }
  }
}