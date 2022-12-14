package me.blog.anabada.local.signUp;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.web.servlet.MockMvc;

import lombok.RequiredArgsConstructor;
import me.blog.anabada.common.mail.EmailService;
import me.blog.anabada.dao.AccountRepository;
import me.blog.anabada.entities.Account;

@RequiredArgsConstructor
@ActiveProfiles("local")
@TestConstructor(autowireMode = AutowireMode.ALL)
@AutoConfigureMockMvc
@SpringBootTest
public class SignUpTest {

    @MockBean
    private final EmailService emailService;
    private final AccountRepository accountRepository;
    private final MockMvc mockMvc;

    @DisplayName("?????? ?????? ?????? ???????????? ?????????")
    @Test
    void signUpForm() throws Exception {
        mockMvc.perform(get("/account/sign-up"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("/account/sign-up"))
            .andExpect(model().attributeExists("signUpForm"))
            .andExpect(unauthenticated());
    }

    @DisplayName("?????? ?????? ?????? - ????????? ??????")
    @Test
    void signUpWrongInput() throws Exception {
        mockMvc.perform(post("/account/sign-up")
            .param("nickname", "kimsiho")
            .param("email", "email..")
            .param("password", "12345")
            .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("/account/sign-up"))
            .andExpect(unauthenticated());
    }

    @DisplayName("?????? ?????? ?????? - ????????? ??????")
    @Test
    void signUpSubmit_with_correct_input() throws Exception {
        mockMvc.perform(post("/account/sign-up")
            .param("nickname", "kimsiho-test")
            .param("email", "kim125y@email.com")
            .param("password", "12345678")
            .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/"))
            .andExpect(authenticated().withUsername("kimsiho-test"));

        verify(emailService, times(1)).send(anyString(), anyString(), anyList());

        Account account = accountRepository.findByEmail("kim125y@email.com");
        assertNotNull(account);
        assertNotEquals(account.getPassword(), "12345678");
        assertNotNull(account.getEmailCheckToken());
    }
}
