import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.Source
import javax.xml.transform.stream.StreamSource
import javax.xml.transform.Result
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory
import org.w3c.dom.Document

import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * see https://forum.katalon.com/discussion/8454/is-there-a-way-to-mass-edit-many-objects-in-the-object-repository
 * 
 * @author kazurayam
 *
 */
WebUI.comment("ObjectRepositoryTransformation started")

KeywordLogger logger = new KeywordLogger()

// *.rs files in the 'Object Repository' directory
Path sourceOR = Paths.get('Object Repository')
List<Path> rsFiles = Files.walk(sourceOR)
	.filter {p -> Files.isRegularFile(p)}
	.filter {p -> p.toString().endsWith('.rs')}
	.collect(Collectors.toList())

// create a directory where the transformed *.rs files are stored
Path resultOR = Paths.get(System.getProperty('user.dir')).resolve('transformed_Object_Repository')
Files.createDirectories(resultOR)
WebUI.comment("created a directory ${resultOR}")

// instanciate Factories of XML/XSLT processing tools
DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance()
XPath xpath = XPathFactory.newInstance().newXPath()
TransformerFactory tFactory = TransformerFactory.newInstance()

def failureCount = 0

// now process XML files
for (Path p : rsFiles) {
	
	// create XSLT processors
	Transformer identityTransformer = tFactory.newTransformer(createIdentityTransformSource())
	Transformer testObjectTransformer = tFactory.newTransformer(createTestObjectTransformSource())


	// subpath under 'Object Repository' dir, e.g., 'Page_CURA Healthcare Service/a_Go to Homepage'
	Path subpath = sourceOR.relativize(p)
	//println "subpath='${subpath}'"
	
	// read input XML to instanciate a DOM
	DocumentBuilder db = dbf.newDocumentBuilder()
	Document doc = db.parse(p.toFile())
	String name = (String)xpath.evaluate("/WebElementEntity/name", doc, XPathConstants.STRING)
	println "processing '${name}' in '${p}'"
	
	// prepare input source
	Source source = new DOMSource(doc)
	
	// check if the Test Object uses BASIC selector
	Boolean isBasic = (Boolean)xpath.evaluate('/WebElementEntity/selectorMethod[. = "BASIC"]', doc, XPathConstants.BOOLEAN)
	if (isBasic) {
		// resolve output path, create directories
		Path outPath = resultOR.resolve(subpath)
		Files.createDirectories(outPath.getParent())
		File outFile = outPath.toFile()
		println "outFile='${outFile.toString()}'"
		Result result = new StreamResult(new FileOutputStream(outFile))
		// execute XSLT processor
		try {
			testObjectTransformer.transform(source, result)
		} catch (TransformerException e) {
			failureCount += 1
			logger.logFailed("FAILED transforming ${p} : ${e.getMessage()}")
		}
	}
}

if (failureCount > 0) {
	KeywordUtil.markFailed("${failureCount} failures")
}



/**
 * This method returns a StreamSource instance which wraps a XSLT stylesheet for 
 * identity-transform : copy a input XML document to output with no modification
 * 
 * @return javax.xml.transform.StreamSource
 */
StreamSource createIdentityTransformSource() {
	def stylesheet = '''<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
'''
	return new StreamSource(new StringReader(stylesheet))
}


/**
 * This method returns a StreamSource instance which wraps a XSLT stylesheet.
 * The stylesheet is designed to transform a *.rs file stored in the Katalon Studio's Object Repository directory.
 * A *.rs file is the definition of a Test Object.
 * 
 * The stylesheet transforms a XML <WebElementEntity> document to a slightly reduced format.
 * It looks at nodes of xpath:'/WebElementEntity/webElementProperties'.
 * It copies a node that matches xpath '/WebElementEntity/webElementProperties[name='tag' or name='class'] and
 * it discards other webElementProperties nodes. 
 */
StreamSource createTestObjectTransformSource() {
	def stylesheet = '''<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="WebElementEntity">
      <xsl:copy>
          <xsl:choose>
              <xsl:when test="selectorMethod/text() = 'BASIC'">
                  <xsl:apply-templates select="@*|node()"/>
              </xsl:when>
              <xsl:otherwise>
                  <xsl:copy-of select="." />
              </xsl:otherwise>
          </xsl:choose>
      </xsl:copy>
  </xsl:template>

  <xsl:template match="webElementProperties">
      <xsl:choose>
          <!--
          <xsl:when test="isSelected/text() = 'true'">
              <xsl:copy-of select="."/>
          </xsl:when>
          -->
          <xsl:when test="name='tag' or name='class'">
			  <xsl:copy-of select="." />
          </xsl:when>
          <xsl:otherwise>
              <!-- discard other webElementProperites nodes -->
          </xsl:otherwise>
      </xsl:choose>
  </xsl:template>

  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>

</xsl:stylesheet>

'''
	return new StreamSource(new StringReader(stylesheet))
}