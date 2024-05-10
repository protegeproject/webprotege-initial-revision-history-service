package edu.stanford.protege.webprotege.initialrevisionhistoryservice;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.WebProtegeCommonConfiguration;
import edu.stanford.protege.webprotege.ipc.WebProtegeIpcApplication;
import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import edu.stanford.protege.webprotege.revision.*;
import io.minio.MinioClient;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

@SpringBootApplication
@Import({WebProtegeIpcApplication.class})
public class WebprotegeInitialRevisionHistoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebprotegeInitialRevisionHistoryServiceApplication.class, args);
    }


    @Bean
    MinioClient minioClient(MinioProperties properties) {
        return MinioClient.builder()
                          .credentials(properties.getAccessKey(), properties.getSecretKey())
                          .endpoint(properties.getEndPoint())
                          .build();
    }
}
