package ma.othmane.tgv




/**
 *
 */
fun chartDataByCanal(canalNumber:Int,canals:List<Canal>,canalByFilesPoints: MutableMap<Canal, List<Pair<MeasurementData, Point?>>>):ChartData{

    var canal = canals.get(if(canalNumber>=955) canalNumber-830 else canalNumber)
    var listMeasurePair = canalByFilesPoints.get(canal)


    var labels = listMeasurePair?.map { (it, _) -> " \" (${it.longitude},${it.latitude}) \" ".replace(" ", "") }
    var signals = listMeasurePair?.map { (_, it) -> it?.signalLevel }

    return ChartData(canalNumber,labels,signals)

}

/**
 *
 */
fun chartToDataSet(chartData:ChartData):String="""
    {
            name: 'ARFCN ${if(chartData.canalNumber>124) chartData.canalNumber+830 else chartData.canalNumber}',
            data: ${chartData.values},

        }
""".trimIndent()

/**
 *
 */
fun chartsToDataSets(chartsData:List<ChartData>)=chartsData.joinToString(separator = ","){ chartToDataSet(it)}


/**
 * this function retunr a Javascript code for HIghcharts
 */
fun chart(labels:List<String>?,chartsData:List<ChartData>)="""
    Highcharts.chart('container', {
    chart: {
        type: 'line',
        zoomType:'x'
    },
    title: {
        text: 'Signal Level By Canal'
    },
    subtitle: {
        text: 'Gathering Data'
    },
    xAxis: {
        categories: ${labels}
    },
    yAxis: {
        title: {
            text: 'Signal Level'
        }
    },
    plotOptions: {
        line: {
            dataLabels: {
                enabled: true
            },
            enableMouseTracking: false
        }
    },
    series: [${chartsToDataSets(chartsData)}]
});
"""