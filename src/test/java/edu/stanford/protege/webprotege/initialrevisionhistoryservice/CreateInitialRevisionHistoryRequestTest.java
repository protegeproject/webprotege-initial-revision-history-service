package edu.stanford.protege.webprotege.initialrevisionhistoryservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import edu.stanford.protege.webprotege.common.BlobLocation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;

import java.io.StringReader;
import java.util.List;

public class CreateInitialRevisionHistoryRequestTest {

    protected static final String BUCKET_NAME = "bucket";

    protected static final String OBJECT_NAME = "name";

    private JacksonTester<CreateInitialRevisionHistoryRequest> json;

    private CreateInitialRevisionHistoryRequest request;

    @BeforeEach
    public void setup() {
        var objectMapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule());
        JacksonTester.initFields(this, objectMapper);

        var location = new BlobLocation(BUCKET_NAME, OBJECT_NAME);
        var locations = List.of(location);
        request = new CreateInitialRevisionHistoryRequest(locations);
    }

    @Test
    public void testSerialization() throws Exception {
        var written = json.write(request);
        assertThat(written).hasJsonPathStringValue("$.documentLocations[0].bucket", BUCKET_NAME);
        assertThat(written).hasJsonPathStringValue("$.documentLocations[0].name", OBJECT_NAME);
    }

    @Test
    public void testDeserialization() throws Exception {
        var read = json.read(new StringReader("""
                                           {
                                                "documentLocations" : [
                                                    {
                                                        "bucket" : "bucket",
                                                        "name" : "name"
                                                    }
                                                ]
                                           }
                                           """));
        assertThat(read.getObject()).isEqualTo(request);
    }

    @Test
    public void testGetChannel() {
        var location = new BlobLocation(BUCKET_NAME, OBJECT_NAME);
        var locations = List.of(location);
        var request = new CreateInitialRevisionHistoryRequest(locations);

        assertThat(request.getChannel()).isEqualTo(CreateInitialRevisionHistoryRequest.CHANNEL);
    }
}
