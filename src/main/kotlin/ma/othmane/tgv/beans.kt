package ma.othmane.tgv

import java.math.BigDecimal

data class Point(val fileId: String, val name: String, val signalLevel: Double, val frequency: Double)

data class MeasurementData(val fileId: String, val gpsFixTime: String, val longitude: String, val latitude: String, val time: String)

data class Canal(val name:String,val start:BigDecimal,val stop:BigDecimal )

data class ChartData(val canalNumber:Int,val labels:List<String>?,val values:List<Double?>?)

object LABELS {
    val GPS_FIX_TIME = "GPS_FIX_TIME"
    val GPS_FIX_LONGITUDE = "GPS_FIX_LONGITUDE"
    val GPS_FIX_LATITUDE = "GPS_FIX_LATITUDE"
    val GPS_FIX_VALUE_TIME = "GPS_FIX_VALUE_TIME"
}


