/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package belreader.dal;

import belreader.be.Order;
import belreader.be.Task;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marek
 */
public class JSONReaderTest
{      
    public JSONReaderTest()
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
    public void testGetOrders() throws ParseException
    {
        System.out.println("getOrders");
        
        InputStream stream = getJsonStream();
        JSONReader instance = new JSONReader(stream);
        List<Order> expResult = generateOrders();
        List<Order> result = instance.getOrders();
        
        assertEquals(expResult, result);
    }
    
    @Test
    public void testEpochDateToDate()
    {
        System.out.println("Testing epochDateToDate");

        /*************************Testing some correct date**************************/
        long epoch = 1556178862000L;
        Date expectedDate = new Date(epoch);
        Date result = JSONReader.epochDateToDate("1556178862266+0200");     
        assertEquals("Epoch date parser test failed",expectedDate,result); 
        
        //still testing correct date but with a little upgrade
        result = JSONReader.epochDateToDate("m1a5j5k6a1j7e8l8ep6sia2ako2leverpostej66+0200");     
        assertEquals("Epoch date parser test failed",expectedDate,result); 
        
        /*************************Testing some minimum date**************************/
        epoch = 0L;
        expectedDate = new Date(epoch);
        result = JSONReader.epochDateToDate("0000000000000+0200");
        assertEquals("Epoch date parser test failed",expectedDate,result);
        
        /*************************Testing invalid string**************************/
        epoch = 0L;
        expectedDate = new Date(epoch);
        result = JSONReader.epochDateToDate("zebysomtupisalkravinyanikomutonevadilo?");
        assertEquals("Epoch date parser test failed",expectedDate,result);
       
        /*************************Testing a little less invalid string**************************/
        epoch = 0L;
        expectedDate = new Date(epoch);
        result = JSONReader.epochDateToDate("12345uz to vieme na spamat");
        assertEquals("Epoch date parser test failed",expectedDate,result);
        
    }
    
    /**
     * 
     * @return List of orders that should match getJsonStream data
     * @throws ParseException 
     */
    protected static List<Order> generateOrders() throws ParseException
    { 
        final int ORDERS_COUNT = 3;
        List<Order> orders = new ArrayList<>(); //instance that will be returned
        
        /***************************Setting up data for a creation of the orders********************/
        Long [] orderDateEpoch = {1558870888000L,1558328504000L,1559972418000L};        //deliveryDates for orders in epoch represantation
        String [] orderNums = {"8399-95778","6118-58952","4307-30526"};
        String [] customerNames = {"Jysk","Maersk","Q8"};
        String [][] departmentNames = 
        {
            {"Halvfab"},
            {"Halvfab"},
            {"Halvfab","Balg","Montage 1","Montage 2","Bertel"}
        };
        Long [][] taskStartEpoch = 
        {
            {1556178862000L},
            {1556178862000L},
            {1556178862000L,1556937573000L,1557696284000L,1558454996000L,1559213707000L}
        };
        Long [][] taskEndEpoch = 
        {
            {1558870888000L},
            {1558328504000L},
            {1556937573000L,1557696284000L,1558454996000L,1559213707000L,1559972418000L}
        };
        Boolean [][] taskStates = 
        {
            {false},
            {false},
            {false,false,false,false,false}
        };
        
        /******************************Checking if the sizes of the fields are correct*************************************/
        assert(orderDateEpoch.length == ORDERS_COUNT && orderNums.length == ORDERS_COUNT && customerNames.length == ORDERS_COUNT);
        assert(taskStartEpoch.length == ORDERS_COUNT && taskEndEpoch.length == ORDERS_COUNT && taskStates.length == ORDERS_COUNT && departmentNames.length == ORDERS_COUNT);
        assert(taskStartEpoch[2].length == 5 && taskEndEpoch[2].length == 5 && taskStates[2].length == 5 && departmentNames[2].length == 5);
        
        /*****************************Creating orders from the Fields*************************/
        for(int i = 0; i < ORDERS_COUNT; i++)
        {
            List<Task> tasks = new ArrayList();
            for(int t = 0; t < departmentNames[i].length; t++)
            {
                tasks.add(new Task(new Date(taskStartEpoch[i][t]),new Date(taskEndEpoch[i][t]),departmentNames[i][t],taskStates[i][t]));
            }
            Order order = new Order(orderNums[i], customerNames[i], new Date(orderDateEpoch[i]), tasks);
            orders.add(order);
        }
        return orders;
    }
    
