package ma.othmane.tgv


fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double, unit: String): Double {
    if (lat1 == lat2 && lon1 == lon2) {
        return 0.0
    } else {
        val theta = lon1 - lon2
        var dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta))
        dist = Math.acos(dist)
        dist = Math.toDegrees(dist)
        dist = dist * 60.0 * 1.1515
        if (unit === "K") {
            dist = dist * 1.609344
        } else if (unit === "N") {
            dist = dist * 0.8684
        }
        return dist
    }
}

