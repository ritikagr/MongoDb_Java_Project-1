/**
 * Created by Ritik on 19-10-2016.
 */
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.print.Doc;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;
import static com.mongodb.client.model.Filters.*;

import static java.util.Arrays.asList;

public class Main {
    String name,adm,house_no,street,city,state,pincode,mobile,branch;
    MongoClient mongoClient;
    MongoDatabase db;
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("1: Add Student Record");
        System.out.println("2: Search Student Record By Admission No");
        System.out.println("3: Show All Students Record");
        System.out.println("4: Delete Student Record");
        System.out.println("5: Update Student Record");

        System.out.println("Enter Option:");
        int c = sc.nextInt();

        switch (c)
        {
            case 1:
                Main main = new Main();
                main.insertData();
                break;
            case 2:
                new Main().searchStudent();
                break;
            case 3:
                new Main().showAllStudent();
                break;
            case 4:
                break;
            case 5:
                break;
            default:
                break;
        }

    }

    public void insertData()
    {
        mongoClient = new MongoClient();
        db = mongoClient.getDatabase("ritik");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);

        Scanner sc = new Scanner(System.in);
        System.out.println("Name:");
        name = sc.nextLine();
        System.out.println("Admission No.:");
        adm = sc.nextLine();
        System.out.println("Branch:");
        branch = sc.nextLine();
        System.out.println("Mobile:");
        mobile = sc.nextLine();
        System.out.println("House No.:");
        house_no = sc.nextLine();
        System.out.println("Street:");
        street = sc.nextLine();
        System.out.println("City:");
        city = sc.nextLine();
        System.out.println("State:");
        state = sc.nextLine();
        System.out.println("Pincode:");
        pincode = sc.nextLine();

        db.getCollection("student").insertOne(new Document("Name",name).append("Admission_No",adm).append("Branch",branch).
                append("Mobile",mobile).append("Address",new Document("House_No",house_no).append("Street",street).append("City",city).
                append("State",state).append("Pincode",pincode)).append("Created_at", dateFormat.format(new Date())));
    }

    public void searchStudent()
    {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter Admission No.:");
        String adm = sc.nextLine();

        mongoClient = new MongoClient();
        db = mongoClient.getDatabase("ritik");

        FindIterable<Document> iterable = db.getCollection("student").find(eq("Admission_No",adm));

        MongoCursor<Document> cursor = iterable.iterator();

        if(cursor.hasNext())
        {
            Document doc = cursor.next();

            showRecord(doc);
        }
        else
            System.out.println("No Record Exists...");

    }

    public void showAllStudent()
    {
        mongoClient = new MongoClient();
        db = mongoClient.getDatabase("ritik");

        FindIterable<Document> iterable = db.getCollection("student").find();
        MongoCursor<Document> cursor = iterable.iterator();

        /*iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document);
            }
        });*/

        while(cursor.hasNext())
        {
            Document doc = cursor.next();
            showRecord(doc);
        }
    }

    public void showRecord(Document doc)
    {
        Document Address = (Document)doc.get("Address");
        System.out.println("Name : "+doc.getString("Name"));
        System.out.println("Admission No : "+doc.getString("Admission_No"));
        System.out.println("Branch : "+doc.getString("Branch"));
        System.out.println("Address : "+ Address.getString("House_No")+" , "+
                Address.getString("Street")+" , "+Address.getString("City")+" , "+Address.getString("State"));
        System.out.println("Pincode : "+ Address.getString("Pincode"));
        System.out.println("Created At : "+doc.getString("Created_at"));
        System.out.println("Mobile : "+doc.getString("Mobile"));
        System.out.println();
    }
}
