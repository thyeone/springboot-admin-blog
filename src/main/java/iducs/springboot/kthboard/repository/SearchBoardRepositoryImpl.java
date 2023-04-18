package iducs.springboot.kthboard.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import iducs.springboot.kthboard.entity.BoardEntity;
import iducs.springboot.kthboard.entity.QBoardEntity;
import iducs.springboot.kthboard.entity.QMemberEntity;
import iducs.springboot.kthboard.entity.QReplyEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@Qualifier("SearchBoardRepositoryImpl")
@Log4j2
public class SearchBoardRepositoryImpl
    extends QuerydslRepositorySupport implements SearchBoardRepository {

    public SearchBoardRepositoryImpl() {
        super(BoardEntity.class);
    }

    @Override
    public BoardEntity searchBoard() {
        // test code
        return null;
    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
        log.info("---------------searchPage----------------");
        QBoardEntity boardEntity = QBoardEntity.boardEntity;
        QReplyEntity replyEntity = QReplyEntity.replyEntity;
        QMemberEntity memberEntity = QMemberEntity.memberEntity;

        JPQLQuery<BoardEntity> jpqlQuery = from(boardEntity);
        jpqlQuery.leftJoin(memberEntity).on(boardEntity.writer.eq(memberEntity));
        jpqlQuery.leftJoin(replyEntity).on(replyEntity.board.eq(boardEntity));
        // select b, w, count(r) from BoardEntity b left join b.writer w left join ReplyEntity r on r.board= b;
        JPQLQuery<Tuple> tuple = jpqlQuery.select(boardEntity, memberEntity, replyEntity.count());

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = boardEntity.bno.gt(0L);

        booleanBuilder.and(expression);

        if(type != null) {
            BooleanBuilder conditionBuilder = new BooleanBuilder();
            if(type.contains("engine"))  // 공학
                booleanBuilder.or(boardEntity.category.contains(keyword));
            if(type.contains("sport"))  // 예체능
                booleanBuilder.or(boardEntity.category.contains(keyword));
            if(type.contains("human"))  // 인문
                booleanBuilder.or(boardEntity.category.contains(keyword));
            booleanBuilder.and(conditionBuilder);
        }
        tuple.where(booleanBuilder);
        Sort sort = pageable.getSort();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending()? Order.ASC: Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(BoardEntity.class, "boardEntity");
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        tuple.groupBy(boardEntity);

        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();

        log.info(result);
        long count = tuple.fetchCount();
        log.info("Count: " + count);
        return new PageImpl<Object[]>(result.stream().map(t -> t.toArray()).collect(Collectors.toList()), pageable, count);
    }
}
