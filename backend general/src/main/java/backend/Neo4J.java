package backend;

import org.neo4j.driver.v1.*;

public class Neo4J {
    private Driver driver;
    private Session session;

    public void connect(String url, String username, String password){
        this.driver = GraphDatabase.driver(url, AuthTokens.basic(username, password));
        this.session = driver.session();
    }

    public void disconnect(){
        this.session.close();
        this.driver.close();
    }

    public void deleteAll(){
        this.session.run("match (a)-[r]->(b) delete r");
        this.session.run("match (n) delete n");
    }


}
