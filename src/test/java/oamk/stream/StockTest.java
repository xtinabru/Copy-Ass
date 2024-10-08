package oamk.stream;

import java.util.List;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StockTest {

    private static MethodHandle IformatProductHandle;

    private static MethodHandle formatProductHandle;
    private static MethodHandle getProductsHandle;
    private static MethodHandle addProductHandle;
    private static MethodHandle toStringHandle;
    private static MethodHandle reportExpiredHandle;
    private static Constructor<?> sConstructor;
    private static Constructor<?> pConstructor;

    @BeforeEach
    public void setUp() throws Throwable {

        // test outputformatter interface
        try {
            Class<?> oclazz = Class.forName("oamk.stream.OutputFormatter");
            boolean isInterface = Modifier.isInterface(oclazz.getModifiers());
            if (isInterface == false) {
                assertTrue(false, "OutputFormatter class must be an interface");
            }
            MethodType fpType = MethodType.methodType(String.class, DateTimeFormatter.class);
            IformatProductHandle = MethodHandles.lookup().findVirtual(oclazz, "formatProduct", fpType);
        } catch (Exception e) {
            assertTrue(false, "Interface OutputFormatter declaration is incorrect");
        }

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

            MethodType type = MethodType.methodType(String.class, DateTimeFormatter.class);
            formatProductHandle = MethodHandles.lookup().findVirtual(pclazz, "formatProduct", type);

        } catch (Exception e) {
            assertTrue(false, "Class Product declaration is incorrect");
        }

        try {
            // test stock class
            Class<?> sclazz = Class.forName("oamk.stream.Stock");
            sConstructor = sclazz.getConstructor();
            MethodType type = MethodType.methodType(List.class);
            getProductsHandle = MethodHandles.lookup().findVirtual(sclazz, "getProducts", type);
            type = MethodType.methodType(void.class, Product.class);
            addProductHandle = MethodHandles.lookup().findVirtual(sclazz, "addProduct", type);
            type = MethodType.methodType(String.class);
            toStringHandle = MethodHandles.lookup().findVirtual(sclazz, "toString", type);
            type = MethodType.methodType(List.class, LocalDateTime.class);
            reportExpiredHandle = MethodHandles.lookup().findVirtual(sclazz, "reportExpired", type);

        } catch (Exception e) {
            assertTrue(false, "Class Stock declaration is incorrect");
        }
    }

    @Test
    @DisplayName("Test methods of class Stock")
    public void testStockMethods() throws Throwable {
        Object[] initargs = {};
        Object s = sConstructor.newInstance(initargs);

        Object[] p1args = { "car", 15000.0, "vehicles", LocalDateTime.of(2060, 12, 31, 23, 59, 59) };
        Object p1 = pConstructor.newInstance(p1args);
        Object[] p2args = { "carrot", 2.99, "vegetables", LocalDateTime.of(2025, 4, 15, 23, 59, 34) };
        Object p2 = pConstructor.newInstance(p2args);
        // methods addproduct and getproducts
        addProductHandle.invoke((Stock) s, (Product) p1);
        addProductHandle.invoke((Stock) s, (Product) p2);
        List<Product> expected = new LinkedList<Product>();
        expected.add((Product) p1);
        expected.add((Product) p2);
        List<Product> actual = (List<Product>) getProductsHandle.invoke((Stock) s);
        assertEquals(expected, actual, "addProduct() or getProducts() method is not correctly implemented");

        // method tostring
        Object[] p3args = { "beans", 4.99, "cannedfood", LocalDateTime.of(2032, 4, 15, 23, 59, 34) };
        Object p3 = pConstructor.newInstance(p3args);
        addProductHandle.invoke((Stock) s, (Product) p3);
        String expected2 = "List of Products\n" + //
                "----------------\n" + //
                "[car,vehicles,15000.00,2060.12.31]\n" + //
                "[carrot,vegetables,2.99,2025.04.15]\n" + //
                "[beans,cannedfood,4.99,2032.04.15]\n" + //
                "-> Total products: 3\n" + //
                "-> Total price: 15007.98\n";
        String actual2 = (String) toStringHandle.invoke((Stock) s);
        assertEquals(expected2, actual2, "toString() method is not correctly implemented");
    }

    @Test
    @DisplayName("Test stream with class Stock")
    public void testStockStream() throws Throwable {

        Object[] initargs = {};
        Object s = sConstructor.newInstance(initargs);
        Object[] p4args = { "fish", 2.99, "food", LocalDateTime.of(2024, 8, 31, 23, 59, 59) };
        Object p4 = pConstructor.newInstance(p4args);
        Object[] p5args = { "fish", 2.99, "food", LocalDateTime.of(2024, 10, 30, 23, 59, 34) };
        Object p5 = pConstructor.newInstance(p5args);
        addProductHandle.invoke((Stock) s, (Product) p4);
        addProductHandle.invoke((Stock) s, (Product) p5);

        List<Product> expected3 = new LinkedList<Product>();
        expected3.add((Product) p4);
        assertEquals(expected3, (List<Product>) reportExpiredHandle.invoke((Stock) s, LocalDateTime.now()),
                "removeExpired() method is not correctly implemented");
    }
}
