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
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.util.KeywordUtil

/**
 * see https://forum.katalon.com/discussion/8454/is-there-a-way-to-mass-edit-many-objects-in-the-object-repository
 * 
 * @author kazurayam
 *
 */

KeywordLogger logger = new KeywordLogger()

DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance()
XPath xpath = XPathFactory.newInstance().newXPath()

List<String> expressions = [
	'/WebElementEntity/webElementProperties[name="tag" and isSelected="true"]',
	'/WebElementEntity/webElementProperties[name="class" and isSelected="true"]'
]

Path objectRepository = Paths.get('Object Repository')
List<Path> rsFiles = Files.walk(objectRepository)
	.filter {p -> Files.isRegularFile(p)}
	.filter {p -> p.toString().endsWith('.rs')}
	.collect(Collectors.toList())

def failureCount = 0
for (Path p : rsFiles) {
	DocumentBuilder db = dbf.newDocumentBuilder()
	Document doc = db.parse(p.toFile())
	String name = (String)xpath.evaluate("/WebElementEntity/name", doc, XPathConstants.STRING)
	Boolean isBasic = (Boolean)xpath.evaluate('/WebElementEntity/selectorMethod[. = "BASIC"]', doc, XPathConstants.BOOLEAN)
	if (isBasic) {
		for (String expr : expressions) {
			Boolean result = (Boolean)xpath.evaluate(expr, doc, XPathConstants.BOOLEAN)
			if (!result) {
				logger.logFailed("FAILURE ${p} against XPath \"${expr}\"")
				failureCount += 1	
			}
		}
	}
}

if (failureCount > 0) {
	KeywordUtil.markFailed("${failureCount} failures")
}