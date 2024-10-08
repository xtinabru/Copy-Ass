package oamk.stream;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class VideoFileTest {
    
    private static MethodHandle getVideoFileDataHandle;
    private static Constructor<?> vConstructor;
    private static Constructor<?> mConstructor;

    @BeforeEach
    public void setUp() {
        try {
            // Check inheritance
            Class<?> pclazz = Playable.class; 
            boolean isInterface = Modifier.isInterface(pclazz.getModifiers());
            if (isInterface == false) {
                assertTrue(false, "Playable class must be declared as interface");
            }
        } catch (Exception e) {
            assertTrue(false, "Interface Playable declaration is incorrect");
        }

        try {
            // Setup for Metadata class
            Class<?> mclazz = Class.forName("oamk.stream.Metadata");
            if (mclazz.isRecord() == false) {
                assertTrue(false, "Metadata class must be a record");
            }
            mConstructor = mclazz.getConstructor(String.class, String.class, String.class);

        } catch (Exception e) {
            assertTrue(false, "Record Metadata declaration is incorrect");
        }

        try {
            // Setup for VideoFile class
            Class<?> vClass = Class.forName("oamk.stream.VideoFile");
            vConstructor = vClass.getConstructor(String.class);
            if (Playable.class.isAssignableFrom(vClass) == false) {
                assertTrue(false, "Class VideoFile does not implement interface Playable");
            }

            MethodType type = MethodType.methodType(Metadata.class);
            getVideoFileDataHandle = MethodHandles.lookup().findVirtual(vClass, "getVideoFileData", type);
                        
        } catch (Exception e) {
            assertTrue(false, "Class VideoFile declaration is incorrect");
        }
    }

    @Test
    @DisplayName("Test getVideoFileData method")
    public void testGetVideoFileData() throws Throwable {
        
        Object[] vargs = { "Ludwig van Beethoven - Ode an die Freude.mp4" };
        Object v = (VideoFile)vConstructor.newInstance(vargs);
        Object[] margs = { "Ludwig van Beethoven", "Ode an die Freude", "mp4" };
        Object expected = (Metadata) mConstructor.newInstance(margs);
        Metadata actual = (Metadata) getVideoFileDataHandle.invoke((VideoFile)v);
        assertEquals((Metadata)expected, actual, "getVideoFileData method is not correctly implemented");

        Object[] vargs2 = { "Mozart - Eine kleine Nachtmusik.wmv" };
        Object v2 = (VideoFile)vConstructor.newInstance(vargs2);
        Object[] margs2 = { "Mozart", "Eine kleine Nachtmusik", "wmv" };
        Object expected2 = (Metadata) mConstructor.newInstance(margs2);
        Metadata actual2 = (Metadata) getVideoFileDataHandle.invoke((VideoFile)v2);
        assertEquals((Metadata)expected2, actual2, "getVideoFileData method is not correctly implemented");
    }
    
}
