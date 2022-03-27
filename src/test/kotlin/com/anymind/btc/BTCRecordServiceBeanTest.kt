package com.anymind.btc

import com.anymind.btc.model.BTCRecord
import com.anymind.btc.repository.BTCRecordRepository
import com.anymind.btc.resources.dto.BTCRecordDTO
import com.anymind.btc.exception.BTCException
import com.anymind.btc.services.BTCRecordServiceBean
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.text.SimpleDateFormat

/**
 * Class provides testing business logic of BTCRecordServiceBean class
 */
@ExtendWith(MockitoExtension::class)
internal class BTCRecordServiceBeanTest {
    @Mock
    private lateinit var btcRecordRepository: BTCRecordRepository

    @InjectMocks
    private lateinit var btcRecordServiceBean: BTCRecordServiceBean

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")

    @AfterEach
    fun tearDown() {
        verifyNoMoreInteractions(
            btcRecordRepository
        )
    }

    @Test
    fun `test history when start date more then end date`() {
        val startDatetime = dateFormatter.parse("2019-10-05T19:00:00+00:00")
        val endDatetime = dateFormatter.parse("2019-10-05T18:00:00+00:00")

        val btcException = Assertions.assertThrows(BTCException::class.java) {
            runBlocking {
                btcRecordServiceBean.findAll(startDatetime, endDatetime)
            }
        }
        assertEquals("Start date must be less then end date", btcException.message)
    }

    @Test
    fun `test history when DB is empty`() = runBlocking {
        val startDatetime = dateFormatter.parse("2019-10-05T18:00:00+03:00")
        val endDatetime = dateFormatter.parse("2019-10-06T00:05:00+05:00")

        `when`(btcRecordRepository.findAllByDatetimeBetweenOrderByDatetime(startDatetime, endDatetime))
            .thenReturn(listOf())
        `when`(btcRecordRepository.getAmountOfPreviousRecords(startDatetime)).thenReturn(null)

        val actualRecords = btcRecordServiceBean.findAll(startDatetime, endDatetime)
        assertTrue(actualRecords.isEmpty())
    }

    @Test
    fun `test history when records from different time zones`() = runBlocking {
        val startDatetime = dateFormatter.parse("2019-10-05T18:00:00+03:00")
        val endDatetime = dateFormatter.parse("2019-10-06T00:05:00+05:00")

        `when`(btcRecordRepository.findAllByDatetimeBetweenOrderByDatetime(startDatetime, endDatetime)).thenReturn(
            listOf(
                BTCRecord(dateFormatter.parse("2019-10-05T14:45:05+01:00"), 10.0),
                BTCRecord(dateFormatter.parse("2019-10-05T16:05:01+01:00"), 100.1),
                BTCRecord(dateFormatter.parse("2019-10-05T20:05:01+03:00"), 55.0),
                BTCRecord(dateFormatter.parse("2019-10-05T21:55:01+03:00"), 1000.0),
            )
        )
        `when`(btcRecordRepository.getAmountOfPreviousRecords(startDatetime)).thenReturn(99.0)

        val actualRecords = btcRecordServiceBean.findAll(startDatetime, endDatetime)
        assertEquals(4, actualRecords.size)
        assertEquals(BTCRecordDTO(dateFormatter.parse("2019-10-05T14:00:00+00:00"), 109.0), actualRecords[0])
        assertEquals(BTCRecordDTO(dateFormatter.parse("2019-10-05T16:00:00+00:00"), 209.1), actualRecords[1])
        assertEquals(BTCRecordDTO(dateFormatter.parse("2019-10-05T18:00:00+00:00"), 264.1), actualRecords[2])
        assertEquals(BTCRecordDTO(dateFormatter.parse("2019-10-05T19:00:00+00:00"), 1264.1), actualRecords[3])
    }

    @Test
    fun `test history when date range includes several days`() = runBlocking {
        val startDatetime = dateFormatter.parse("2019-10-01T00:05:00+00:00")
        val endDatetime = dateFormatter.parse("2020-02-05T18:00:00+00:00")

        `when`(btcRecordRepository.findAllByDatetimeBetweenOrderByDatetime(startDatetime, endDatetime)).thenReturn(
            listOf(
                BTCRecord(dateFormatter.parse("2019-10-10T15:15:00+00:00"), 1000.0),
                BTCRecord(dateFormatter.parse("2019-10-10T15:30:00+00:00"), 5.0),
                BTCRecord(dateFormatter.parse("2019-10-10T15:55:00+00:00"), 20.7),
                BTCRecord(dateFormatter.parse("2019-11-10T15:18:00+00:00"), 55.1),
                BTCRecord(dateFormatter.parse("2019-11-10T15:59:59+00:00"), 44.0),
                BTCRecord(dateFormatter.parse("2020-01-10T15:44:59+00:00"), 101.0),
                BTCRecord(dateFormatter.parse("2020-02-04T18:05:00+00:00"), 1.0),
            )
        )
        `when`(btcRecordRepository.getAmountOfPreviousRecords(startDatetime)).thenReturn(10000.0)

        val actualRecords = btcRecordServiceBean.findAll(startDatetime, endDatetime)
        assertEquals(4, actualRecords.size)
        assertEquals(BTCRecordDTO(dateFormatter.parse("2019-10-10T16:00:00+00:00"), 11025.7), actualRecords[0])
        assertEquals(BTCRecordDTO(dateFormatter.parse("2019-11-10T16:00:00+00:00"), 11124.8), actualRecords[1])
        assertEquals(BTCRecordDTO(dateFormatter.parse("2020-01-10T16:00:00+00:00"), 11225.8), actualRecords[2])
        assertEquals(BTCRecordDTO(dateFormatter.parse("2020-02-04T19:00:00+00:00"), 11226.8), actualRecords[3])
    }

    @Test
    fun `test of saving record with negative amount`() {
        val btcRecordDto = BTCRecordDTO(dateFormatter.parse("2019-10-05T14:45:05+01:00"), -10.0)

        val btcException = Assertions.assertThrows(BTCException::class.java) {
            runBlocking { btcRecordServiceBean.save(btcRecordDto) }
        }
        assertEquals("Amount must be positive", btcException.message)
    }

    @Test
    fun `test of saving record`() {
        val btcRecordDto = BTCRecordDTO(dateFormatter.parse("2019-10-05T14:45:05+01:00"), 10.0)
        val expectedResult = BTCRecord(btcRecordDto.datetime, btcRecordDto.amount)

        `when`(btcRecordRepository.save(any())).thenReturn(expectedResult)

        val actualResult = runBlocking { btcRecordServiceBean.save(btcRecordDto) }
        assertEquals(expectedResult, actualResult)
    }
}