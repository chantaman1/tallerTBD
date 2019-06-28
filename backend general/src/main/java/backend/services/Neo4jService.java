package backend.services;

import backend.Neo4J.Neo4J;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.neo4j.drive.v1.Record;
import org.neo4j.drive.v1.StatementResult;
import org.neo4j.driver.v1.Value;

import java.util.List;
import java.util.ArrayList;

@CrossOrigin
@RestController
@RequestMapping("/neo4j")
public class Neo4JService{
  @Autowired
  Neo4J neo;

  //Soy un metodo
}
