package edu.stanford.protege.webprotege.initialrevisionhistoryservice;

import edu.stanford.protege.webprotege.change.AddAxiomChange;
import edu.stanford.protege.webprotege.change.AddOntologyAnnotationChange;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.common.BlobLocation;
import edu.stanford.protege.webprotege.common.UserId;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.semanticweb.binaryowl.BinaryOWLOntologyDocumentHandlerAdapter;
import org.semanticweb.binaryowl.BinaryOWLOntologyDocumentSerializer;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.springframework.stereotype.Component;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-05-03
 */
@Component
public class OntologyChangesLoader {

    private final MinioOntologyDocumentLoader ontologyDocumentLoader;

    public OntologyChangesLoader(MinioOntologyDocumentLoader ontologyDocumentLoader) {
        this.ontologyDocumentLoader = ontologyDocumentLoader;
    }

    public void loadOntologyAndPopulateChanges(BlobLocation blobLocation,
                                               UserId userId,
                                               List<OntologyChange> ontologyChanges) throws UncheckedIOException {

        var serializer = new BinaryOWLOntologyDocumentSerializer();
        var ontologyIdRef = new AtomicReference<OWLOntologyID>();
        var ontologyDocumentBytes = ontologyDocumentLoader.loadOntologyDocument(blobLocation);
        try {
            serializer.read(new ByteArrayInputStream(ontologyDocumentBytes),
                            new BinaryOWLOntologyDocumentHandlerAdapter<>() {
                                @Override
                                public void handleOntologyID(OWLOntologyID ontologyID) throws RuntimeException {
                                    ontologyIdRef.set(ontologyID);
                                }

                                @Override
                                public void handleOntologyAnnotations(Set<OWLAnnotation> annotations) throws RuntimeException {
                                    annotations.forEach(annotation -> {
                                        ontologyChanges.add(new AddOntologyAnnotationChange(ontologyIdRef.get(),
                                                                                            annotation));
                                    });
                                }

                                @Override
                                public void handleAxioms(Set<OWLAxiom> axioms) throws RuntimeException {
                                    axioms.forEach(axiom -> {
                                        ontologyChanges.add(new AddAxiomChange(ontologyIdRef.get(), axiom));
                                    });
                                }
                            },
                            new OWLDataFactoryImpl());
        } catch (IOException e) {
            throw new UncheckedIOException("Problem deserailizing ontology document", e);
        }
    }
}
