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

import backend.models.ListaPalabra;
import backend.services.ListaPalabraService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.print.Doc;

@Component
public class Elastic{
    private ArrayList<Integer> resultList;
    private int positiveResult;
    private int negativeResult;
    private ArrayList<Integer> resultListGeneral;
    private int positiveResultGeneral;
    private int negativeResultGeneral;
    @Autowired
    ListaPalabraService listaPalabraService;

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
            int amountOfTweets = 0;

            while (cursor.hasNext()) {
                amountOfTweets++;
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
            System.out.println("Cantidad de tweets: " + amountOfTweets);
            cursor.close();
            writer.close();
        } catch (IOException ioe) {
            System.out.println(" Error en " + ioe.getClass() + "\n mensaje: " + ioe.getMessage());
        }
    }

    private int getQuantity(String genre){
            int total = 0;

            try {
                IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("indice/")));
                IndexSearcher searcher = new IndexSearcher(reader);
                Analyzer analyzer = new StandardAnalyzer();
                QueryParser parser = new QueryParser("text", analyzer);
                Query query = parser.parse(genre);
                TopDocs result = searcher.search(query, 560000);
                ScoreDoc[] hits = result.scoreDocs;
                total = hits.length;

                reader.close();
            } catch(IOException | ParseException ex) {
                Logger.getLogger(Elastic.class.getName()).log(Level.SEVERE,null,ex);
            }

            return total;
    }

    private int getSentimentAnalysis(String sentiment){
        int total = 0;

        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("indice/")));
            IndexSearcher searcher = new IndexSearcher(reader);
            Analyzer analyzer = new StandardAnalyzer();
            QueryParser parser = new QueryParser("sentimentAnalysis", analyzer);
            Query query = parser.parse(sentiment);
            TopDocs result = searcher.search(query, 550000);
            ScoreDoc[] hits = result.scoreDocs;
            total = hits.length;

            reader.close();
        } catch(IOException | ParseException ex) {
            Logger.getLogger(Elastic.class.getName()).log(Level.SEVERE,null,ex);
        }

        return total;
    }

    public int getGenreAndSentiment(String sentiment, String genre){
        int total = 0;
        String[] strSplit = genre.split(" ");
        String textQuery = "";
        if(strSplit.length > 1){
            for(int i = 0; i < strSplit.length ; i++) {
                if(i + 1 == strSplit.length){
                    textQuery = textQuery + strSplit[i];
                }
                else {
                    textQuery = textQuery + strSplit[i] + "\\ ";
                }
            }
        }
        else{
            textQuery = genre;
        }
        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("indice/")));
            IndexSearcher searcher = new IndexSearcher(reader);
            Analyzer analyzer = new StandardAnalyzer();
            QueryParser parser = new QueryParser("<default field>", analyzer);
            String special = "text:" + textQuery + " AND sentimentAnalysis:" + sentiment;
            TopDocs result = searcher.search(parser.parse(special), 550000);
            ScoreDoc[] hits = result.scoreDocs;
            total = hits.length;

            reader.close();
        } catch(IOException | ParseException ex) {
            Logger.getLogger(Elastic.class.getName()).log(Level.SEVERE,null,ex);
        }

        return total;
    }

    public int getGenreAndSentimentBetweenDates(String sentiment, String genre, String startDate, String endDate){
        int total = 0;
        String[] strSplit = genre.split(" ");
        String textQuery = "";
        if(strSplit.length > 1){
            for(int i = 0; i < strSplit.length ; i++) {
                if(i + 1 == strSplit.length){
                    textQuery = textQuery + strSplit[i];
                }
                else {
                    textQuery = textQuery + strSplit[i] + "\\ ";
                }
            }
        }
        else{
            textQuery = genre;
        }
        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("indice/")));
            IndexSearcher searcher = new IndexSearcher(reader);
            Analyzer analyzer = new StandardAnalyzer();
            QueryParser parser = new QueryParser("<default field>", analyzer);
            String special = "date:[" + startDate + " TO " + endDate + "] AND text:" + textQuery + " AND sentimentAnalysis:" + sentiment;
            TopDocs result = searcher.search(parser.parse(special), 500000);
            ScoreDoc[] hits = result.scoreDocs;
            total = hits.length;

            reader.close();
        } catch(IOException | ParseException ex) {
            Logger.getLogger(Elastic.class.getName()).log(Level.SEVERE,null,ex);
        }

        return total;
    }

    public int getGenreAndSentimentByDate(String sentiment, String genre, String date){
        int total = 0;
        String[] strSplit = genre.split(" ");
        String textQuery = "";
        if(strSplit.length > 1){
            for(int i = 0; i < strSplit.length ; i++) {
                if(i + 1 == strSplit.length){
                    textQuery = textQuery + strSplit[i];
                }
                else {
                    textQuery = textQuery + strSplit[i] + "\\ ";
                }
            }
        }
        else{
            textQuery = genre;
        }
        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("indice/")));
            IndexSearcher searcher = new IndexSearcher(reader);
            Analyzer analyzer = new StandardAnalyzer();
            QueryParser parser = new QueryParser("<default field>", analyzer);
            String special = "date:" + date + " AND text:" + textQuery + " AND sentimentAnalysis:" + sentiment;
            TopDocs result = searcher.search(parser.parse(special), 520000);
            ScoreDoc[] hits = result.scoreDocs;
            total = hits.length;

            reader.close();
        } catch(IOException | ParseException ex) {
            Logger.getLogger(Elastic.class.getName()).log(Level.SEVERE,null,ex);
        }

        return total;
    }

    public void countAllKeywordAndSentiments(){
        //List<ListaPalabra> palabras = listaPalabraService.getAllPalabras();
        List<String> palabras = new ArrayList<>();
        palabras.add("rock");
        palabras.add("pop");
        palabras.add("metal");
        palabras.add("reggaeton");
        for(String palabra : palabras){
            int totalPositivo = getGenreAndSentiment("positive", palabra);
            int totalNegativo = getGenreAndSentiment("negative", palabra);
            System.out.println("Genero: " + palabra + " Total Positivos: " + totalPositivo + " Total negativos: " + totalNegativo);
        }
    }

    public void countAllByKeywords(){
        List<ListaPalabra> palabras = listaPalabraService.getAllPalabras();
        for(ListaPalabra palabra : palabras){
            int cantidad = getQuantity(palabra.getPalabra());
        }

    }

    public void countAllSentiments(){
        int negative = getSentimentAnalysis("negative");
        int positive = getSentimentAnalysis("positive");
    }
}
