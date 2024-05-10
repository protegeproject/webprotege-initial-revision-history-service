package edu.stanford.protege.webprotege.initialrevisionhistoryservice;

import edu.stanford.protege.webprotege.common.BlobLocation;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-05-03
 */
@Component
public class MinioOntologyDocumentLoader {

    private final MinioClient minioClient;

    private final MinioProperties minioProperties;

    public MinioOntologyDocumentLoader(MinioClient minioClient, MinioProperties minioProperties) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
    }

    public byte [] loadOntologyDocument(@Nonnull BlobLocation location) throws StorageException {
        try {
            var object = minioClient.getObject(GetObjectArgs.builder()
                                                            .bucket(minioProperties.getOntologyDocumentsBucketName())
                                                            .object(location.name())
                                                            .build());
            return object.readAllBytes();
        } catch (ErrorResponseException | XmlParserException | ServerException | NoSuchAlgorithmException |
                 IOException | InvalidResponseException | InvalidKeyException | InternalException |
                 InsufficientDataException e) {
            throw new StorageException("Problem reading ontology document object from storage", e);
        }
    }
}
