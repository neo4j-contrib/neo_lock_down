neo_lock_down
=============

Security Rule for Neo4j that disables Traversal REST API

To Create jar:

    mvn package

To Install:

    cp target/neo_lock_down-1.0.jar neo4j/plugins


In neo4j-server.properties:

    org.neo4j.server.rest.security_rules=org.neo4j.server.rest.security.LockDownSecurityRule