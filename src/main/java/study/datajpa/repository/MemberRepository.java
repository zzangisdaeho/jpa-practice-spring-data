package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    //Query annotation을 생략해도 관례상 Entity.MethodName과 일치하는 namedQuery가 있는지 확인하여 있으면 선적용한다. 없으면 메소드네이밍으로 실행한다.
//    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);
}
