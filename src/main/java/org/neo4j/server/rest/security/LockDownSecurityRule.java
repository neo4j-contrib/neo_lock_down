package org.neo4j.server.rest.security;

import javax.servlet.http.HttpServletRequest;

public class LockDownSecurityRule implements SecurityRule {

    public static final String REALM = "WallyWorld"; // as per RFC2617 :-);

    @Override
    public boolean isAuthorized( HttpServletRequest request)
    {
        return false;
    }

    @Override
    public String forUriPath()
    {
        return "*node/*/traverse*";
    }

    @Override
    public String wwwAuthenticateHeader()
    {
        return SecurityFilter.basicAuthenticationResponse(REALM);
    }

}
