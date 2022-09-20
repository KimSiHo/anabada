package me.blog.anabada.prod.src;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.blog.anabada.dao.AccountRepository;
import me.blog.anabada.entities.Account;
import me.blog.anabada.entities.UserAccount;

public class TestUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) {
        Account account = Account.builder()
            .email("test001@naver.com")
            .nickname("test_user")
            .password("test1234")
            .bio("i am a test user")
            .build();

        return new UserAccount(account);
    }
}