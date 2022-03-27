package com.anymind.btc.resources

import com.anymind.btc.resources.dto.BTCRecordDTO
import com.anymind.btc.resources.dto.DateTimeRangeDTO
import com.anymind.btc.services.BTCRecordService
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.*

/**
 * Class provides access to basic API operations
 */
@RestController
@RequestMapping("/btc", produces = [APPLICATION_JSON_VALUE])
class BTCRecordResource(private val btcRecordService: BTCRecordService) {

    /**
     * Method allows saving btc record in Database
     */
    @PostMapping("save")
    suspend fun save(@RequestBody recordDto: BTCRecordDTO): RestResponse {
        btcRecordService.save(recordDto)
        return RestResponse("BTC was saved successfully")
    }

    /**
     * Method allows showing btc record history in specified date range
     */
    @PostMapping("history")
    suspend fun getHistory(@RequestBody range: DateTimeRangeDTO) =
        btcRecordService.findAll(range.startDatetime, range.endDatetime)
}