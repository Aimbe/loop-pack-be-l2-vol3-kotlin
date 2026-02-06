package com.loopers.domain.member

import com.loopers.domain.member.vo.BirthDate
import com.loopers.domain.member.vo.Email
import com.loopers.domain.member.vo.LoginId
import com.loopers.domain.member.vo.Name
import com.loopers.domain.member.vo.Password
import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * 회원 등록 담당 서비스
 */
@Component
class MemberRegister(
    private val memberRepository: MemberRepository,
) {

    /**
     * 새 회원을 등록합니다.
     * @throws CoreException DUPLICATE_LOGIN_ID if loginId already exists
     */
    @Transactional
    fun register(
        loginId: LoginId,
        rawPassword: String,
        name: Name,
        birthDate: BirthDate,
        email: Email,
    ): Member {
        if (memberRepository.existsByLoginId(loginId)) {
            throw CoreException(ErrorType.DUPLICATE_LOGIN_ID)
        }

        val password = Password.of(rawPassword, birthDate.value)

        val member = Member(
            loginId = loginId,
            password = password,
            name = name,
            birthDate = birthDate,
            email = email,
        )

        return memberRepository.save(member)
    }
}
