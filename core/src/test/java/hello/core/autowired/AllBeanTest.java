package hello.core.autowired;

import hello.core.AutoAppConfig;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Map;

public class AllBeanTest {

    @Test
    void findAllBean(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class,DiscountService.class);
        DiscountService discountService = ac.getBean(DiscountService.class);
        Member member = new Member(1L, "chan", Grade.VIP);
        int discountPrice = discountService.discount(member,1000,"fixDiscountPolicy");
    }
    @RequiredArgsConstructor
    static class DiscountService{
        private final Map<String, DiscountPolicy> policyMap;
        private final List<DiscountPolicy> policies;

//        public DiscountService(Map<String, DiscountPolicy> policyMap, List<DiscountPolicy> policies) {
//            this.policyMap = policyMap;
//            this.policies = policies;
//        }
        public int discount(Member member, int price, String discountCode){

            DiscountPolicy discountPolicy = policyMap.get(discountCode);

            return discountPolicy.discount(member,price);

        }
    }
}
