import groovy.io.FileType

import org.custommonkey.xmlunit.*

import org.testng.annotations.*

class PhonemesXmlTest {

    def referenceXmlDir
    def bleedingXmlDir

    @BeforeSuite
    void setup() {
        referenceXmlDir = new File(System.properties.ReferenceXmlDir)
        assert referenceXmlDir
        bleedingXmlDir = new File(System.properties.BleedingXmlDir)
        assert bleedingXmlDir
    }

    @DataProvider
    Object[][] xmlFiles() {
        def xmlFiles = []
        referenceXmlDir.eachFileMatch(FileType.FILES, ~/.*\.xml$/, { referenceFile ->
            def bleedingFile = new File(bleedingXmlDir, referenceFile.name)
            xmlFiles << [referenceFile, bleedingFile]
        })
        xmlFiles
    }

    @Test(dataProvider = 'xmlFiles')
    void compareXmlFiles(File expectedFile, File actualFile) {
        def diff = XMLUnit.compareXML(expectedFile.text, actualFile.text)
        def details = new DetailedDiff(diff)
        assert details.similar()
    }
}
