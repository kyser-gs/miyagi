package backend

import java.text.SimpleDateFormat
import grails.converters.JSON
import grails.gorm.transactions.Transactional

class LocationController {
    LocationService locationService;

    def distance() {
        def locationRequest = new LocationRequest(
            destinations: params.destinations,
            origins: params.origins,
            units: params.units,
            key: params.key
        )

        render([distance: locationService.getDistance(locationRequest)] as JSON)
    }
}