package chistosito

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class HtmlParser (website: String) {

    private var doc: Document = Jsoup.connect(website).get()

    fun text(id: String, clas: String? = null): String {
        val element: Element = doc.getElementById(id) ?: return ""
        if(clas != null){
            if(element.getElementsByClass(clas).first() != null){
                return element.getElementsByClass(clas).first()!!.text()
            }
        }
        return element.text()
    }

    fun setOfClassElements(id: String, clas: String): List<Element> {
        val element: Element = doc.getElementById(id) ?: return emptyList()
        return element.getElementsByClass(clas)
    }

    //TODO: generify these functions further
    fun images(id: String, clas: String? = null): List<String> {
        val element: Element = doc.getElementById(id) ?: return emptyList()
        val listOfImages = mutableListOf<String>()
        if(clas != null){
            for(el in element.getElementsByClass(clas)) {
                listOfImages.add(el.attr("src"))
            }
        }
        return listOfImages
    }

}

suspend fun parser(website: String,  block: suspend HtmlParser.() -> Unit): Unit =
    HtmlParser(website).block()