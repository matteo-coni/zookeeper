package org.apache.zookeeper.server;

import org.apache.zookeeper.ZooDefs;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class DeleteNodeDataTreeTest {

    private String path;
    private long zxid;

    private DataTree dataTree;

    public DeleteNodeDataTreeTest(String path, long zxid){
        this.path = path;
        this.zxid = zxid;
        this.dataTree = new DataTree();
    }

    @Before
    public void setUpTree() {

        if(path != null) {

            try {

                String[] pathElements = path.split("/");

                System.out.println(Arrays.toString(pathElements));
                String oldPath = "";
                int i = 1;
                for (String elem : pathElements) {
                    System.out.println(pathElements.length + "    i: " + i);
                    if (!(i == 1)) { //modifica per creare il nodo interamente

                        System.out.println(oldPath + "/" + pathElements[i - 1]);
                        this.dataTree.createNode(oldPath + "/" + pathElements[i - 1], new byte[10], ZooDefs.Ids.CREATOR_ALL_ACL, 0, dataTree.getNode(oldPath).stat.getCversion(), 2, 1);
                        oldPath = oldPath + "/" + pathElements[i - 1];

                    }
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][]{

                {"/app1/p_1", 2 },

        });
    }

    @Test
    public void deleteNodeTest(){

        Exception error = null;
        DataNode dataNodeGet;

        try {

            dataTree.deleteNode(this.path, this.zxid);
            dataNodeGet = dataTree.getNode(this.path);
            Assert.assertNull(dataNodeGet);


        } catch (Exception e){
            error = e;
            e.printStackTrace();
            Assert.assertNotNull(error);
        }
        Assert.assertNull(error);

    }


}