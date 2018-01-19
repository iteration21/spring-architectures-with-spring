package com.packtpub.externalbanks;

import com.packtpub.externalbanks.transfer.EventNotificationChannel;
import com.packtpub.transfermoneyapp.domain.TransferMoneyDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

@Slf4j
@EnableBinding(EventNotificationChannel.class)
@SpringBootApplication
public class ExternalBanksApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExternalBanksApplication.class, args);
    }


    @Bean
    IntegrationFlow integrationFlow(EventNotificationChannel eventNotificationChannel) {
        return IntegrationFlows.from(eventNotificationChannel.subscriptionOnMoneyTransferredChannel()).
                handle(TransferMoneyDetails.class, (payload, headers) -> {
                    log.info("Should we notify to external banks: " + payload.isExternalBank());
                    if (payload.isExternalBank()) {
                        log.info("Notifying to external bank about transaction: " + payload);
                    }
                    return null;
                }).get();
    }
}


