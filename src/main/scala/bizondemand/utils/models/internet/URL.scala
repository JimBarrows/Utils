package bizondemand.utils.models.internet

import _root_.bizondemand.utils.logging.Log
import java.net.URLEncoder
import java.net.{URL => JavaUrl}

/**
 *
 * @author jimbarrows
 * @created: Dec 31, 2009 11:08:55 AM
 * @version 1.0
 *
 */

case class Parameter(key: String, value: String) {	
	override def toString = URLEncoder.encode(key) + "=" + URLEncoder.encode(value)
}

object Parameter {
	def apply( key:String, value:Int) :Parameter = Parameter(key, value.toString)
	def apply( key:String, value:Long) :Parameter = Parameter(key, value.toString)
	def apply( key:String, value:Float) :Parameter = Parameter(key, value.toString)
	def apply( key:String, value:Double) :Parameter = Parameter(key, value.toString)	
}




class Protocol(protocol:String) {
	override def toString = protocol
}

object Protocol {
	def apply(protocol:String) :Protocol = {
		protocol.toUpperCase match {
			case "HTTP" => Http()
			case "HTTPS" => Https()
			case "FTP" => Ftp()
			case "MAIL" =>Mail()
			case other => new Protocol(other)
		}
	}
}

case class Http() extends Protocol("http") {}
case class Https() extends Protocol ("https") {}
case class Ftp() extends Protocol("ftp") {}
case class Mail() extends Protocol("mail"){}

case class Url(protocol: Protocol,
        username: Option[String],
        password: Option[String],
        domainName: DomainName,
        port: Option[Int], 
        path: Option[List[String]], 
        parameters: Option[List[Parameter]]) {    
        	
  	def +&(param: Parameter) = addParam(param)

  	def ++&(paramList: List[Parameter]) = addAllParams(paramList)

	def +/(pathPart: String) = addPath(pathPart)

	def ++/(pathPartsList: List[String]) = addAllPathParts(pathPartsList)

	def +<(name: String) = prependDomainName(name)

	def addParam(param: Parameter): Url = new Url(protocol, username, password, domainName, port, path, Some(param :: parameters.getOrElse(Nil)))

  	def addAllParams(paramList: List[Parameter]): Url = paramList match {
    	case Nil => new Url(protocol, username, password, domainName, port, path, parameters)
    	case _ => new Url(protocol, username, password, domainName, port, path, Some(parameters.getOrElse(Nil) ::: paramList))
 	}

  	def addPath(pathPart: String): Url = new Url(protocol, username, password, domainName, port, Some(path.getOrElse(Nil) ::: pathPart :: Nil), parameters)

  	def addAllPathParts(pathPartsList: List[String]) = pathPartsList match {
    	case Nil => new Url(protocol, username, password, domainName, port, path, parameters)
    	case _ => new Url(protocol, username, password, domainName, port, Some(path.getOrElse(Nil) ::: pathPartsList), parameters)
  	}

  	def prependDomainName(name: String): Url = new Url(protocol, username, password, DomainName( name :: domainName.subdomains), port, path, parameters)

  	override def toString = protocol + "://" + username.getOrElse("") + (if (password.isDefined) ":" else "") + password.getOrElse("") + (if (username.isDefined) "@" else "") +
          domainName +
          port.map(p => ":" + p).getOrElse("") +
          path.map(_.mkString("/", "/", "")).getOrElse("") +
          parameters.map(_.mkString("?", "&", "")).getOrElse("")
}

object Url extends Log{
	def apply(url: String) :Url = {
    	val parsed: JavaUrl = new JavaUrl(url)
    
    	var username :Option[String]= None
    	var password : Option[String] = None
    	if( parsed.getUserInfo != null && ! parsed.getUserInfo.isEmpty) {
      		val pair = parsed.getUserInfo.split(":")
      		username = Some(pair(0))
      		if ( pair.length ==2) {
        		password = Some(pair(1)) 
      		}
    	}

    	Url( Protocol(parsed.getProtocol),
      		username,
      		password,
      		DomainName.run(parsed.getHost).getOrElse(DomainName(""::Nil)),
    		if( parsed.getPort < 0) None else Some(parsed.getPort),
      		if (parsed.getPath == null || parsed.getPath.isEmpty) None else Some(parsed.getPath.split("/").toList.drop(1)),
      		if (parsed.getQuery == null || parsed.getQuery.isEmpty)
        		None
      		else 
      			Some(parsed.getQuery.split("&").toList.map(param => {
        				var pair = param.split("=")
        				Parameter(pair(0), pair(1))
      				})
      			)
      	)
	}
	
	def apply(protocol:String, domainName:DomainName, path:List[String], parameters:List[Parameter]) :Url = {
		Url( new Protocol(protocol), None, None, domainName, None, Some(path), Some(parameters))
	}
		
	def apply(protocol:String, domainName:DomainName, path:List[String]) :Url = {
		Url( new Protocol(protocol), None, None, domainName, None, Some(path), None)
	}
			
	def apply(protocol:String, domainName:DomainName) :Url = {
		Url( new Protocol(protocol), None, None, domainName, None, None, None)
	}
		
	def apply(protocol:Protocol, domainName:DomainName, path:List[String], parameters:List[Parameter]) :Url = {
		Url( protocol, None, None, domainName, None, Some(path), Some(parameters))
	}
			
	def apply(protocol:Protocol, domainName:DomainName, path:List[String]) :Url = {
		Url( protocol, None, None, domainName, None, Some(path), None)
	}
			
	def apply(protocol:Protocol, domainName:DomainName) :Url = {
		Url( protocol, None, None, domainName, None, None, None)
	}
}

object LocalHost80 extends Url(Http(), None, None, DomainName("localhost"::Nil), None, None, None) {}

object LocalHost8080 extends Url(Http(), None, None, DomainName("localhost"::Nil), Some(8080), None, None)
