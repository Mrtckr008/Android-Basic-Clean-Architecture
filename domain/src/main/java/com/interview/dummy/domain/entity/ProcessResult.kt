package com.interview.dummy.domain.entity

data class ProcessResult(
    val fetchResponse: FetchResponse?,
    val fetchError: FetchError?,
    val waitTime: Double
)
