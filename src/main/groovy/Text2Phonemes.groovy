import marytts.LocalMaryInterface
import marytts.Version
import groovy.xml.XmlUtil

class Text2Phonemes {

    static void main(args) {
        def (localeStr, textDirPath, xmlDirPath) = args.take(3)

        // init mary
        def locale = Locale.forLanguageTag(localeStr == 'en' ? 'en-US' : localeStr)
        def mary = new LocalMaryInterface()
        mary.locale = locale
        mary.outputType = 'PHONEMES'
        println "MaryTTS ${Version.specificationVersion()}, ${mary.locale.displayLanguage}"

        // init input, output directories, parser
        def textDir = new File(textDirPath)
        def xmlDir = new File(xmlDirPath)
        def parser = new XmlSlurper(false, false)

        // process
        textDir.eachFile { inputFile ->
            println "Synthesizing $inputFile.name"
            def doc = mary.generateXML(inputFile.text)
            def xmlStr = XmlUtil.serialize(doc.documentElement)
            def xml = parser.parseText(xmlStr)
            def outputFile = new File(xmlDir, inputFile.name - 'txt' + 'xml')
            outputFile.text = XmlUtil.serialize(xml)
            println "Wrote $outputFile.name"
        }
    }

}
