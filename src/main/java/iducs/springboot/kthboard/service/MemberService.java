package iducs.springboot.kthboard.service;



import iducs.springboot.kthboard.domain.Member;
import iducs.springboot.kthboard.domain.PageRequestDTO;
import iducs.springboot.kthboard.domain.PageResultDTO;
import iducs.springboot.kthboard.entity.MemberEntity;

import java.util.List;

public interface MemberService {
    void create(Member member);
    Member readById(Long seq);
    PageResultDTO<Member, MemberEntity> readListBy(PageRequestDTO pageRequestDTO);
    List<Member> readAll();
    void update(Member member);
    void delete(Member member);

    Member readByName(Member member);
    Member readByEmail(String email);

    Member loginByEmail(Member member);

    /**
     * 주의 : DTO와 Entity의 pk number의 자료형을 똑같이 맞출 것
     */
    default MemberEntity dtoToEntity(Member member) {
        MemberEntity entity = MemberEntity.builder()
                .seq(member.getSeq())
                .id(member.getId())
                .pw(member.getPw())
                .name(member.getName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .address(member.getAddress())
                .deny(member.getDeny())
                .level(member.getLevel())
                .build();
        return entity;
    }

    // Service -> Controller : entity -> dto로 변환 후 반환
    default Member entityToDto(MemberEntity entity) {
        Member member = Member.builder()
                .seq(entity.getSeq())
                .id(entity.getId())
                .pw(entity.getPw())
                .name(entity.getName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .deny(entity.getDeny())
                .level(entity.getLevel())
                .build();
        return member;
    }
}
