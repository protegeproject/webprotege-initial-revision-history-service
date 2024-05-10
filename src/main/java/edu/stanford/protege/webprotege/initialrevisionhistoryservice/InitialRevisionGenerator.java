package edu.stanford.protege.webprotege.initialrevisionhistoryservice;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.common.BlobLocation;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.revision.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-05-03
 */
@Component
public class InitialRevisionGenerator {

    private final Logger logger = LoggerFactory.getLogger(InitialRevisionGenerator.class);

    private final OntologyChangesLoader ontologyChangesLoader;

    private final RevisionHistoryStorer revisionHistoryStorer;

    public InitialRevisionGenerator(OntologyChangesLoader ontologyChangesLoader,
                                    RevisionHistoryStorer revisionHistoryStorer) {
        this.ontologyChangesLoader = ontologyChangesLoader;
        this.revisionHistoryStorer = revisionHistoryStorer;
    }

    public BlobLocation writeRevisionHistoryFromOntologies(UserId userId,
                                                           List<BlobLocation> ontologyDocumentLocations,
                                                           String initialChangeDescription) {
        logger.info("Creating initial revision history document from ontologies");
        var ontologyChanges = new ArrayList<OntologyChange>();
        ontologyDocumentLocations.forEach(ontologyDocumentLocation -> {
            ontologyChangesLoader.loadOntologyAndPopulateChanges(ontologyDocumentLocation, userId, ontologyChanges);
            logger.info("Processed ontology at {}.  Cumulative number of changes: {}", ontologyDocumentLocation, ontologyChanges.size());
        });
        var initialRevisionNumber = RevisionNumber.getRevisionNumber(1);
        var timestamp = System.currentTimeMillis();
        var initialRevision = new Revision(userId, initialRevisionNumber, ImmutableList.copyOf(ontologyChanges), timestamp, initialChangeDescription);
        var revisionLocation = revisionHistoryStorer.storeRevision(initialRevision);
        logger.info("Stored revision history document at {}", revisionLocation);
        return revisionLocation;
    }


}
