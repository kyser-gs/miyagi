package backend

import groovy.json.JsonSlurper

class LocationService {
    def getDistance(LocationRequest request) {
        def query = "destinations=${URLEncoder.encode(request.destinations, 'UTF-8')}" +
                         "&origins=${URLEncoder.encode(request.origins, 'UTF-8')}" +
                         "&units=${request.units}" +
                         "&key=${request.key}"

        def url = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?${query}")
        def connection = url.openConnection() as HttpURLConnection

        def responseCode = connection.getResponseCode()

        if (responseCode == 200) {
            def response = new JsonSlurper().parseText(connection.inputStream.text)

            def distanceValue = response.rows[0].elements[0].distance.text

            return distanceValue
        } else {
            throw new RuntimeException("Google Maps API call failed: ${responseCode}")
        }
    }
}