package me.blog.anabada.entities;

import java.util.Arrays;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserAccount extends User {

    private Account account;

    public UserAccount(Account account) {
        super(account.getNickname(), account.getPassword(), Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }
}
