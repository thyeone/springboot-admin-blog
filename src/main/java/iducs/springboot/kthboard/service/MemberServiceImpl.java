package iducs.springboot.kthboard.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;

import iducs.springboot.kthboard.domain.Member;
import iducs.springboot.kthboard.domain.PageRequestDTO;
import iducs.springboot.kthboard.domain.PageResultDTO;
import iducs.springboot.kthboard.entity.MemberEntity;
import iducs.springboot.kthboard.entity.QMemberEntity;
import iducs.springboot.kthboard.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
public class MemberServiceImpl implements MemberService {

    final MemberRepository memberRepository;  // DI(Dependency Injection)

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void create(Member member) {
        MemberEntity entity = dtoToEntity(member);
        memberRepository.save(entity);
    }

    @Override
    public Member readById(Long seq) {
        Member member = null;

        Optional<MemberEntity> result = memberRepository.findById(seq);
        if(result.isPresent()) {
            member = entityToDto(result.get());
        }
        return member;
    }

    @Override
    public PageResultDTO<Member, MemberEntity> readListBy(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("seq").descending());
        BooleanBuilder booleanBuilder = findByCondition(pageRequestDTO);
        Page<MemberEntity> result = memberRepository.findAll(booleanBuilder, pageable);
        Function<MemberEntity, Member> fn = (entity -> entityToDto(entity));
        return new PageResultDTO<>(result, fn);
    }

    private BooleanBuilder findByCondition(PageRequestDTO pageRequestDTO) {
        String type = pageRequestDTO.getType();
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QMemberEntity qMemberEntity = QMemberEntity.memberEntity;

        BooleanExpression expression = qMemberEntity.seq.gt(0L);    // where seq > 0 and title == "ti"
        booleanBuilder.and(expression);

        if(type == null || type.trim().length() == 0) {
            return booleanBuilder;
        }
        String keyword = pageRequestDTO.getKeyword();

        log.info(">>>>>>>> " + pageRequestDTO);

        BooleanBuilder conditionBuilder = new BooleanBuilder();
        if(type.contains("1"))  // 1단계
            conditionBuilder.or(qMemberEntity.level.contains(keyword));
        if(type.contains("2"))  // 2단계
            conditionBuilder.or(qMemberEntity.level.contains(keyword));
        if(type.contains("3"))  // 3단계
            conditionBuilder.or(qMemberEntity.level.contains(keyword));
        if(type.contains("e"))  // email로 검색
            conditionBuilder.or(qMemberEntity.email.contains(keyword));
        if(type.contains("p"))  // phone으로 검색
            conditionBuilder.or(qMemberEntity.phone.contains(keyword));
        if(type.contains("a"))  // address로 검색
            conditionBuilder.or(qMemberEntity.address.contains(keyword));
        booleanBuilder.and(conditionBuilder);
        return booleanBuilder;  // 완성된 조건 or 술어(predicate)
    }

    @Override
    public List<Member> readAll() {
        List<Member> members = new ArrayList<>();   // 반환 리스트 객체
        // JpaRepository 구현체의 메서드 findAll(), List<T>
        List<MemberEntity> entities = memberRepository.findAll();   // entity들
        for(MemberEntity entity : entities) {
            Member member = entityToDto(entity);
            members.add(member);
        }
        return members;
    }

    @Override
    public void update(Member member) {
        MemberEntity entity = dtoToEntity(member);
        memberRepository.save(entity);
    }

    @Override
    public void delete(Member member) {
        MemberEntity entity = dtoToEntity(member);
        memberRepository.deleteById(entity.getSeq());   // 유일키로 지운다
        // memberRepository.delete(entity);
    }

    @Override
    public Member loginByEmail(Member member) {
        Member memberDTO = null;
        Object result = memberRepository.getMemberByEmail(member.getEmail(), member.getPw());
        if(result != null) {
            memberDTO = entityToDto((MemberEntity) result);
        }
        return memberDTO;
    }

    @Override
    public Member readByName(Member member) {
        return null;
    }

    @Override
    public Member readByEmail(String email) {
        return null;
    }
}
