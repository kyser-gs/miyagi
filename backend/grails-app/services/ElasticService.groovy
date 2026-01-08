package backend

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import java.text.SimpleDateFormat

class ElasticService {
    String serverUrl = "https://23347470c4754472a885f58a3ac4bc0f.us-central1.gcp.cloud.es.io"
    String apiKey = "VDYtSVc1c0Jhb2pBUktWNksxdm06MDZsdm9jak9MMkdNRHFaYzR2YlVuQQ=="

    def indexUser(User user) {
        def url = new URL("${serverUrl}/users/_doc/${user.id}")
        def connection = url.openConnection() as HttpURLConnection

        connection.setRequestMethod("PUT")
        connection.setRequestProperty("Authorization", "ApiKey ${apiKey}")
        connection.setRequestProperty("Content-Type", "application/json")
        connection.setDoOutput(true)

        def sdf = new SimpleDateFormat("yyyy-MM-dd")

        def json = new JsonBuilder([
                firstName: user.firstName,
                lastName: user.lastName,
                fullName: "${user.firstName} ${user.lastName}",
                dateOfBirth: sdf.format(user.dateOfBirth),
                address: [
                        street: user.street,
                        apt: user.apt,
                        city: user.city,
                        state: user.state,
                        zip: user.zipCode,
                        fullAddress: "${user.street} ${user.apt ?: ''} ${user.city} ${user.state} ${user.zipCode}"
                ]
        ]).toString()


        connection.outputStream.write(json.bytes)

        def responseCode = connection.responseCode
        if (responseCode == 200 || responseCode == 201) {
            return new JsonSlurper().parseText(connection.inputStream.text)
        } else {
            def errorResponse = connection.errorStream?.text ?: "No error details"
            throw new RuntimeException("Elasticsearch indexing failed: ${responseCode} - ${errorResponse}")
        }
    }

    def searchUsers(String name = null, Date startDate = null, Date endDate = null, int page = 0, int size = 10){
        def url = new URL("${serverUrl}/users/_search")
        def connection = url.openConnection() as HttpURLConnection

        connection.setRequestMethod("POST")
        connection.setRequestProperty("Authorization", "ApiKey ${apiKey}")
        connection.setRequestProperty("Content-Type", "application/json")
        connection.setDoOutput(true)

        def sdf = new SimpleDateFormat("yyyy-MM-dd")

        def mustClauses = []

        if (name) {
            mustClauses << [
                multi_match: [
                    query: name,
                    fields: ["firstName", "lastName", "fullName"]
                ]
            ]
        }

        if (startDate || endDate) {
            def rangeQuery = [:]
            if (startDate) rangeQuery.gte = sdf.format(startDate)
            if (endDate) rangeQuery.lte = sdf.format(endDate)

            mustClauses << [
                range: [
                    dateOfBirth: rangeQuery
                ]
            ]
        }

        def queryMap = [
            from: page * size,
            size: size
        ]

        if (mustClauses) {
            queryMap.query = [
                bool: [
                    must: mustClauses
                ]
            ]
        } else {
            queryMap.query = [match_all: [:]]
        }

        def json = new JsonBuilder(queryMap).toString()
        connection.outputStream.write(json.bytes)

        def responseCode = connection.responseCode
        if (responseCode == 200) {
            return new JsonSlurper().parseText(connection.inputStream.text)
        } else {
            def errorResponse = connection.errorStream?.text ?: "No error details"
            log.error("Elasticsearch search failed. Status: ${responseCode}, Error: ${errorResponse}")
            throw new RuntimeException("Elasticsearch search failed: ${responseCode} - ${errorResponse}")
        }
    }
}