package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

//인터페이스를 보고 spring data에서 클래스를 자동 생성하여(proxy) projection해준다
public interface UsernameOnly {

    //@value로 넣어주면 get호출시 해당 포멧으로 값을 넘겨준다.
    //Open Projections를 사용하면 select 최적화는 안된다. (전체 column을 select한다)
    @Value("#{target.username + ' ' + target.age}")
    String getUsername();
}
