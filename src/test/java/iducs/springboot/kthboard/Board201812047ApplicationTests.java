package iducs.springboot.kthboard;

import iducs.springboot.kthboard.entity.MemberEntity;
import iducs.springboot.kthboard.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;
import java.util.stream.IntStream;

@SpringBootTest
class Board201812047ApplicationTests {

    @Autowired
    MemberRepository memberRepository;  // MemberRepository 클래스형 객체를 Spring을 통하여 생성

    @Test   // Unit Test : JUnit 도구 활용 -> 통합 테스트(Integration Test)
    void contextLoads() {
        // Integer 데이터 흐름, Lambda식 - 함수형 언어의 특징을 활용
        IntStream.rangeClosed(1, 50).forEach(i -> {
            MemberEntity member = MemberEntity.builder()
                    .id("id " + i)
                    .pw("pw " + i)
                    .name("name " + i)
                    .email("email-" + i + "@induk.ac.kr")
                    .phone("010 - " + new Random().nextInt(50) + "-" + new Random().nextInt(50))
                    .address("address " + i)
                    .deny("general")
                    .level("" + (new Random().nextInt(3) + 1))
                    .build();
            memberRepository.save(member);
        });
    }

    @Test
    void testAdmin() {
        // Integer 데이터 흐름, Lambda 식 - 함수형 언어의 특징을 활용
        String str = "admin";
        MemberEntity entity = MemberEntity.builder()
                .id(str)
                .pw(str)
                .name("name-"+str)
                .email(str + "@induk.ac.kr")
                .phone("phone-" + new Random().nextInt(50))
                .address("address-" + new Random().nextInt(50))
                .deny("admin")
                .level("" + (new Random().nextInt(3) + 1))
                .build();
        memberRepository.save(entity);
    }
}
