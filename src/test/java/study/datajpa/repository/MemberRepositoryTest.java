package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

    @PersistenceContext EntityManager em;
    
    @Test
    public void testMember(){
        Member member = new Member("memberA");

        Member savedMember = memberRepository.save(member);

        Optional<Member> byId = memberRepository.findById(savedMember.getId());
        Member findMember = byId.get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThen(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);

    }

    @Test
    public void testNamedQuery(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsername("AAA");

        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void testQuery(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);

        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void findUsernameList(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();

        assertThat(usernameList.get(0)).isEqualTo("AAA");
    }

    @Test
    public void findMemberDto(){
        Team teamA = new Team("teamA");
//        teamRepository.save(teamA);

        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        m1.setTeam(teamA);
        m2.setTeam(teamA);

        memberRepository.save(m1);
        memberRepository.save(m2);


        List<MemberDto> memberDto = memberRepository.findMemberDto();

        assertThat(memberDto.get(1).getTeamName()).isEqualTo("teamA");
    }

    @Test
    public void findByNames(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> byNames = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));

        assertThat(byNames.size()).isEqualTo(2);
    }

    @Test
    public void returnType(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> list = memberRepository.findListByUsername("AAA");
        Member member = memberRepository.findMemberByUsername("AAA");
        Optional<Member> optional = memberRepository.findOptionalByUsername("AAA");

        assertThat(list.size()).isEqualTo(1);
        assertThat(member.getUsername()).isEqualTo("AAA");
        assertThat(optional.isEmpty()).isFalse();
    }

    @Test
    public void paging(){
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        //page를 반환할때는 map을 쓰면 편하다
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        //then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        assertThat(content.size()).isEqualTo(3);
        assertThat(totalElements).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();

        Slice<Member> slice = memberRepository.findSliceByAge(age, pageRequest); // slice면 limit +1 만큼 날려서 다음데이터가 있는지 슬쩍 본다.

    }

    @Test
    public void bultUpdate(){
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);

//        // bulk연산은 DB에 바로 밖는 쿼리로써, 영속성 컨텍스트와 이격이 날 수 있다.
//        // 고로 bulk연산 후 영속성컨텍스트를 초기화해줘야한다.
//        List<Member> memberBefore = memberRepository.findByUsername("member5"); // 벌크연산 후 DB에는 41이 되어있으나, 영속성에는 40으로 저장되어있는것을 리턴
//        System.out.println(memberBefore.get(0));
//        assertThat(memberBefore.get(0).getAge()).isEqualTo(40);
//
//        //같은 transaction이면 같은 EntityManager가 사용된다.
//        em.flush(); // 위에서 JPQL실행되면서 지연 쿼리가 다 실행됨. 더 나갈 쿼리는 없겠지만 혹시모르니 한번 flush해주는것
//        em.clear();
//        // !!!!직접 clear 안해줘도 @Modifying에 clear옵션 넣어주면 된다.!!!!

        List<Member> memberAfter = memberRepository.findByUsername("member5"); // 영속성 클리어 후 41이 된 member를 다시 떠옴
        System.out.println(memberAfter.get(0));
        assertThat(memberAfter.get(0).getAge()).isEqualTo(41);

        //then
        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy(){
        //given
        //member1 -> teamA
        //member2 -> teamB

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamB);

        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //when
//        List<Member> members = memberRepository.findMemberFetchJoin();
//
//        for (Member member : members) {
//            System.out.println("member = " + member.getUsername());
//            System.out.println("member.teamClass = " + member.getTeam().getClass());
//            System.out.println("member.team = " + member.getTeam().getName());
//        }

        List<Member> members = memberRepository.findAll();

        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member.teamClass = " + member.getTeam().getClass());
            System.out.println("member.team = " + member.getTeam().getName());
        }
    }

    @Test
    public void queryHint(){
        //given
        Member member1 = memberRepository.save(new Member("member1", 10));

        em.flush();
        em.clear();

        //when
        Member findMember = memberRepository.findReadOnlyByUsername(member1.getUsername());
        //readonly를 hint로 주었기 때문에 snapshot을 떠놓지 않아 변경감지가 작동하지 않는다.
        findMember.setUsername("member2");

        em.flush();
    }

    @Test
    public void lock(){
        //given
        Member member1 = memberRepository.save(new Member("member1", 10));

        em.flush();
        em.clear();

        //when
        List<Member> result = memberRepository.findLockByUsername(member1.getUsername()); // for update로 나간다
    }

    @Test
    public void callCustom(){
        List<Member> result = memberRepository.findMemberCustom();
    }

}