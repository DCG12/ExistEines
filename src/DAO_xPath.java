

import org.basex.api.client.ClientQuery;
import org.basex.api.client.ClientSession;
import org.basex.core.cmd.CreateDB;
import org.exist.storage.btree.DBException;
import org.exist.storage.index.CollectionStore;
import org.exist.xmldb.CollectionImpl;
import org.exist.xquery.XQuery;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XQueryService;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class DAO_xPath {

    private static Scanner sc = new Scanner(System.in);

    private static String URI = "xmldb:exist://localhost:8080/exist/xmlrpc";
    private static String driver = "org.exist.xmldb.DatabaseImpl";
    private static String colleccion = "";
    private static String archivo = "";
    private static String ruta = "";

    public static void main(String args[]) throws XMLDBException,
            ClassNotFoundException, IllegalAccessException, InstantiationException, IOException, XQException {

        //createCollection();
        //deleteCollection();
        //createResource();
        //deleteResource();
        consultas();
    }


    private static void SetCollection() {
        System.out.println("Nombre de la col·lección que quieres crear");
        colleccion = sc.nextLine();
    }

    private static void SetResource() {
        System.out.println("Nombre del resource que quieres añadir");
        archivo = sc.nextLine();
    }

    private static void SetRutaResource() {
        System.out.println("Ruta del resource que quieres añadir");
        ruta = sc.nextLine();
    }

    public static void createCollection() throws ClassNotFoundException, XMLDBException, IllegalAccessException, InstantiationException {

        SetCollection();

        Class cl = Class.forName(driver);
        Database database = (Database) cl.newInstance();
        database.setProperty("create-database", "true");

        DatabaseManager.registerDatabase(database);
        Collection col = DatabaseManager.getCollection(URI + "/db", "admin", "admin");

        //Creem la col·lecció on guardarem el recurs
        CollectionManagementService colmgt = (CollectionManagementService) col.getService("CollectionManagementService", "1.0");
        //l'hi donem un nom a la nova col·lecció
        col = colmgt.createCollection(colleccion);
    }

    public static void deleteCollection() throws ClassNotFoundException, XMLDBException, IllegalAccessException, InstantiationException {

        Class cl = Class.forName(driver);
        Database database = (Database) cl.newInstance();
        database.setProperty("create-database", "true");

        DatabaseManager.registerDatabase(database);
        Collection col = DatabaseManager.getCollection(URI + "/db", "admin", "admin");

        //Creem la col·lecció on guardarem el recurs
        CollectionManagementService colmgt = (CollectionManagementService) col.getService("CollectionManagementService", "1.0");
        //l'hi donem un nom a la nova col·lecció
        colmgt.removeCollection(colleccion);
    }

    public static void createResource() throws XMLDBException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        SetResource();
        SetRutaResource();

        File f = new File(ruta);

        // initialize database driver
        Class cl = Class.forName(driver);
        Database database = (Database) cl.newInstance();
        database.setProperty("create-database", "true");

        // crear el manegador
        DatabaseManager.registerDatabase(database);

        // adquirir la col·lecció que volem tractar
        Collection col = DatabaseManager.getCollection(URI + "/db/"+ colleccion +"/", "admin", "admin");

        //afegir el recurs que farem servir
        Resource res = col.createResource(archivo, "XMLResource");
        res.setContent(f);
        col.storeResource(res);
    }

    public static void deleteResource() throws XMLDBException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        // initialize database driver
        Class cl = Class.forName(driver);
        Database database = (Database) cl.newInstance();
        database.setProperty("create-database", "true");

        // crear el manegador
        DatabaseManager.registerDatabase(database);

        // adquirir la col·lecció que volem tractar
        Collection col = DatabaseManager.getCollection(URI + "/db/"+ colleccion +"/", "admin", "admin");

        Resource res2 = col.getResource(archivo);

        col.removeResource(res2);
    }

    public static void consultas() throws ClassNotFoundException, XMLDBException, IllegalAccessException, InstantiationException, IOException, XQException {

            // initialize database driver
            Class cl = Class.forName(driver);
            Database database = (Database) cl.newInstance();
            database.setProperty("create-database", "true");

            // crear el manegador
            DatabaseManager.registerDatabase(database);

            // adquirir la col·lecció que volem tractar
            Collection col = DatabaseManager.getCollection(URI+"/db/","admin","admin");

            //Creem la col·lecció on guardarem el recurs
            CollectionManagementService colmgt = (CollectionManagementService) col.getService("CollectionManagementService", "1.0");

            //Creamos un nuevo recurso vacio dentro del programa y le damos el recurso pokemons.xml que esta dentro de la col·lección
            Resource res2 = col.getResource("mondial.xml");

        String xQuery = "for $x in doc( name )." + "return data($x).";

        XQueryService service = (XQueryService) col.getService("XQueryService", "1.0");
        service.setProperty("indent", "yes");
        ResourceSet result = service.query(xQuery);
        ResourceIterator i =  result.getIterator();
        while(i.hasMoreResources()){
            Resource r = i.nextResource();
            String Value = (String) r.getContent();
            System.out.println(Value);
        }


        }
}

