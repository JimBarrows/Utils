import sbt._

class UtilsProject(info: ProjectInfo) extends DefaultProject(info)
{
  
	val maven = DefaultMavenRepository
	val scalaToolsRelease = ScalaToolsReleases
	val scalaToolsSnapshots = ScalaToolsSnapshots
	val javaNet = JavaNet1Repository
	val jboss = "jBoss" at "http://repository.jboss.org/maven2"
	val guicey = "GuiceyFruit" at "http://guiceyfruit.googlecode.com/svn/repo/releases/"
	val databinder = "DataBinder" at "http://databinder.net/repo"
	//val configgy = 	"Configgy" at "http://www.lag.net/repo"
	//val akkRepo = "Akka Maven Repository" at "http://scalablesolutions.se/akka/repository"
	val bizOnDemand = Resolver.ssh("BizOnDemand", "nsfwenterprises.com", "/var/local/artifacts/release/") as("jimbarrows")
		
			
	val jetty = "org.mortbay.jetty" % "jetty" % "6.1.22" % "test->default"
	val junit = "junit" % "junit" % "4.5" % "test->default"	
	val logging = "org.slf4j" % "slf4j-log4j12" % "1.4.1"
	val jodaTime = "joda-time" % "joda-time" % "1.6"
	//val akka = "se.scalablesolutions.akka" %% "akka-core" % "0.7.1"
	
	val specs = buildScalaVersion match {
		case "2.7.7" => "org.scala-tools.testing" % "specs" % "1.6.2.1" % "test->default"
		case "2.8.0" => "org.scala-tools.testing" %% "specs" % "1.6.5" % "test->default"
	}
  	
  	override def managedStyle =ManagedStyle.Maven
  	val publishTo = Resolver.ssh("BizOnDemand", "nsfwenterprises.com", "/var/local/artifacts/release/") as("jimbarrows")  	

}
