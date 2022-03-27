package com.anymind.btc.services

import com.anymind.btc.exception.BTCException
import com.anymind.btc.model.BTCRecord
import com.anymind.btc.repository.BTCRecordRepository
import com.anymind.btc.resources.dto.BTCRecordDTO
import com.anymind.btc.services.DateHelper.Companion.addOneDay
import com.anymind.btc.services.DateHelper.Companion.clearDate
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Class implements interface BTCRecordService
 */
@Service
@Transactional
class BTCRecordServiceBean(private val btcRecordRepository: BTCRecordRepository): BTCRecordService {

    override suspend fun findAll(startDatetime: Date, endDatetime: Date): List<BTCRecordDTO> {
        log.info("Finding btc records from {} to {}", startDatetime, endDatetime)

        if (startDatetime.after(endDatetime)) {
            log.error("Start date {} must be less then end date {}", startDatetime, endDatetime)
            throw BTCException("Start date must be less then end date")
        }

        var foundRecords = btcRecordRepository.findAllByDatetimeBetweenOrderByDatetime(startDatetime, endDatetime)
            .groupingBy { clearDate(it.datetime!!) }
            .fold(0.0) { total, it -> total + it.amount!! }
            .map { BTCRecordDTO(addOneDay(it.key), it.value) }

        val previousTotalAmount = btcRecordRepository.getAmountOfPreviousRecords(startDatetime) ?: 0.0
        foundRecords = summarizeAmountInRecords(foundRecords, previousTotalAmount)
        log.info("Found {} btc records from {} to {}", foundRecords.size, startDatetime, endDatetime)
        return foundRecords
    }

    override suspend fun save(btcRecordDto: BTCRecordDTO): BTCRecord {
        log.info("Saving btc record datetime={}, amount={}", btcRecordDto.datetime, btcRecordDto.amount)

        if (btcRecordDto.amount.compareTo(0) <= 0) {
            log.error("Amount {} must be positive", btcRecordDto.amount)
            throw BTCException("Amount must be positive")
        }

        val record = btcRecordRepository.save(BTCRecord(btcRecordDto.datetime, btcRecordDto.amount))
        log.info("Saved btc record datetime={}, amount={}", record.datetime, record.amount)
        return record
    }

    /**
     * Method summarizes amount of previous dtc records in order to show current balance in every datetime
     */
    private fun summarizeAmountInRecords(records: List<BTCRecordDTO>, previousTotalAmount: Double) : List<BTCRecordDTO> {
        val result: MutableList<BTCRecordDTO> = mutableListOf()
        val iterator = records.iterator()
        var previousElement: BTCRecordDTO? = null

        while (iterator.hasNext()) {
            if (previousElement == null) {
                previousElement = BTCRecordDTO(iterator.next(), previousTotalAmount)
                result.add(previousElement)
            } else {
                val currentElement = BTCRecordDTO(iterator.next(), previousElement.amount)
                previousElement = currentElement
                result.add(currentElement)
            }
        }
        return result
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(BTCRecordServiceBean::class.java)
    }
}