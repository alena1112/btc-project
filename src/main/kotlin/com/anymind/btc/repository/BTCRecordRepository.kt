package com.anymind.btc.repository

import com.anymind.btc.model.BTCRecord
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Repository class allows to CRUD operations with btc records
 */
@Repository
interface BTCRecordRepository : JpaRepository<BTCRecord, Long> {

    /**
     * Method returns btc records from specified date range
     */
    fun findAllByDatetimeBetweenOrderByDatetime(fromDate: Date, toDate: Date): List<BTCRecord>

    /**
     * Method returns total amount from btc records which have datetime less than specified date
     */
    @Query("select sum(r.amount) from BTCRecord r where r.datetime < :fromDate")
    fun getAmountOfPreviousRecords(@Param("fromDate") fromDate: Date): Double?
}