package org.neo4j.server.rest.security;

import javax.servlet.http.HttpServletRequest;

public class LockDownSecurityRule implements SecurityRule {

    public static final String REALM = "WallyWorld"; // as per RFC2617 :-);

    @Override
    public boolean isAuthorized( HttpServletRequest request)
    {
        // always fails - a production implementation performs
        return false;
    }

    @Override
    public String forUriPath()
    {
        return "/db/data/node/*/traverse/*";
    }

    @Override
    public String wwwAuthenticateHeader()
    {
        return SecurityFilter.basicAuthenticationResponse(REALM);
    }

}
