package com.anymind.btc

import com.anymind.btc.model.BTCRecord
import com.anymind.btc.repository.BTCRecordRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import java.text.SimpleDateFormat

@DataJpaTest
class BTCRecordRepositoryTest @Autowired constructor(
    val entityManager: TestEntityManager,
    val btcRecordRepository: BTCRecordRepository) {

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")

    @Test
    fun `test of finding records between two dates`() {
        entityManager.persist(BTCRecord(dateFormatter.parse("2015-10-05T14:30:00+00:00"), 100.0))
        entityManager.persist(BTCRecord(dateFormatter.parse("2015-10-05T13:55:00+00:00"), 10.0))
        entityManager.persist(BTCRecord(dateFormatter.parse("2015-10-05T16:10:00+00:00"), 1.1))
        entityManager.flush()

        val foundRecords = btcRecordRepository.findAllByDatetimeBetweenOrderByDatetime(
            dateFormatter.parse("2015-10-05T14:00:00+00:00"),
            dateFormatter.parse("2015-10-05T16:00:00+00:00")
        )

        assertEquals(1, foundRecords.size)
    }

    @Test
    fun `test of finding amount before start date`() {
        entityManager.persist(BTCRecord(dateFormatter.parse("2014-03-20T06:55:00+00:00"), 10.0))
        entityManager.persist(BTCRecord(dateFormatter.parse("2014-03-21T10:35:00+00:00"), 100.0))
        entityManager.persist(BTCRecord(dateFormatter.parse("2014-03-21T10:36:00+00:00"), 1000.0))
        entityManager.flush()

        val amountOfPreviousRecords =
            btcRecordRepository.getAmountOfPreviousRecords(dateFormatter.parse("2014-03-21T10:35:55+00:00"))

        assertEquals(110.0, amountOfPreviousRecords)
    }
}