package edu.stanford.protege.webprotege.initialrevisionhistoryservice;

import edu.stanford.protege.webprotege.common.BlobLocation;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.revision.Revision;
import edu.stanford.protege.webprotege.revision.RevisionSerializationTask;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-05-03
 */
@Component
public class RevisionHistoryStorer {

    private final MinioRevisionHistoryDocumentStorer documentStorer;

    public RevisionHistoryStorer(MinioRevisionHistoryDocumentStorer documentStorer) {
        this.documentStorer = documentStorer;
    }

    public BlobLocation storeRevision(Revision revision) {
        try {
            var tempFile = Files.createTempFile("webprotege-", "-revision-history.bin");
            var revisionSerializationTask = new RevisionSerializationTask(tempFile.toFile(), revision);
            revisionSerializationTask.call();
            var location = documentStorer.storeDocument(tempFile);
            Files.delete(tempFile);
            return location;
        } catch (IOException e) {
            throw new UncheckedIOException("Problem storing revision history", e);
        }
    }



    private static Path createTempFile() throws IOException {
        return Files.createTempFile("webprotege-", null);
    }
}
