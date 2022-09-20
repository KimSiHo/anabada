package me.blog.anabada.local.main;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.web.servlet.MockMvc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.blog.anabada.service.impl.AccountServiceImpl;

@Slf4j
@RequiredArgsConstructor
@ActiveProfiles("local")
@TestConstructor(autowireMode = AutowireMode.ALL)
@AutoConfigureMockMvc
@SpringBootTest
public class HomeControllerTest {

    private final MockMvc mockMvc;
    private final AccountServiceImpl accountService;

    @DisplayName("로그아웃 상태 - 메인 화면 테스트")
    @Test
    void logoutMainPageTest() throws Exception {
        mockMvc.perform(get("/"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("index"))
            .andExpect(unauthenticated());
    }

    @DisplayName("로그인 상태 - 메인 화면 테스트")
    @Test
    void loginMainPageTest() throws Exception {

        mockMvc.perform(get("/"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("index"))
            .andExpect(model().attributeExists("account"))
            .andExpect(model().attributeExists("notiCount"))
            .andExpect(authenticated());
    }
}
