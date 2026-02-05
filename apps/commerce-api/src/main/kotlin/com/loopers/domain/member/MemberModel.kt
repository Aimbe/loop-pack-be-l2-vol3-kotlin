package com.loopers.domain.member

import com.loopers.domain.BaseEntity
import com.loopers.domain.member.vo.BirthDate
import com.loopers.domain.member.vo.Email
import com.loopers.domain.member.vo.LoginId
import com.loopers.domain.member.vo.Name
import com.loopers.domain.member.vo.Password
import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "members")
class MemberModel(
    loginId: LoginId,
    password: Password,
    name: Name,
    birthDate: BirthDate,
    email: Email,
) : BaseEntity() {

    @Embedded
    val loginId: LoginId = loginId

    @Embedded
    var password: Password = password
        protected set

    @Embedded
    val name: Name = name

    @Embedded
    val birthDate: BirthDate = birthDate

    @Embedded
    val email: Email = email

    /**
     * 비밀번호를 변경합니다.
     * @param currentRawPassword 현재 평문 비밀번호 (검증용)
     * @param newRawPassword 새 평문 비밀번호
     * @throws CoreException AUTHENTICATION_FAILED if current password is wrong
     * @throws CoreException SAME_PASSWORD_NOT_ALLOWED if new password is same as current
     * @throws CoreException PASSWORD_CONTAINS_BIRTHDATE if new password contains birth date
     */
    fun changePassword(currentRawPassword: String, newRawPassword: String) {
        if (!authenticate(currentRawPassword)) {
            throw CoreException(ErrorType.AUTHENTICATION_FAILED, "현재 비밀번호가 일치하지 않습니다.")
        }

        if (currentRawPassword == newRawPassword) {
            throw CoreException(ErrorType.SAME_PASSWORD_NOT_ALLOWED)
        }

        this.password = Password.of(newRawPassword, birthDate.value)
    }

    /**
     * 비밀번호로 인증합니다.
     * @param rawPassword 평문 비밀번호
     * @return 인증 성공 여부
     */
    fun authenticate(rawPassword: String): Boolean {
        return password.matches(rawPassword)
    }
}
