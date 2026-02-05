package com.loopers.domain.member

import com.loopers.domain.member.vo.LoginId

interface MemberRepository {
    fun save(member: MemberModel): MemberModel
    fun findByLoginId(loginId: LoginId): MemberModel?
    fun existsByLoginId(loginId: LoginId): Boolean
}
