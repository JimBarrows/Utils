import sbt._

class UtilsProject(info: ProjectInfo) extends DefaultProject(info)
{
  
	 val bizOnDemand_libs_Releases     =  "libs-releases-local"   at 
			"http://bizondemand.biz:8081/artifactory/libs-releases-local"
  val bizOnDemand_libs_Snapshots    = "libs-snapshots-local"   at 
			"http://bizondemand.biz:8081/artifactory/libs-snapshots-local"
  val bizOnDemand_plugins_Releases  = "plugins-releases-local" at 
			"http://bizondemand.biz:8081/artifactory/plugins-releases-local"
  val bizOnDemand_plugins_Snapshots = "plugins-snaphots-local" at 
			"http://bizondemand.biz:8081/artifactory/plugins-snapshots-local"
  
	val scalatoolsSnapshot = 
    "Scala Tools Snapshot" at "http://scala-tools.org/repo-snapshots/"

	override def managedStyle=ManagedStyle.Maven
	Credentials(Path.userHome / ".ivy2" / ".credentials", log)
  val publishTo = "Biz On Demand Artifacts" at "http://bizondemand.biz:8081/artifactory/libs-releases-local"
	
			
	val jetty = "org.mortbay.jetty" % "jetty" % "6.1.22" % "test->default"
	val junit = "junit" % "junit" % "4.5" % "test->default"	
	val logging = "org.slf4j" % "slf4j-log4j12" % "1.4.1"
	val jodaTime = "joda-time" % "joda-time" % "1.6"	
	
	val specs = buildScalaVersion match {
		case "2.7.7" => "org.scala-tools.testing" % "specs" % "1.6.2.1" % "test->default"
		case "2.8.0" => "org.scala-tools.testing" %% "specs" % "1.6.5" % "test->default"
		case "2.9.0" => "org.scala-tools.testing" %% "specs" % "1.6.8" % "test->default"
	}
  	
}
