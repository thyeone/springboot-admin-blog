package iducs.springboot.kthboard;

import iducs.springboot.kthboard.entity.MemberEntity;
import iducs.springboot.kthboard.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;
import java.util.stream.IntStream;

@SpringBootTest
public class MemberTests {
    // POJO (Plain Old Java Object) : 가장 기본적인 자바 객체 형태-필드, getter, setter
    // Beans 규약을 준수, 생성자가 복잡하지 않음
    // DI(Dependency Injection) : 의존성 주입, Spring Framework가 의존성을 해결하는 방법

    @Autowired
    MemberRepository memberRepository;

    @Test
    void contextLoads() {
        // Integer 데이터 흐름, Lambda 식 - 함수형 언어의 특징을 활용
        IntStream.rangeClosed(1, 50).forEach(i -> {
            MemberEntity member = MemberEntity.builder()
                    .id("id-" + i)
                    .pw("pw-" + i)
                    .name("name-" + i)
                    .email("email" + i + "@induk.ac.kr")
                    .phone("phone-" + new Random().nextInt(50))
                    .address("address-" + new Random().nextInt(50))
                    .build();
            memberRepository.save(member);
        });
    }
}
