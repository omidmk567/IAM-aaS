package com.omidmk.iamapi;

import com.omidmk.iamapi.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class IAMApiApplicationTests {

    @MockBean
    private CustomerService customerService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
        assertThat(customerService).isNotNull();
//        Assertions.assertNull(customerService.findUserByEmail("omid@gmail.com").orElse(null));
    }

    @Test
    void shouldReturnDefaultMessage() throws Exception {
        this.mockMvc.perform(get("/v1/plans/beginner")).andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("")));
    }

}
