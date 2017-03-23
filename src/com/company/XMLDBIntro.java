package com.company;

import org.exist.storage.btree.DBException;
import org.exist.storage.index.CollectionStore;
import org.exist.xmldb.CollectionImpl;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XQueryService;

import java.io.File;


public class XMLDBIntro {

    private static String URI = "xmldb:exist://localhost:8080/exist/xmlrpc";
    private static String driver = "org.exist.xmldb.DatabaseImpl";

    public static void main(String args[]) throws XMLDBException,
            ClassNotFoundException, IllegalAccessException, InstantiationException{

        try {
            afegirFitxer("Pokemons.xml");
        } catch (DBException e) {
            e.printStackTrace();
        }

    }

    private static void afegirFitxer(String fl) throws XMLDBException,
            ClassNotFoundException, IllegalAccessException, InstantiationException, DBException {
        File f = new File("/home/46406163y/Baixades/Factbook.xml");

        // initialize database driver
        Class cl = Class.forName(driver);
        Database database = (Database) cl.newInstance();
        database.setProperty("create-database", "true");

        // crear el manegador
        DatabaseManager.registerDatabase(database);

        // adquirir la col·lecció que volem tractar
        Collection col = DatabaseManager.getCollection(URI+"/db","admin","admin");
        Collection col2 = DatabaseManager.getCollection(URI+"/db","admin","admin");

        //Creem la col·lecció on guardarem el recurs
        CollectionManagementService colmgt = (CollectionManagementService) col.getService("CollectionManagementService", "1.0");

        col = colmgt.createCollection("Recursos");

        //afegir el recurs que farem servir
        Resource res = col.createResource("pokemons.xml","XMLResource");
        res.setContent(f);
        col.storeResource(res);

        Resource res2 = col.getResource("pokemons.xml");


        System.out.println( res2.getContent() );

    }
}
