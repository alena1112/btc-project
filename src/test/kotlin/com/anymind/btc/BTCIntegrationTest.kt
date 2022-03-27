package com.anymind.btc

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("testing")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BTCIntegrationTest(@Autowired val restTemplate: TestRestTemplate) {
    private lateinit var headers: HttpHeaders

    @BeforeEach
    fun setUp() {
        headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
    }

    @Test
    fun `test of saving btc record`() {
        val request = HttpEntity<String>("{\"datetime\": \"2019-10-05T14:45:05+01:00\", \"amount\": 10}", headers)
        val response = restTemplate.postForEntity("/btc/save", request, String::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).contains("{\"response\":\"BTC was saved successfully\"}")
    }

    @Test
    fun `test of saving btc record with empty request`() {
        val request = HttpEntity<String>(null, headers)
        val response = restTemplate.postForEntity("/btc/save", request, String::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `test of saving btc record with empty date in request`() {
        val request = HttpEntity<String>("{\"datetime\": \"\", \"amount\": 10}", headers)
        val response = restTemplate.postForEntity("/btc/save", request, String::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `test of saving btc record with incorrect date in request`() {
        val request = HttpEntity<String>("{\"datetime\": \"14:45:05T2019-10-05\", \"amount\": 10}", headers)
        val response = restTemplate.postForEntity("/btc/save", request, String::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `test of saving btc record with empty amount in request`() {
        val request = HttpEntity<String>("{\"datetime\": \"2019-10-05T14:45:05+01:00\"}", headers)
        val response = restTemplate.postForEntity("/btc/save", request, String::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).contains("{\"response\":\"Exception: Amount must be positive\"}")
    }

    @Test
    fun `test of saving btc record with negative amount in request`() {
        val request = HttpEntity<String>("{\"datetime\": \"2019-10-05T14:45:05+01:00\"}, \"amount\": -10}", headers)
        val response = restTemplate.postForEntity("/btc/save", request, String::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).contains("{\"response\":\"Exception: Amount must be positive\"}")
    }

    @Test
    fun `test history`() {
        val request = HttpEntity<String>(
            "{\"startDatetime\": \"2019-10-05T18:00:00+03:00\", \"endDatetime\": \"2019-10-06T00:05:00+05:00\"}",
            headers
        )
        val response = restTemplate.postForEntity("/btc/history", request, String::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `test history with empty request body`() {
        val request = HttpEntity<String>(null, headers)
        val response = restTemplate.postForEntity("/btc/history", request, String::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `test history with incorrect date`() {
        val request = HttpEntity<String>("{\"startDatetime\": \"18:00:00+03:00T2019-10-05\"}", headers)
        val response = restTemplate.postForEntity("/btc/history", request, String::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }
}