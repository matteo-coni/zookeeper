package org.apache.zookeeper.server;

import org.apache.zookeeper.data.ACL;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class CreateNodeDataTreeTest {

    private String path;
    private byte[] data;
    private List<ACL> acl;
    private long ephemeralOwner;
    private int parentCVersion;
    private long zxid;
    private long time;

    public CreateNodeDataTreeTest(String path, byte[] data, List<ACL> acl, long ephemeralOwner, int parentCVersion, long zxid, long time){
        this.path = path;
        this.data = data;
        this.acl = acl;
        this.ephemeralOwner = ephemeralOwner;
        this.parentCVersion = parentCVersion;
        this.zxid = zxid;
        this.time = time;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][]{

                {null, null, null, null, null, null, null} //prova

        });
    }

    @Test
    public void createNodeTest(){

    }

}