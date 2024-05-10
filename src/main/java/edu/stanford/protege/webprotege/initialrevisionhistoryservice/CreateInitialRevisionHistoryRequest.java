package edu.stanford.protege.webprotege.initialrevisionhistoryservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.BlobLocation;
import edu.stanford.protege.webprotege.common.Request;

import java.util.List;

import static edu.stanford.protege.webprotege.initialrevisionhistoryservice.CreateInitialRevisionHistoryRequest.CHANNEL;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-05-03
 */
@JsonTypeName(CHANNEL)
public record CreateInitialRevisionHistoryRequest(@JsonProperty("documentLocations") List<BlobLocation> documentLocations) implements Request<CreateInitialRevisionHistoryResponse> {

    public static final String CHANNEL = "webprotege.revisions.CreateInitialRevisionHistory";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
