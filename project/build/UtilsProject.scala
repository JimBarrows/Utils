import sbt._

class UtilsProject(info: ProjectInfo) extends DefaultProject(info)
{
  
	val maven = DefaultMavenRepository
	val scalaToolsRelease = ScalaToolsReleases
	val scalaToolsSnapshots = ScalaToolsSnapshots
	val javaNet = JavaNet1Repository	
	def url = new java.net.URL("http://bizondemand.biz/artifacts")
	val bizOnDemand = Resolver.url("BizOnDemand", url)
		
			
	val jetty = "org.mortbay.jetty" % "jetty" % "6.1.22" % "test->default"
	val junit = "junit" % "junit" % "4.5" % "test->default"	
	val logging = "org.slf4j" % "slf4j-log4j12" % "1.4.1"
	val jodaTime = "joda-time" % "joda-time" % "1.6"	
	
	val specs = buildScalaVersion match {
		case "2.7.7" => "org.scala-tools.testing" % "specs" % "1.6.2.1" % "test->default"
		case "2.8.0" => "org.scala-tools.testing" %% "specs" % "1.6.5" % "test->default"
	}
  	
  	val publishTo = Resolver.ssh("BizOnDemand", "bizondemand.biz", "/var/local/artifacts/release/")  	

}
