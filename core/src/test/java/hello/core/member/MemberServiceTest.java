package hello.core.member;

import hello.core.AppConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MemberServiceTest {

    MemberService memberservice;

    @BeforeEach
    public void beforeEach(){
        AppConfig appConfig=new AppConfig();
        memberservice = appConfig.memberService();
    }

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
