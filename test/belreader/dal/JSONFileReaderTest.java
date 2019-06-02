/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package belreader.dal;

import belreader.be.Order;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marek
 */
public class JSONFileReaderTest
{
    
    public JSONFileReaderTest()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    @Test
    public void testGetOrders() throws IOException, ParseException
    {
        System.out.println("getOrders");
        final String FILE_PATH = "test/belreader/resources/jsonUnitTest.txt";
        
        /************************Setting up the file with json data*********************************/
        //Set up the file
        FileWriter fw = new FileWriter(FILE_PATH); //Create access to the file
        BufferedWriter buffWriter = new BufferedWriter(fw); //Change it to buffer
        
        //read json data into buffer
        InputStream jsonStream = JSONReaderTest.getJsonStream(); //Load stream with json data
        byte [] buffer = new byte[jsonStream.available()]; //Creates buffer for jsonStream
        jsonStream.read(buffer); //Reads content of the json stream inside of the buffer
        
        //Write JSON data into the file
        String text = new String(buffer, "UTF-8"); //Creates String from byte array in encoding UTF-8 
        buffWriter.write(text); //Writes the json data inside the file buffer
        buffWriter.close(); //closes the buffer
        
        /***************************Testing the file reader************************************/
        JSONFileReader instance = new JSONFileReader(FILE_PATH);
        List<Order> expResult = JSONReaderTest.generateOrders();
        List<Order> result = instance.getOrders();
        assertEquals(expResult, result);
    }
    
}
