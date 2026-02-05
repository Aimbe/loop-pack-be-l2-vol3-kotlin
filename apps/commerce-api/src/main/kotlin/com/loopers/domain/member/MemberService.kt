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

@Component
class MemberService(
    private val memberRepository: MemberRepository,
) {

    /**
     * 새 회원을 등록합니다.
     * @throws CoreException DUPLICATE_LOGIN_ID if loginId already exists
     */
    @Transactional
    fun register(
        loginId: LoginId,
        password: Password,
        name: Name,
        birthDate: BirthDate,
        email: Email,
    ): MemberModel {
        if (memberRepository.existsByLoginId(loginId)) {
            throw CoreException(ErrorType.DUPLICATE_LOGIN_ID)
        }

        val member = MemberModel(
            loginId = loginId,
            password = password,
            name = name,
            birthDate = birthDate,
            email = email,
        )

        return memberRepository.save(member)
    }

    /**
     * 로그인ID로 회원을 조회합니다.
     * @throws CoreException MEMBER_NOT_FOUND if member doesn't exist
     */
    @Transactional(readOnly = true)
    fun getMemberByLoginId(loginId: LoginId): MemberModel {
        return memberRepository.findByLoginId(loginId)
            ?: throw CoreException(ErrorType.MEMBER_NOT_FOUND)
    }

    /**
     * 로그인ID와 비밀번호로 인증합니다.
     * @throws CoreException MEMBER_NOT_FOUND if member doesn't exist
     * @throws CoreException AUTHENTICATION_FAILED if password doesn't match
     */
    @Transactional(readOnly = true)
    fun authenticate(loginId: LoginId, rawPassword: String): MemberModel {
        val member = getMemberByLoginId(loginId)

        if (!member.authenticate(rawPassword)) {
            throw CoreException(ErrorType.AUTHENTICATION_FAILED)
        }

        return member
    }

    /**
     * 비밀번호를 변경합니다.
     * @throws CoreException MEMBER_NOT_FOUND if member doesn't exist
     * @throws CoreException AUTHENTICATION_FAILED if current password doesn't match
     * @throws CoreException SAME_PASSWORD_NOT_ALLOWED if new password is same as current
     * @throws CoreException PASSWORD_CONTAINS_BIRTHDATE if new password contains birthdate
     */
    @Transactional
    fun changePassword(
        loginId: LoginId,
        currentRawPassword: String,
        newRawPassword: String,
    ) {
        val member = getMemberByLoginId(loginId)
        member.changePassword(currentRawPassword, newRawPassword)
    }
}
