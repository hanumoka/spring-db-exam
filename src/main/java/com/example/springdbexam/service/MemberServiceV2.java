package com.example.springdbexam.service;

import com.example.springdbexam.domain.Member;
import com.example.springdbexam.repository.MemberRepositoryV1;
import com.example.springdbexam.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 파라미터 연동, 풀을 고려한 종료
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {

    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Connection con = dataSource.getConnection();

        try {
            con.setAutoCommit(false); // 트랜젝션 시작
            bizLogin(con, fromId, toId, money);
            con.commit(); // 성공시 커밋
        } catch (Exception e) {
            con.rollback();  // 실패시 롤벡
            throw new IllegalStateException(e);
        } finally {
            release(con);
        } // finally
    }

    private void bizLogin(Connection con, String fromId, String toId, int money) throws SQLException {
        // 비지니스 로직 시작
        Member fromMember = memberRepository.findById(con, fromId);
        Member toMember = memberRepository.findById(con, toId);
        memberRepository.update(con, fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(con, toId, toMember.getMoney() + money);
    }

    // 트랜젝션 테스트를 위해서 일부러 예외를 발생시키는 벨리데이션
    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException();
        }
    }

    private void release(Connection con) {
        if (con != null) {
            try {
                con.setAutoCommit(true);  // 주의!!, 커넥션을 반환시 다시 오토커밋을 활성화 해야 한다.
                con.close();
            } catch (Exception e) {
                log.info("error", e);
            }
        } //if
    }
}
