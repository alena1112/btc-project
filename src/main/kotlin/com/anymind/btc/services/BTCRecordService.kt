package com.anymind.btc.services

import com.anymind.btc.model.BTCRecord
import com.anymind.btc.resources.dto.BTCRecordDTO
import java.util.*

/**
 * Interface contains base business logic to work with btc records
 */
interface BTCRecordService {
    /**
     * Method returns list of btc records in specified date range. Btc records are grouped by each hour
     * of each day of year. Amounts are aggregated in every group of records
     */
    suspend fun findAll(startDatetime: Date, endDatetime: Date): List<BTCRecordDTO>

    /**
     * Method provides stored btc record in Database. Btc record datetime timezone convert to application timezone
     */
    suspend fun save(btcRecordDto: BTCRecordDTO): BTCRecord
}