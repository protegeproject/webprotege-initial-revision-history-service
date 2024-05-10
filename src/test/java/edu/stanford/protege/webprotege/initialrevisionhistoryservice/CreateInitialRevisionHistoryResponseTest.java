package edu.stanford.protege.webprotege.initialrevisionhistoryservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import edu.stanford.protege.webprotege.common.BlobLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;

import java.io.StringReader;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CreateInitialRevisionHistoryResponseTest {

    protected static final String BUCKET_NAME = "bucket";

    protected static final String OBJECT_NAME = "name";

    private JacksonTester<CreateInitialRevisionHistoryResponse> json;

    private CreateInitialRevisionHistoryResponse response;

    @BeforeEach
    public void setup() {
        var objectMapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule());
        JacksonTester.initFields(this, objectMapper);

        var location = new BlobLocation(BUCKET_NAME, OBJECT_NAME);
        response = new CreateInitialRevisionHistoryResponse(new BlobLocation(BUCKET_NAME, OBJECT_NAME));
    }

    @Test
    public void testSerialization() throws Exception {
        var written = json.write(response);
        assertThat(written).hasJsonPathStringValue("$.documentLocation.bucket", BUCKET_NAME);
        assertThat(written).hasJsonPathStringValue("$.documentLocation.name", OBJECT_NAME);
    }

    @Test
    public void testDeserialization() throws Exception {
        var read = json.read(new StringReader("""
                                           {
                                                "documentLocation" :
                                                    {
                                                        "bucket" : "bucket",
                                                        "name" : "name"
                                                    }
                                           }
                                           """));
        assertThat(read.getObject()).isEqualTo(response);
    }

    @Test
    public void testGetChannel() {
        var location = new BlobLocation(BUCKET_NAME, OBJECT_NAME);
        var locations = List.of(location);
        var request = new CreateInitialRevisionHistoryRequest(locations);

        assertThat(request.getChannel()).isEqualTo(CreateInitialRevisionHistoryRequest.CHANNEL);
    }
}