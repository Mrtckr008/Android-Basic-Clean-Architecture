package com.interview.dummy.domain.entity

data class FetchResponse(
    val people: List<Person>,
    val next: String?
)
