package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {
    //꼭 엔티티 하나당 하나의 Repository만 있어야하는것은 아니다. 여러개 만들어도 된다는것!

    private final EntityManager em;

    List<Member> findAllMembers(){
        return em.createQuery("select m from Member m").getResultList();
    }
}
