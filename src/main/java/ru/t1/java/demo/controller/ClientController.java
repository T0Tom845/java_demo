package ru.t1.java.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.t1.java.demo.aop.annotation.LogMethod;
import ru.t1.java.demo.aop.annotation.LoggableException;
import ru.t1.java.demo.kafka.KafkaClientProducer;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.model.dto.ClientDto;
import ru.t1.java.demo.model.enums.Metrics;
import ru.t1.java.demo.repository.ClientRepository;
import ru.t1.java.demo.service.ClientService;
import ru.t1.java.demo.service.MetricService;
import ru.t1.java.demo.util.ClientMapper;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final ClientService clientService;
    private final KafkaClientProducer kafkaClientProducer;
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final MetricService metricService;

    @Value("${t1.kafka.topic.client_registration}")
    private String topic;

    @LogMethod
    @GetMapping(value = "/parse")
    @LoggableException
    public void parseSource() {
//        try {
        // ...
        throw new IllegalStateException();
//        } catch (IllegalStateException e) {
//            log.warn(e.getMessage());
//        }

//        clientRepository.save(Client.builder()
//                .firstName("John42")
//                .build());
//        clientRepository.findClientByFirstName("John42");
//        metricService.incrementByName(Metrics.CLIENT_CONTROLLER_REQUEST_COUNT.getValue());
    }

    //    @HandlingResult
    @LogMethod
    @GetMapping("/client")
    public ResponseEntity<ClientDto> getClient() {
        log.debug("Getting client with id ");
        return ResponseEntity.ok()
                .body(ClientDto.builder()
                        .firstName("John")
                        .build());
    }

    @GetMapping("/admin")
//    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }

    @GetMapping("/register")
    public ResponseEntity<List<Client>> register(@RequestBody ClientDto clientDto) {
        log.info("Registering client: {}", clientDto);
        List<Client> clients = clientService.registerClients(
                List.of(clientMapper.toEntityWithId(clientDto))
        );
//        log.info("Client registered: {}", client.getId());
        metricService.incrementByName(Metrics.CLIENT_CONTROLLER_REQUEST_COUNT.getValue());
        return ResponseEntity.ok().body(clients);
    }
}
