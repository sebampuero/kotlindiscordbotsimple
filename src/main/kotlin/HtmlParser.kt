package chistosito

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class HtmlParser (champ: String) {

    private var doc: Document = Jsoup.connect("https://rankedboost.com/league-of-legends/build/$champ").get()

    fun text(id: String, clas: String? = null): String {
        val element: Element = doc.getElementById(id) ?: return ""
        if(clas != null){
            if(element.getElementsByClass(clas).first() != null){
                return element.getElementsByClass(clas).first()!!.text()
            }
        }
        return element.text()
    }

    //TODO: add fn for images and more customization

}

suspend fun parser(champ: String,  block: suspend HtmlParser.() -> Unit): Unit =
    HtmlParser(champ).block()