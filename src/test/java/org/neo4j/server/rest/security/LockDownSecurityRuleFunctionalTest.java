package org.neo4j.server.rest.security;

import org.neo4j.server.NeoServer;
import org.neo4j.server.helpers.ServerBuilder;
import org.neo4j.server.rest.JaxRsResponse;
import org.neo4j.server.rest.RestRequest;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class LockDownSecurityRuleFunctionalTest {

    @Test
    public void should401WithBasicChallengeWhenASecurityRuleFails()
            throws Exception
    {
        NeoServer server = ServerBuilder.server()
                .withSecurityRules(LockDownSecurityRule.class.getCanonicalName() ).build();
        server.start();

        RestRequest restRequest = new RestRequest(server.baseUri());
        String dummyText = "";
        JaxRsResponse response = restRequest.post("db/data/node/0/traverse/node", dummyText);
        assertTrue(response.getStatus() == 401);
        assertTrue(response.getHeaders().getFirst( "WWW-Authenticate" ).contains("Basic realm=\"WallyWorld\""));
        server.stop();
    }


    @Test
    public void should401WithBasicChallengeWhenASecurityRuleFailswithBatch()
            throws Exception
    {
        NeoServer server = ServerBuilder.server()
                .withSecurityRules(LockDownSecurityRule.class.getCanonicalName() ).build();
        server.start();

        RestRequest restRequest = new RestRequest(server.baseUri());
        String dummyText = "[ {\n" +
                "  \"method\" : \"POST\",\n" +
                "  \"to\" : \"/node/0/traverse\",\n" +
                "  \"body\" : {\n" +
                "    \"order\" : \"breadth_first\"\n" +
                "  },\n" +
                "  \"id\" : 0";
        JaxRsResponse response = restRequest.post("db/data/batch", dummyText);
        assertTrue(response.getStatus() == 401);
        assertTrue(response.getHeaders().getFirst( "WWW-Authenticate" ).contains("Basic realm=\"WallyWorld\""));
        server.stop();
    }

    @Test
    public void should200WithBasicChallengeWhenASecurityRuleSucceeds()
            throws Exception
    {
        NeoServer server = ServerBuilder.server()
                .withSecurityRules(LockDownSecurityRule.class.getCanonicalName() ).build();
        server.start();

        RestRequest restRequest = new RestRequest(server.baseUri());

        JaxRsResponse response = restRequest.get("db/data/node/0");
        assertTrue(response.getStatus() == 200);
        server.stop();
    }

}
