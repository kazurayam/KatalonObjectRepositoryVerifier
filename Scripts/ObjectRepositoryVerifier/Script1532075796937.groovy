import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

import org.w3c.dom.Document

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI


DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance()
XPath xpath = XPathFactory.newInstance().newXPath()

Path objectRepository = Paths.get('Object Repository')
List<Path> rsFiles = Files.walk(objectRepository)
	.filter({p -> Files.isRegularFile(p)})
	.collect(Collectors.toList())
for (Path p : rsFiles) {
	DocumentBuilder db = dbf.newDocumentBuilder()
	Document doc = db.parse(p.toFile())
	String name = (String)xpath.evaluate("/WebElementEntity/name", doc, XPathConstants.STRING)
	WebUI.comment("name=${name}")
}