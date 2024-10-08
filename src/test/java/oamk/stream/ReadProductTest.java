package oamk.stream;

import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.LinkedList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReadProductTest {

    private static MethodHandle getProductsHandle;
    private static MethodHandle readCSVHandle;
    private static Constructor<?> rConstructor;
    private static Constructor<?> pConstructor;

    private static ByteArrayOutputStream errContent;

    @BeforeEach
    public void setUp() throws Throwable {

        errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));
    
        try {
            // test record product
            Class<?> pclazz = Class.forName("oamk.stream.Product");
            if (pclazz.isRecord() == false) {
                assertTrue(false, "Product class must be a record");
            }
            if (OutputFormatter.class.isAssignableFrom(pclazz) == false) {
                assertTrue(false, "Record Product does not implement interface OutputFormatter");
            }
            // String name, Double price, String category, LocalDateTime dt
            pConstructor = pclazz.getConstructor(String.class, Double.class, String.class, LocalDateTime.class);

        } catch (Exception e) {
            assertTrue(false, "Class Product declaration is incorrect");
        }

        try {
            // test readproductfile class
            Class<?> rclazz = Class.forName("oamk.stream.ReadProductFile");
            rConstructor = rclazz.getConstructor(String.class);

            MethodType type = MethodType.methodType(List.class);
            getProductsHandle = MethodHandles.lookup().findVirtual(rclazz, "getProducts", type);            
            type = MethodType.methodType(void.class);
            readCSVHandle = MethodHandles.lookup().findVirtual(rclazz, "readCSV", type);            
        } catch (Exception e) {
            assertTrue(false, "Interface OutputFormatter declaration is incorrect");
        }
    }

    @AfterAll
    public static void cleanUp() {
        errContent = null;
    }

    @Test
    @DisplayName("Test trying to read non-existing CSV file")
    public void testConstructorFail() throws Throwable {
        Boolean fails = false;
        try {
            Object[] initargs = { "asdasd.csv" };
            Object r = rConstructor.newInstance(initargs);
        } catch (InvocationTargetException e) {
            Throwable target = e.getTargetException();
            if (target.getClass().equals(new IllegalArgumentException().getClass())) {
                fails = true;
            }
        } finally {
            if (fails == false) {
                assertTrue(fails, "Trying to test exception when file does exist");
            }
        }
    }

    @Test
    @DisplayName("Test reading CSV file: field 1 missing")
    public void testReadCSVField1() throws Throwable {
        Object[] initargs = { "src/test/test1.csv" };
        Object r = rConstructor.newInstance(initargs);
        readCSVHandle.invoke((ReadProductFile)r);
        Object[] p1args = { "shirt", 12.99, "clothes", LocalDateTime.of(2024, 11, 5,19,46,0) };
        Object p1 = pConstructor.newInstance(p1args);
        List<Product> expected = new LinkedList<Product>();
        expected.add((Product)p1);
        List<Product> actual = (List<Product>) getProductsHandle.invoke(r);
        assertEquals(expected,actual,"File test1.csv was not correctly read");
        String expected2 = "oamk.stream.ReadProductFile$CSVMissingParameterException: Error in test1.csv, skipped line 2: missing parameter";
        assertTrue(errContent.toString().contains(expected2), "Method did not catch the missing parameter in line 2");
    }

    @Test
    @DisplayName("Test reading CSV file: field 2 missing")
    public void testReadCSVField2() throws Throwable {
        Object[] initargs = { "src/test/test2.csv" };
        Object r = rConstructor.newInstance(initargs);
        readCSVHandle.invoke((ReadProductFile)r);
        List<Product> expected = new LinkedList<Product>();
        List<Product> actual = (List<Product>) getProductsHandle.invoke((ReadProductFile)r);
        assertEquals(expected,actual,"File test2.csv was not correctly read");
        String expected2 = "oamk.stream.ReadProductFile$CSVMissingParameterException: Error in test2.csv, skipped line 1: missing parameter";
        assertTrue(errContent.toString().contains(expected2), "Method did not catch the missing parameter in line 1");
    }

    @Test
    @DisplayName("Test reading CSV file: field 3 missing")
    public void testReadCSVField3() throws Throwable {
        Object[] initargs = { "src/test/test3.csv" };
        Object r = rConstructor.newInstance(initargs);
        readCSVHandle.invoke(r);
        List<Product> expected = new LinkedList<Product>();
        // icecream,3.49,food,2024-09-28T19:46:00.000
        // icecream,3.49,food,2024-09-28T19:47:00.000
        Object[] p1args = { "icecream", 3.49, "food", LocalDateTime.of(2024, 9, 28,19,46,0) };
        Object p1 = (Product) pConstructor.newInstance(p1args);
        Object[] p2args = { "icecream", 3.49, "food", LocalDateTime.of(2024, 9, 28,19,47,0) };
        Object p2 = (Product) pConstructor.newInstance(p2args);
        expected.add((Product)p1);
        expected.add((Product)p2);
        List<Product> actual = (List<Product>) getProductsHandle.invoke((ReadProductFile)r);
        assertEquals(expected,actual,"File test3.csv was not correctly read");
        String expected2 = "oamk.stream.ReadProductFile$CSVMissingParameterException: Error in test3.csv, skipped line 3: missing parameter";
        assertTrue(errContent.toString().contains(expected2), "Method did not catch the missing parameter in line 3");
    }

    @Test
    @DisplayName("Test reading CSV file: field 4 missing")
    public void testReadCSVField4() throws Throwable {
        Object[] initargs = { "src/test/test4.csv" };
        Object r = rConstructor.newInstance(initargs);
        readCSVHandle.invoke(r);
        List<Product> expected = new LinkedList<Product>();
        // icecream,3.49,food,2024-09-28T19:46:00.000
        // icecream,3.49,food,2024-09-28T19:47:00.000
        // icecream,3.49,food
        // icecream,3.49,food,2024-09-28T19:49:00.000
        Object[] p1args = { "icecream", 3.49, "food", LocalDateTime.of(2024, 9, 28,19,46,0) };
        Object p1 = (Product) pConstructor.newInstance(p1args);
        Object[] p2args = { "icecream", 3.49, "food", LocalDateTime.of(2024, 9, 28,19,47,0) };
        Object p2 = (Product) pConstructor.newInstance(p2args);
        Object[] p3args = { "icecream", 3.49, "food", LocalDateTime.of(2024, 9, 28,19,49,0) };
        Object p3 = (Product) pConstructor.newInstance(p3args);
        expected.add((Product)p1);
        expected.add((Product)p2);
        expected.add((Product)p3);
        List<Product> actual = (List<Product>) getProductsHandle.invoke(r);
        assertEquals(expected,actual,"File test3.csv was not correctly read");
        String expected2 = "oamk.stream.ReadProductFile$CSVMissingParameterException: Error in test4.csv, skipped line 3: missing parameter";
        assertTrue(errContent.toString().contains(expected2), "Method did not catch the missing parameter in line 3");
    }

}
