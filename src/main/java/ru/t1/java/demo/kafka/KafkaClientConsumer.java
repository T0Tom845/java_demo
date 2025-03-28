package ru.t1.java.demo.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.model.dto.ClientDto;
import ru.t1.java.demo.service.ClientService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaClientConsumer {

    private final ClientService clientService;

    @KafkaListener(id = "${t1.kafka.consumer.group-id}",
            topics = {"t1_demo_client_registration", "client_topic"},
            containerFactory = "kafkaListenerContainerFactory")
    public void listener(@Payload List<ClientDto> messageList,
                         Acknowledgment ack,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        log.debug("Client consumer: Обработка новых сообщений");


        try {
            log.error("Topic: " + topic);
            log.error("Key: " + key);
            messageList.stream()
                    .forEach(System.err::println);
//            List<Client> clients = messageList.stream()
//                    .map(dto -> {
//                        dto.setFirstName(key + "@" + dto.getFirstName());
//                        return ClientMapper.toEntity(dto);
//                    })
//                    .toList();
//            clientService.registerClients(clients);
        } finally {
            ack.acknowledge();
        }


        log.debug("Client consumer: записи обработаны");
    }
}
