package ma.othmane.tgv

import java.io.File
import java.math.BigDecimal



/**
 * this function parses all file in a directory and return a map of
 * having a key the file and the point where the data was calculated and the list of Points corresponding to it
 *
 */
fun initData(fileName: String): MutableMap<MeasurementData, List<Point>> {
    var map: MutableMap<MeasurementData, List<Point>> = HashMap();
    var dataFiles: MutableList<File> = ArrayList<File>()

    File(fileName).walk().forEach {
        if (it.isFile) dataFiles.add(it)
    }

    dataFiles.forEach {
        var pair = it.extractAll()
        if (!"".equals(pair.first.latitude) || !"".equals(pair.first.longitude))
            map.put(pair.first, pair.second)
    }

    return map;
}

/*
* initiliaze radio canals
 */
fun initCanals(): List<Canal> {
    var canals: MutableList<Canal> = ArrayList();
    var startFrequency = BigDecimal.valueOf(934.90)
    var step = BigDecimal.valueOf(0.2)

    for (n in 0..124) {
        canals.add(Canal("ARFCN${n}", startFrequency, startFrequency + step))
        startFrequency += step;
    }

    startFrequency = BigDecimal.valueOf(921.1)
    for (n in 955..1023) {
        canals.add(Canal("ARFCN${n}", startFrequency, startFrequency + step))
        startFrequency += step;
    }

    return canals
}

/**
 * extracts a data from a data file as a pair of  <MeasurementData, List<Point>> {
 */
fun File.extractAll(): Pair<MeasurementData, List<Point>> {
    var list: MutableList<Point> = ArrayList();

    val prefix = "P_"
    var gpsFixTime: String = ""
    var longitude: String = ""
    var latitude: String = ""
    var time: String = ""

    this.forEachLine {
        if (it.startsWith(prefix)) list.add(it.extractPoint(this.name))
        if ("".equals(gpsFixTime)) gpsFixTime = it.extractValue(LABELS.GPS_FIX_TIME);
        if ("".equals(longitude)) longitude = it.extractValue(LABELS.GPS_FIX_LONGITUDE)
        if ("".equals(latitude)) latitude = it.extractValue(LABELS.GPS_FIX_LATITUDE);
        if ("".equals(time)) time = it.extractValue(LABELS.GPS_FIX_VALUE_TIME);
    }
    return Pair(MeasurementData(this.name, gpsFixTime, longitude, latitude, time), list);


}

/**
 * extracts a Point
 */
fun String.extractPoint(fileId: String): Point {
    var name = this.substringBefore("=");
    var signalLevel = this.substringAfter("=").substringBefore(" ").toDouble();
    var frequency = this.substringAfter(" ,").substringBefore(" MHz").toDouble();

    return Point(fileId, name, signalLevel, frequency)
}

fun String.extractValue(measure: String): String {
    var value: String = ""
    if (this.startsWith(measure)) value = this.substringAfter("${measure}=");
    return value;
}


fun String.toGPSDouble(): Double {
    val els = this.split(" ")
    if (els.size > 3) {
        var dd = Math.signum(els.get(1).toDouble()) * (Math.abs(els.get(1).toDouble()) + (els.get(2).toDouble() / 60.0) + (els.get(3).toDouble() / 3600.0));
        return dd
    }
    return 0.00
}

fun Pair<MeasurementData, MeasurementData>.distance(): Double {

    with(this) {
        return distance(first.latitude.toGPSDouble(),
                first.longitude.toGPSDouble(),
                second.latitude.toGPSDouble(),
                second.longitude.toGPSDouble(),
                "K"

        )
    }

}
