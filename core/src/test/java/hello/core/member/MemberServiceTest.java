package hello.core.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class MemberServiceTest {

    MemberService memberservice = new MemberServiceImpl();

    @Test
    void join() {
        //given
        Member member = new Member(1L, "yunha", Grade.VIP);
        //when
        memberservice.join(member);
        Member findMember = memberservice.findMember(1L);
        //then
        Assertions.assertThat(member).isEqualTo(findMember);
    }
}
