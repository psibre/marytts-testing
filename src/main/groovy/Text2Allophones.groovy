import marytts.LocalMaryInterface
import marytts.Version

class Text2Allophones {

    static void main(args) {
        def localeStr = args.first()
        def locale = Locale.forLanguageTag(localeStr == 'en' ? 'en-US' : localeStr)
        def mary = new LocalMaryInterface()
        mary.locale = locale
        println "MaryTTS ${Version.specificationVersion()}, ${mary.locale.displayLanguage}"
    }

}
