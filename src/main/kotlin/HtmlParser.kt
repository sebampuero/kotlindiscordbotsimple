package chistosito

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class HtmlParser (website: String) {

    private var doc: Document = Jsoup.connect(website).get()

    fun firstFound(id: String, clas: String? = null): Element? {
        val element: Element = doc.getElementById(id) ?: return null
        if(clas != null){
            if(element.getElementsByClass(clas).first() != null){
                return element.getElementsByClass(clas).first()!!
            }
        }
        return element
    }

    fun elementsByClass(id: String, clas: String): List<Element> {
        val element: Element = doc.getElementById(id) ?: return emptyList()
        return element.getElementsByClass(clas)
    }

    fun attributesInClass(id: String, clas: String, elementName: String, attr: String): List<String> {
        val listOfAttrs = mutableListOf<String>()
        val classElements = doc.getElementById(id)?.getElementsByClass(clas)?.first()
        classElements?.getElementsByTag(elementName)?.forEach {
            listOfAttrs.add(it.attr(attr))
        }
        return listOfAttrs
    }

    fun tableAsText(clas: String): List<List<String>> {
        val table = doc.getElementsByClass(clas).first() ?: return emptyList()
        val listWithTrs = mutableListOf<List<String>>()
        table.getElementsByTag("tr").forEach { tr ->
            val trList = mutableListOf<String>()
            listWithTrs.add(trList)
            tr.getElementsByTag("th").forEach { th ->
                trList.add(th.text())
            }
            tr.getElementsByTag("td").forEach { td ->
                trList.add(td.text())
            }
        }
        return listWithTrs
    }

}

suspend fun parser(website: String,  block: suspend HtmlParser.() -> Unit): Unit =
    HtmlParser(website).block()