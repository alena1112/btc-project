package com.anymind.btc.repository

import com.anymind.btc.model.BTCRecord
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BTCRecordRepository : JpaRepository<BTCRecord, Long> {
    fun findAllByDatetimeBetweenOrderByDatetime(fromDate: Date, toDate: Date): List<BTCRecord>

    @Query("select sum(r.amount) from BTCRecord r where r.datetime < :fromDate")
    fun getAmountOfPreviousRecords(@Param("fromDate") fromDate: Date): Double?
}