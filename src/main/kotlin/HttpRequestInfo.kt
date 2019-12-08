import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

const val apiKey : String = "1651861654"
const val apiPass :  String = "53a5db25-1849-11ea-9f40-00505680c1b4"

const val chartType : String = "SID"

fun getHttpRequest(url: URL, xmlFileName : String){
    with(url.openConnection() as HttpURLConnection) {
        requestMethod = "GET"  // optional default is GET

        println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")

        inputStream.bufferedReader().use {
            File("$xmlFileName.xml").bufferedWriter().use { out ->
                it.lines().forEach { line ->
                    out.write(line + "\n")
                }
            }
        }
    }
}

fun getSIDChartInfo(icao: String){
    val url = URL("http://www.aisweb.aer.mil.br/api/?" +
            "apiKey=$apiKey" +
            "&apiPass=$apiPass" +
            "&area=cartas" +
            "&IcaoCode=$icao" +
            "&tipo=$chartType")

    getHttpRequest(url, "sidInfoFile")
}

fun getNotamInfo(icao : String){
    val url = URL("http://www.aisweb.aer.mil.br/api/?" +
            "apiKey=$apiKey" +
            "&apiPass=$apiPass" +
            "&area=notam" +
            "&IcaoCode=$icao" +
            "&dist=N")

    getHttpRequest(url, "notamInfoFile")
}


fun readSidInfoRead(icao: String, runwayID : String){
    getSIDChartInfo(icao)
    val xlmFile: File = File("sidInfoFile.xml")
    val xmlDoc: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xlmFile)

    xmlDoc.documentElement.normalize()

    println("Root Node:" + xmlDoc.documentElement.nodeName + "\n")

    val sidsList: NodeList = xmlDoc.getElementsByTagName("item")

    for(i in 0 until sidsList.length) {
        var itemNode: Node = sidsList.item(i)

        if (itemNode.getNodeType().equals(Node.ELEMENT_NODE)) {
            val elem = itemNode as Element
            val mMap = mutableMapOf<String, String>()

            for(j in 0 until elem.attributes.length) {
                mMap.putIfAbsent(elem.attributes.item(j).nodeName, elem.attributes.item(j).nodeValue)
            }
            if(elem.getElementsByTagName("nome").item(0).textContent.contains(runwayID)){
                println("Chart identifier : ${itemNode.nodeName} - $mMap")

                println("ICAO: ${elem.getElementsByTagName("IcaoCode").item(0).textContent}")
                println("Description: ${elem.getElementsByTagName("tipo_descr").item(0).textContent}")
                println("Date: ${elem.getElementsByTagName("dt").item(0).textContent}")
                println("Link to download the SID: ${elem.getElementsByTagName("link").item(0).textContent}")
                println("Link to download the SID procedures: ${elem.getElementsByTagName("tabcode").item(0).textContent}")
                println("WayPoints: ${elem.getElementsByTagName("nome").item(0).textContent}")
                println("---------------")
            }
        }
    }
}

fun readNotamInfoFile(icao : String){
    getNotamInfo(icao)
    val xlmFile: File = File("notamInfoFile.xml")
    val xmlDoc: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xlmFile)

    xmlDoc.documentElement.normalize()

    println("Root Node:" + xmlDoc.documentElement.nodeName + "\n")

    val notamList: NodeList = xmlDoc.getElementsByTagName("notam")

    for(i in 0 until notamList.length) {
        var itemNode: Node = notamList.item(i)

        if (itemNode.getNodeType().equals(Node.ELEMENT_NODE)) {
            val elem = itemNode as Element
            val mMap = mutableMapOf<String, String>()

            for(j in 0 until elem.attributes.length) {
                mMap.putIfAbsent(elem.attributes.item(j).nodeName, elem.attributes.item(j).nodeValue)
            }
            println("Chart identifier : ${itemNode.nodeName} - $mMap")

            println("Database NotamID: ${elem.getElementsByTagName("id").item(0).textContent}")
            println("Notam Code: ${elem.getElementsByTagName("cod").item(0).textContent}")
            println("Status (N - novo, S - Substituidor, C - Cancelado): ${elem.getElementsByTagName("status").item(0).textContent}")
            println("Start/ End Date: ${elem.getElementsByTagName("dt").item(0).textContent}")
            println("Airport ICAO: ${elem.getElementsByTagName("loc").item(0).textContent}")
            println("City: ${elem.getElementsByTagName("cidade").item(0).textContent}")
            println("Notam Info Description: ${elem.getElementsByTagName("e").item(0).textContent}")
            println("---------------")
        }
    }

}