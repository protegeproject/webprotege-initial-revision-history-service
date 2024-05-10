package edu.stanford.protege.webprotege.initialrevisionhistoryservice;

import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-05-03
 */
@WebProtegeHandler
public class CreateInitialRevisionHistoryCommandHandler implements CommandHandler<CreateInitialRevisionHistoryRequest, CreateInitialRevisionHistoryResponse> {

    private final InitialRevisionGenerator initialRevisionGenerator;

    public CreateInitialRevisionHistoryCommandHandler(InitialRevisionGenerator initialRevisionGenerator) {
        this.initialRevisionGenerator = initialRevisionGenerator;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return CreateInitialRevisionHistoryRequest.CHANNEL;
    }

    @Override
    public Class<CreateInitialRevisionHistoryRequest> getRequestClass() {
        return CreateInitialRevisionHistoryRequest.class;
    }

    @Override
    public Mono<CreateInitialRevisionHistoryResponse> handleRequest(CreateInitialRevisionHistoryRequest request,
                                                                    ExecutionContext executionContext) {
        var changeDocumentLocation = initialRevisionGenerator.writeRevisionHistoryFromOntologies(executionContext.userId(),
                                                                    request.documentLocations(),
                                                                    "Initial import");
        return Mono.just(new CreateInitialRevisionHistoryResponse(changeDocumentLocation));
    }
}
