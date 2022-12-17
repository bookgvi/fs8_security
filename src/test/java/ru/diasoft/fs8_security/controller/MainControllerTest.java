package ru.diasoft.fs8_security.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MainControllerTest {
    private final String root = "/";
    private final String index = "index.html";
    private final String uriIndex = root + index;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void checkRedirectionToLoginForm() throws Exception {
        String loginUrl = "http://localhost/login";
        mockMvc.perform(get(uriIndex))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(loginUrl));
    }


    @Test
    @WithMockUser(roles = "SPECIAL_USER")
    void checkSecurityAccessTo_indexPage_forUserWithRightRole() throws Exception {
        mockMvc.perform(get(root))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(forwardedUrl(index));

        mockMvc.perform(get(uriIndex))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Welcome to main page")));
    }

    @Test
    void cheackSecurityAccessTo_helloPage() throws Exception {
        String uri = "/hello.html";
        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Welcome to greeting page!")));
    }
}