package com.loopers.domain.member.vo

import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Embeddable
data class BirthDate(
    @Column(name = "birth_date", nullable = false)
    val value: LocalDate,
) {
    init {
        if (value.isAfter(LocalDate.now())) {
            throw CoreException(ErrorType.INVALID_BIRTHDATE_FORMAT, "생년월일은 미래 날짜일 수 없습니다.")
        }
    }

    fun toYYYYMMDD(): String {
        return value.format(FORMATTER)
    }

    companion object {
        private val FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd")
    }
}
