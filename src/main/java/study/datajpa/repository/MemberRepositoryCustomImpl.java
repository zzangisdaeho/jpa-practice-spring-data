package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {
    //Impl을 접미사로 꼭 써야한다. (Impl)
    //부모 인터페이스는 아무거나 지어도 상관없다. (MemberRepositoryCustom)
    //JPA repository 인터페이스의 명칭이 꼭 들어가야한다. (MemberRepository)
    //그래야 spring data JPA가 조립해서 MemberRepository에서 MemberRepositoryCustomImpl의 구현체를 쓸 수 있게 해준다.
    //이건 Spring이 해주는것이지 JAVA가 이러한 구조를 읽을 수 있는것은 아니다!

    // Impl가 싫으면 아래와 같은 옵션을 넣고 바꿀순 있다. 근데 그냥 관례대로 쓰자;;
//    @EnableJpaRepositories(basePackages = "study.datajpa.repository",
//            repositoryImplementationPostfix = "Impl")

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m")
                .getResultList();
    }
}
