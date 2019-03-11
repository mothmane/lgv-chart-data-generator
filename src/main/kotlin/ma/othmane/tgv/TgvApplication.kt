package ma.othmane.tgv

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.io.File
import java.math.BigDecimal
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.collections.ArrayList
import kotlin.streams.toList

@SpringBootApplication
class TgvApplication {
    var canals = initCanals()

    var canalByFilesPoints: MutableMap<Canal, List<Pair<MeasurementData, Point?>>> = HashMap()
    var listOfMesurement: List<MeasurementData>? = ArrayList();
    var listOfPairMeasurement:List<Pair<MeasurementData,MeasurementData>> = ArrayList()

    @Bean
    fun cammandLineRunner(conf: LGVConfiguration) = CommandLineRunner {
        var mapMeasurePoints = initData(conf.dataFolder)



        canals.forEach {
            var canal = it
            var listMeasureMaxPoint = mapMeasurePoints.map { it.key to it.value.filter { canal.between(it.frequency) }.maxBy { it.signalLevel } }.sortedBy { it.first.fileId }
            listOfPairMeasurement=listMeasureMaxPoint.windowed(2,1).map { Pair(it.first().first,it.last().first) }

            listOfMesurement = listMeasureMaxPoint.map { it.first }
            //var distances=listOfPairs.map {
            //        distance(it.first.first.latitude.toDouble(),it.first.first.longitude.toDouble(),it.second.first.latitude.toDouble(),it.second.first.longitude.toDouble(),"K") }
            canalByFilesPoints.putIfAbsent(canal, listMeasureMaxPoint)
        }
        var pk:Double=conf.p0.toDouble();

        var measureStart:MeasurementData= listOfMesurement?.get(0)!!

        var listDistances=listOfPairMeasurement?.map{
             pk=(Pair(it.first,it.second)).distance()+pk
            "%.2f".format(Locale.ENGLISH,pk)
        }



        println(listDistances)
        var entryCanals: List<String> = conf.canal.split(",").map {
            it.trim()
            var value:Int=it.toInt();
            if(value>=955) "${value-830}" else "${value}"
         }

        var charts = entryCanals.map { chartDataByCanal(it.toInt(), canals, canalByFilesPoints) }


        File(conf.destinationFolder + conf.js).writeText(chart(listDistances,charts))



    }
}

fun MutableMap<Canal, MutableMap<MeasurementData, MutableList<Point>>>.putPointList(canal: Canal, mesure: MeasurementData, list: MutableList<Point>) {
    if (this.get(canal) != null) {
        var mp = this.get(canal)
        mp?.put(
                mesure,
                list
        )
    } else {
        this.put(canal, HashMap<MeasurementData, MutableList<Point>>())
        this.putPointList(canal, mesure, list)
    }

}


fun Canal.between(value: Double): Boolean {
    if ((value.compareTo(this.start.toDouble()) >= 0) && (value.compareTo(this.stop.toDouble()) <= 0)) {
        return true
    }
    return false;
}


fun main(args: Array<String>) {
    runApplication<TgvApplication>(*args)
}


