package com.anymind.btc.model

import java.util.*
import javax.persistence.*

/**
 * Entity class provides work with btc records and stored them in database
 */
@Entity
@Table(name = "btc_record")
class BTCRecord(
    @Column(name = "date_time", nullable = false) var datetime: Date? = null,
    @Column(name = "amount", nullable = false) var amount: Double? = 0.0
) {
    @SequenceGenerator(name = "ID", sequenceName = "id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID")
    @Id var id: Long = 0
}