    /**
     * 
     * @return InputStream of jsonData that should match generateOrders data
     */
    protected static InputStream getJsonStream()
    {
        byte[] bytes = "{\"ProductionOrders\":[{\"__type\":\"ProductionOrder:#ProductionMonitor\",\"Customer\":{\"__type\":\"Customer:#ProductionMonitor\",\"Name\":\"Jysk\"},\"Delivery\":{\"__type\":\"Delivery:#ProductionMonitor\",\"DeliveryTime\":\"\\/Date(1558870888940+0200)\\/\"},\"DepartmentTasks\":[{\"__type\":\"DepartmentTask:#ProductionMonitor\",\"Department\":{\"__type\":\"Department:#ProductionMonitor\",\"Name\":\"Halvfab\"},\"EndDate\":\"\\/Date(1558870888940+0200)\\/\",\"FinishedOrder\":false,\"StartDate\":\"\\/Date(1556178862266+0200)\\/\"}],\"Order\":{\"__type\":\"Order:#ProductionMonitor\",\"OrderNumber\":\"8399-95778\"}},{\"__type\":\"ProductionOrder:#ProductionMonitor\",\"Customer\":{\"__type\":\"Customer:#ProductionMonitor\",\"Name\":\"Maersk\"},\"Delivery\":{\"__type\":\"Delivery:#ProductionMonitor\",\"DeliveryTime\":\"\\/Date(1558328504270+0200)\\/\"},\"DepartmentTasks\":[{\"__type\":\"DepartmentTask:#ProductionMonitor\",\"Department\":{\"__type\":\"Department:#ProductionMonitor\",\"Name\":\"Halvfab\"},\"EndDate\":\"\\/Date(1558328504270+0200)\\/\",\"FinishedOrder\":false,\"StartDate\":\"\\/Date(1556178862266+0200)\\/\"}],\"Order\":{\"__type\":\"Order:#ProductionMonitor\",\"OrderNumber\":\"6118-58952\"}},{\"__type\":\"ProductionOrder:#ProductionMonitor\",\"Customer\":{\"__type\":\"Customer:#ProductionMonitor\",\"Name\":\"Q8\"},\"Delivery\":{\"__type\":\"Delivery:#ProductionMonitor\",\"DeliveryTime\":\"\\/Date(1559972418655+0200)\\/\"},\"DepartmentTasks\":[{\"__type\":\"DepartmentTask:#ProductionMonitor\",\"Department\":{\"__type\":\"Department:#ProductionMonitor\",\"Name\":\"Halvfab\"},\"EndDate\":\"\\/Date(1556937573543+0200)\\/\",\"FinishedOrder\":false,\"StartDate\":\"\\/Date(1556178862266+0200)\\/\"},{\"__type\":\"DepartmentTask:#ProductionMonitor\",\"Department\":{\"__type\":\"Department:#ProductionMonitor\",\"Name\":\"Balg\"},\"EndDate\":\"\\/Date(1557696284821+0200)\\/\",\"FinishedOrder\":false,\"StartDate\":\"\\/Date(1556937573543+0200)\\/\"},{\"__type\":\"DepartmentTask:#ProductionMonitor\",\"Department\":{\"__type\":\"Department:#ProductionMonitor\",\"Name\":\"Montage 1\"},\"EndDate\":\"\\/Date(1558454996099+0200)\\/\",\"FinishedOrder\":false,\"StartDate\":\"\\/Date(1557696284821+0200)\\/\"},{\"__type\":\"DepartmentTask:#ProductionMonitor\",\"Department\":{\"__type\":\"Department:#ProductionMonitor\",\"Name\":\"Montage 2\"},\"EndDate\":\"\\/Date(1559213707377+0200)\\/\",\"FinishedOrder\":false,\"StartDate\":\"\\/Date(1558454996099+0200)\\/\"},{\"__type\":\"DepartmentTask:#ProductionMonitor\",\"Department\":{\"__type\":\"Department:#ProductionMonitor\",\"Name\":\"Bertel\"},\"EndDate\":\"\\/Date(1559972418655+0200)\\/\",\"FinishedOrder\":false,\"StartDate\":\"\\/Date(1559213707377+0200)\\/\"}],\"Order\":{\"__type\":\"Order:#ProductionMonitor\",\"OrderNumber\":\"4307-30526\"}}]}".getBytes();
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        return is;
    }

    
}
