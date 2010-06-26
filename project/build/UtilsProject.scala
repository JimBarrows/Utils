import sbt._

class UtilsProject(info: ProjectInfo) extends DefaultProject(info)
{
  
	override def repositories = Set(
		"jBoss" at "http://repository.jboss.org/maven2",
		"Multiverse Releases" at "http://multiverse.googlecode.com/svn/maven-repository/releases/",
		"GuiceyFruit" at "http://guiceyfruit.googlecode.com/svn/repo/releases/",
		"DataBinder" at "http://databinder.net/repo",
		"Configgy" at "http://www.lag.net/repo",
		"Akka Maven Repository" at "http://scalablesolutions.se/akka/repository",
		"Java.Net" at "http://download.java.net/maven/2",
		ScalaToolsSnapshots)

	override def libraryDependencies = Set(    		
		"junit" % "junit" % "4.5" % "test->default",
		"org.scala-tools.testing" % "specs" % "1.6.1" % "test->default",
		"org.slf4j" % "slf4j-log4j12" % "1.4.1",
		"joda-time" % "joda-time" % "1.6",
		"se.scalablesolutions.akka" % "akka-core_2.7.7" % "0.7.1"
		
  	) ++ super.libraryDependencies
  	
  	override def managedStyle =ManagedStyle.Maven
  	val publishTo = Resolver.ssh("BizOnDemand", "nsfwenterprises.com", "/var/local/artifacts/release/") as("jimbarrows")  	

}
