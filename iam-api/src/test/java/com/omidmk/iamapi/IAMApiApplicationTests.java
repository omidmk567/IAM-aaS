package com.omidmk.iamapi;

import com.omidmk.iamapi.exception.DeploymentNotFoundException;
import com.omidmk.iamapi.exception.RealmAlreadyExistException;
import com.omidmk.iamapi.exception.TicketNotFoundException;
import com.omidmk.iamapi.exception.UserNotFoundException;
import com.omidmk.iamapi.model.deployment.DeploymentModel;
import com.omidmk.iamapi.model.deployment.PlanDV;
import com.omidmk.iamapi.model.ticket.DialogModel;
import com.omidmk.iamapi.model.ticket.TicketModel;
import com.omidmk.iamapi.model.user.UserModel;
import com.omidmk.iamapi.service.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IAMApiApplicationTests {

//    @MockBean
    @Autowired
    private CustomerService customerService;

    @Autowired
    private DeploymentService deploymentService;

    @Autowired
    private KeycloakService keycloakService;

    @Autowired
    private MailService mailService;

    @Autowired
    private TicketService ticketService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(0)
    void contextLoads() {
        assertThat(customerService).isNotNull();
        assertThat(deploymentService).isNotNull();
        assertThat(keycloakService).isNotNull();
        assertThat(mailService).isNotNull();
        assertThat(ticketService).isNotNull();
    }

    @Test
    @Order(1)
    void customerServiceTest() {
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> customerService.findUserById(UUID.randomUUID()));
        UserModel user = customerService.saveUser(new UserModel("test@test.com", false, "firstnameTest", "lastnameTest", 100f));
        assertThat(user).isNotNull();
        assertThat(user.getId()).isNotNull();
        assertThat(user.getEmail()).isEqualTo("test@test.com");
        assertThat(user.getFirstName()).isEqualTo("firstnameTest");
        Optional<UserModel> foundUser = customerService.findUserByEmail("test@test.com");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(user.getId());
        assertThat(customerService.findAll(Pageable.unpaged())).hasSize(1);
    }

    @Test
    @Order(2)
    void deploymentServiceTest() throws RealmAlreadyExistException {
        assertThatExceptionOfType(DeploymentNotFoundException.class)
                .isThrownBy(() -> deploymentService.findDeploymentById(UUID.randomUUID()));
        assertThat(deploymentService.findAllDeployments(Pageable.unpaged())).isEmpty();
        assertThat(deploymentService.isRealmAvailable("test")).isTrue();
        assertThat(deploymentService.isRealmAvailable("master")).isFalse();
        UserModel user = customerService.findAllCustomers(Pageable.unpaged()).toList().get(0);
        DeploymentModel deployment = deploymentService.createDeployment(user, "test", PlanDV.BEGINNER);
        assertThat(deployment).isNotNull();
        assertThat(deployment.getId()).isNotNull();
        assertThat(deployment.getRealmName()).isEqualTo("test");
        assertThat(deployment.getState().toString()).isEqualTo("DEPLOYING");
    }

    @Test
    @Order(3)
    void ticketServiceTest() {
        assertThatExceptionOfType(TicketNotFoundException.class)
                .isThrownBy(() -> ticketService.findTicketById(UUID.randomUUID()));
        assertThat(ticketService.findAllTickets(Pageable.unpaged())).isEmpty();
        assertThat(ticketService.findClosedTickets(Pageable.unpaged())).isEmpty();
        UserModel user = customerService.findAllCustomers(Pageable.unpaged()).toList().get(0);
        TicketModel ticket = new TicketModel();
        ticket.setCustomer(user);
        ticket.setDialogs(List.of(new DialogModel(user, "Hello, test.")));
        ticket.setState(TicketModel.State.WAITING_FOR_ADMIN_RESPONSE);
        ticket = ticketService.saveTicket(ticket);
        assertThat(ticket).isNotNull();
        assertThat(ticket.getId()).isNotNull();
        assertThat(ticket.getDialogs()).hasSize(1);
        assertThat(ticket.getDialogs().get(0).getText()).isEqualTo("Hello, test.");
        assertThat(ticket.getState().toString()).isEqualTo("WAITING_FOR_ADMIN_RESPONSE");
    }

    @Test
    void shouldReturnDefaultMessage() throws Exception {
        this.mockMvc.perform(get("/v1/plans/beginner")).andDo(print()).andExpect(status().is4xxClientError());
    }
}
