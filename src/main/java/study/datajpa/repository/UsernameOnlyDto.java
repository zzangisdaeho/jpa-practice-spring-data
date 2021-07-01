package study.datajpa.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class UsernameOnlyDto {

    private final String username;

    //생성자 파라미터명이 중요!!
    //생성자 파라미터명과 엔티티 파라미터가 일치하는걸 기준으로 jpa가 값을 넣어준다
    public UsernameOnlyDto(String username) {
        this.username = username;
    }
}
