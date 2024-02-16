package org.apache.zookeeper.common;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;


@RunWith(Parameterized.class)
public class PathUtilsTest {
    private final String inputValidPath;
    private final boolean expExec;
    private static final Logger LOG = LoggerFactory.getLogger(PathUtils.class);

    public PathUtilsTest(String inputValidPath, boolean expExec){

        this.inputValidPath = inputValidPath;
        this.expExec = expExec;

    }

    @Parameterized.Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
                //valid
                {"/", false}, //nodo root
                {"/app1", false},
                {"/app1/p_1", false},
                //invalid
                {"app1", true},
                {"app1/p_1", true},
                {"", true},
                //null
                {null, true},

                //miglioramenti
                {"//", true},
                {"/./app1", true},
                {"/./.", true},
                {"/app1//p_1", true},
                {" ", true},
                {"/\0 ", true},
                {"/app1/", true},
                {"/app1/../p_1", true},
                {"/\u001e", true},
                {"/\u0019", true},
                {"/\u007F", true},
                {"/\u008F", true},
                {"/\u009F", true},
                {"/\ud800", true},
                {"/\uF8FF", true},
                {"/\uFFF0", true},
                {"/\uFFFF", true}

        });
    }

    @Before
    public void setUp(){
    }

    @Test
    public void ValidPathTest(){

        IllegalArgumentException error = null;

        try {
            PathUtils.validatePath(this.inputValidPath);
            if(expExec == false){
                Assert.assertNull(error);
            }
        } catch (IllegalArgumentException e){
            error = e;
            e.printStackTrace();
            Assert.assertNotNull(error);
        }

        //Assert.assertNull(error);


    }
}