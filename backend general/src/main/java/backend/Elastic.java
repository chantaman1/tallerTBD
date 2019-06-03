package backend;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.index.*;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.elasticsearch.search.DocValueFormat;
import org.joda.time.DateTime;

import javax.print.Doc;

public class Elastic{
    private ArrayList<Integer> resultList;
    private int positiveResult;
    private int negativeResult;
    private ArrayList<Integer> resultListGeneral;
    private int positiveResultGeneral;
    private int negativeResultGeneral;

    public void indexCreate() {
        try {
            Directory dir = FSDirectory.open(Paths.get("indice/"));
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            iwc.setOpenMode(OpenMode.CREATE);
            IndexWriter writer = new IndexWriter(dir, iwc);
            
            //Mongo connection
            
            MongoConnection mongo = MongoConnection.getMongo();
            mongo.OpenMongoClient();
            DBCursor cursor = mongo.getTweets();
            Document doc = null;
            Document user = null;

            while (cursor.hasNext()) {
                DBObject cur = cursor.next();
                doc = new Document();
                Date date = (Date) cur.get("createdAt");
                String auxiliar = DateTools.dateToString(date, DateTools.Resolution.DAY);
                doc.add(new StringField("id", cur.get("_id").toString(), Field.Store.YES));
                doc.add(new TextField("text", cur.get("text").toString(), Field.Store.YES));
                doc.add(new TextField("date", auxiliar, Field.Store.YES));
                doc.add(new StringField("latitude", cur.get("latitude").toString(), Field.Store.YES));
                doc.add(new StringField("longitude", cur.get("longitude").toString(), Field.Store.YES));
                doc.add(new StringField("city", cur.get("city").toString(), Field.Store.YES));
                doc.add(new StringField("country", cur.get("country").toString(), Field.Store.YES));
                doc.add(new TextField("userName", cur.get("userName").toString(), Field.Store.YES));
                doc.add(new TextField("followersCount", cur.get("followersCount").toString(), Field.Store.YES));
                doc.add(new TextField("sentimentAnalysis", cur.get("sentimentAnalysis").toString(), Field.Store.YES));

                if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
                    System.out.println("Usuario del tweet: " + doc.get("userName"));
                    System.out.println("Indexando el tweet: " + doc.get("text"));
                    System.out.println("Fecha del tweet :" + doc.get("date"));
                    System.out.println("Análisis del tweet :" + doc.get("sentimentAnalysis"));
                    System.out.println("Followers :" + doc.get("followersCount") + "\n");
                    writer.addDocument(doc);
                    // System.out.println(doc);
                } else {
                    System.out.println("Actualizando el tweet: " + doc.get("text") + "\n");
                    writer.updateDocument(new Term("text" + cur.get("text")), doc);
                    // System.out.println(doc);
                }
            }
            cursor.close();
            writer.close();
        } catch (IOException ioe) {
            System.out.println(" Error en " + ioe.getClass() + "\n mensaje: " + ioe.getMessage());
        }
    }
}



























