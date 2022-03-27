package com.anymind.btc.resources.dto

import java.math.RoundingMode
import java.util.*

/**
 * DTO class represents btc record
 */
data class BTCRecordDTO(
    val datetime: Date,
    val amount: Double
) {
    constructor(record: BTCRecordDTO, previousTotalAmount: Double) :
            this(record.datetime, roundAmount(record.amount + previousTotalAmount))

    companion object {
        private fun roundAmount(amount: Double) =
            amount.toBigDecimal().setScale(1, RoundingMode.HALF_UP).toDouble()
    }
}