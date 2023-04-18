package iducs.springboot.kthboard.entity;

import lombok.*;

import javax.persistence.*;

// Spring Data 관련 Annotations
@Entity // Spring Data Jpa의 엔티티(entity, 개체)임을 나타냄
@Table(name="member201712046")

// Lombok 관련 Annotations
@ToString   // toString() 생성
@Getter     // getter() 생성
@Setter     // setter() 추가
@Builder
@NoArgsConstructor  // 모든 매개변수를 갖는 생성자
@AllArgsConstructor // 디폴트 생성자(아무런 매개변수가 없음)
public class MemberEntity {
    // entity -> repository에서 주로 사용됨
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;   // long 기본형 Wrapper 클래스

    @Column(length = 30, nullable = false)
    private String id;

    @Column(length = 20, nullable = false)
    private String pw;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private String email;

    @Column(length = 30, nullable = false)
    private String phone;

    @Column(length = 100)
    private String address;

    @Column(length = 10)
    private String deny;

    @Column(length = 10)
    private String level;
}
