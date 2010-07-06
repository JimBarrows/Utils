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
case class URL(protocol: String,
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

  def addParam(param: Parameter): URL = new URL(protocol, username, password, domainName, port, path, Some(param :: parameters.getOrElse(Nil)))

  def addAllParams(paramList: List[Parameter]): URL = paramList match {
    case Nil => new URL(protocol, username, password, domainName, port, path, parameters)
    case _ => new URL(protocol, username, password, domainName, port, path, Some(parameters.getOrElse(Nil) ::: paramList))
  }

  def addPath(pathPart: String): URL = new URL(protocol, username, password, domainName, port, Some(path.getOrElse(Nil) ::: pathPart :: Nil), parameters)

  def addAllPathParts(pathPartsList: List[String]) = pathPartsList match {
    case Nil => new URL(protocol, username, password, domainName, port, path, parameters)
    case _ => new URL(protocol, username, password, domainName, port, Some(path.getOrElse(Nil) ::: pathPartsList), parameters)
  }

  def prependDomainName(name: String): URL = new URL(protocol, username, password, DomainName( name :: domainName.subdomains), port, path, parameters)

  override def toString = protocol + "://" + username.getOrElse("") + (if (password.isDefined) ":" else "") + password.getOrElse("") + (if (username.isDefined) "@" else "") +
          domainName +
          port.map(p => ":" + p).getOrElse("") +
          path.map(_.mkString("/", "/", "")).getOrElse("") +
          parameters.map(_.mkString("?", "&", "")).getOrElse("")
}

object URL extends Log{
  def apply(url: String) :URL = {
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

    new URL(parsed.getProtocol,
      username,
      password,
      DomainName.run(parsed.getHost).getOrElse(DomainName(""::Nil)),
      if( parsed.getPort < 0) None else Some(parsed.getPort),
      if (parsed.getPath == null || parsed.getPath.isEmpty) None else Some(parsed.getPath.split("/").toList.drop(1)),
      if (parsed.getQuery == null || parsed.getQuery.isEmpty)
        None
      else Some(parsed.getQuery.split("&").toList.map(param => {
        var pair = param.split("=")
        Parameter(pair(0), pair(1))
      })))
  }
}

object LocalHost80 extends URL("http", None, None, DomainName("localhost"::Nil), None, None, None)

object LocalHost8080 extends URL("http", None, None, DomainName("localhost"::Nil), Some(8080), None, None)
