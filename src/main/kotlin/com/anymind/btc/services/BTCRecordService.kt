package com.anymind.btc.services

import com.anymind.btc.model.BTCRecord
import com.anymind.btc.resources.dto.BTCRecordDTO
import java.util.*

interface BTCRecordService {
    suspend fun findAll(startDatetime: Date, endDatetime: Date): List<BTCRecordDTO>
    suspend fun save(btcRecordDto: BTCRecordDTO): BTCRecord
}