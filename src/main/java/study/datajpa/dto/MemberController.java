package study.datajpa.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id){
        return memberRepository.findById(id).get().getUsername();
    }

    //Domain class Converter
    //HTTP 파라미터로 넘어온 엔티티의 아이디로 엔티티 객체를 찾아서 바인딩
    //Spring이 중간에서 컨버팅하는 과정을 끝내고 파라미터(member)에 인젝션해준다.
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member){
        return member.getUsername();
    }

    //@PageableDefault가 전역설정보다 우선권을 갖는다
    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 5, sort = "username") Pageable pageable){
//        return memberRepository.findAll(pageable)
//                .map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        return memberRepository.findAll(pageable)
                .map(MemberDto::new);
    }

    @PostConstruct
    public void init(){
        List<Member> saveList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
             saveList.add(new Member("user" + i, i));

        }
        memberRepository.saveAll(saveList);
    }
}
