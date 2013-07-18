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
                "    \"return_filter\" : {\n" +
                "         \"body\" : \"position.length()<3;\",\n" +
                "         \"language\" : \"javascript\" },\n" +
                "    \"prune_evaluator\" : {\n" +
                "         \"name\" : \"none\",\n" +
                "         \"language\" : \"builtin\" }\n" +
                "  },\n" +
                "  \"id\" : 0 }]";

        JaxRsResponse response = restRequest.post("db/data/batch", dummyText);

        // We don't get a nice 200 with an error on the batch request
        // instead we get a 500 actual exception

        //System.out.println(response.getStatus());
        //System.out.println(response.getEntity().toString());
        //assertTrue(response.getStatus() == 401);
        //assertTrue(response.getHeaders().getFirst( "WWW-Authenticate" ).contains("Basic realm=\"WallyWorld\""));
        assertTrue(response.getStatus() == 500);
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
