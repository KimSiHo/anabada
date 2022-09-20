package me.blog.anabada.local.product;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.http.entity.ContentType;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;

import lombok.RequiredArgsConstructor;
import me.blog.anabada.common.mail.EmailService;
import me.blog.anabada.dao.AccountRepository;
import me.blog.anabada.entities.Account;
import me.blog.anabada.prod.src.TestUserDetailsService;

@RequiredArgsConstructor
@ActiveProfiles("local")
@TestConstructor(autowireMode = AutowireMode.ALL)
@AutoConfigureMockMvc
@SpringBootTest
public class ProductTest {

    @MockBean
    private final EmailService emailService;
    private final AccountRepository accountRepository;
    private final MockMvc mockMvc;
    private final TestUserDetailsService testUserDetailsService = new TestUserDetailsService();
    private UserDetails testUser;

    @BeforeEach
    void setup() {
        Account account = Account.builder()
            .email("test001@naver.com")
            .nickname("test_user")
            .password("test1234")
            .bio("i am a test user")
            .build();

        accountRepository.save(account);

        testUser = testUserDetailsService.loadUserByUsername("test_user");
    }

    @WithMockUser("USER")
    @DisplayName("상품 등록 화면 보이는지 테스트")
    @Test
    void productUploadForm() throws Exception {
        mockMvc.perform(get("/product/upload"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("product/product-upload"))
            .andExpect(model().attributeExists("productUploadForm"));
    }

    @DisplayName("상품 정상 등록 테스트")
    @Test
    void productUpload() throws Exception {
        Path path = Paths.get("src/test/resources/test-image/product-upload.jpg");
        //File file = ResourceUtils.getFile(this.getClass().getResource("/test-image/product-upload.jpg"));

        MockMultipartFile file = new MockMultipartFile("productImageFiles",
            "product-upload.png",
            ContentType.IMAGE_PNG.toString(),
            new FileInputStream(path.toFile()));

        mockMvc.perform(multipart("/product/upload")
            .file(file)
            .part(new MockPart("bio", "짧은 소개123456".getBytes(StandardCharsets.UTF_8)),
                  new MockPart("productKindCode", "PSC001".getBytes(StandardCharsets.UTF_8)))
            .with(user(testUser)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("/"));

//        mockMvc.perform(multipart("/product/upload")
//            .file(file)
//            .with(request -> {return request;})
//            .param()
//            .part(new MockPart("bio", "짧은 소개123456".getBytes(StandardCharsets.UTF_8)),
//                new MockPart("productKindCode", "PSC001".getBytes(StandardCharsets.UTF_8))))
//
//            .with(user("test").roles("USER"))
//            .andDo(print())
//            .andExpect(status().isOk())
//            .andExpect(view().name("/"));

    }

}
