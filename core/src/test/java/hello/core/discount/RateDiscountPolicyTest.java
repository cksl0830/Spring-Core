package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RateDiscountPolicyTest {

    RateDiscountPolicy discountPolicy = new RateDiscountPolicy();

    @Test
    @DisplayName("VIP는 10% 할인이 적용")
    void vip_o(){

        Member member= new Member(1L,"chan", Grade.VIP);
        int discount = discountPolicy.discount(member,10000);
        assertThat(discount).isEqualTo(1000);
    }

    @Test
    @DisplayName("VIP아니면 할인 적용 X")
    void vip_x(){
        Member member= new Member(2L,"yunha", Grade.BASIC);
        int discount = discountPolicy.discount(member,10000);
        assertThat(discount).isEqualTo(0);
    }

}