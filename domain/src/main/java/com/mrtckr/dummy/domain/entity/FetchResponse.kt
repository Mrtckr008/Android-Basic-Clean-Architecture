package com.mrtckr.dummy.domain.entity

data class FetchResponse(
    val people: List<Person>,
    val next: String?
)
