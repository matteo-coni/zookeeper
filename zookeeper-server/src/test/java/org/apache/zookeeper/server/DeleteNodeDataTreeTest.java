package org.apache.zookeeper.server;

import org.apache.zookeeper.ZooDefs;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class DeleteNodeDataTreeTest {

    private String path;
    private long zxid;

    private DataTree dataTree;
    private DataTree dataTreeCont;
    private DataTree dataTreeTTL;

    public DeleteNodeDataTreeTest(String path, long zxid){
        this.path = path;
        this.zxid = zxid;
        this.dataTree = new DataTree();
        this.dataTreeCont = new DataTree();
        this.dataTreeTTL = new DataTree();
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

            try { //container

                String[] pathElements = path.split("/");

                System.out.println(Arrays.toString(pathElements));
                String oldPath = "";
                int i = 1;
                for (String elem : pathElements) {
                    System.out.println(pathElements.length + "    i: " + i);
                    if (!(i == 1)) { //modifica per creare il nodo interamente
                        if(i == pathElements.length){
                            System.out.println(oldPath + "/" + pathElements[i - 1]);
                            this.dataTreeCont.createNode(oldPath + "/" + pathElements[i - 1], new byte[10], ZooDefs.Ids.CREATOR_ALL_ACL, 0x8000000000000000L, dataTree.getNode(oldPath).stat.getCversion(), 2, 1);
                            oldPath = oldPath + "/" + pathElements[i - 1];
                        } else {
                            System.out.println(oldPath + "/" + pathElements[i - 1]);
                            this.dataTreeCont.createNode(oldPath + "/" + pathElements[i - 1], new byte[10], ZooDefs.Ids.CREATOR_ALL_ACL, 0, dataTree.getNode(oldPath).stat.getCversion(), 2, 1);
                            oldPath = oldPath + "/" + pathElements[i - 1];
                        }
                    }
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();

            }

            try { //ttl

                String[] pathElements = path.split("/");

                System.out.println(Arrays.toString(pathElements));
                String oldPath = "";
                int i = 1;
                for (String elem : pathElements) {
                    System.out.println(pathElements.length + "    i: " + i);
                    if (!(i == 1)) { //modifica per creare il nodo interamente
                        if(i == pathElements.length){
                            System.out.println(oldPath + "/" + pathElements[i - 1]);
                            this.dataTreeTTL.createNode(oldPath + "/" + pathElements[i - 1], new byte[10], ZooDefs.Ids.CREATOR_ALL_ACL, -2, dataTree.getNode(oldPath).stat.getCversion(), 2, 1);
                            oldPath = oldPath + "/" + pathElements[i - 1];
                        } else {
                            System.out.println(oldPath + "/" + pathElements[i - 1]);
                            this.dataTreeTTL.createNode(oldPath + "/" + pathElements[i - 1], new byte[10], ZooDefs.Ids.CREATOR_ALL_ACL, 0, dataTree.getNode(oldPath).stat.getCversion(), 2, 1);
                            oldPath = oldPath + "/" + pathElements[i - 1];
                        }
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

                {"/", 0},
                {"/app1", 0},
                {"/app1/p_1", 0},
                {"app1", 0},
                {"app1/p_1", 0},
                {"", 0},
                {null, 0},

                {"/", 1},
                {"/app1", 1},
                {"/app1/p_1", 1},
                {"app1", 1},
                {"app1/p_1", 1},
                {"", 1},
                {null, 1},

                {"/", 10},
                {"/app1", 10},
                {"/app1/p_1", 10},
                {"app1", 10},
                {"app1/p_1", 10},
                {"", 10},
                {null, 10},

                {"/", -1},
                {"/app1", -1},
                {"/app1/p_1", -1},
                {"app1", -1},
                {"app1/p_1", -1},
                {"", -1},
                {null, -1},

                {"/", -10},
                {"/app1", -10},
                {"/app1/p_1", -10},
                {"app1", -10},
                {"app1/p_1", -10},
                {"", -10},
                {null, -10},

        });
    }

    @Test
    public void deleteNodeTest(){

        Exception error;
        DataNode dataNodeGet;

        try {

            dataTree.deleteNode(this.path, this.zxid);
            dataNodeGet = dataTree.getNode(this.path);
            System.out.println(dataNodeGet);
            Assert.assertNull(dataNodeGet); //controllo che è stato tolto effettivamente il nodo


        } catch (Exception e){
            error = e;
            e.printStackTrace();
            Assert.assertNotNull(error);
        }


    }

    @Test // miglioramento Container
    public void deleteNodeContTest(){

        Exception error;
        DataNode dataNodeGet;

        try {

            dataTreeCont.deleteNode(this.path, this.zxid);
            dataNodeGet = dataTreeCont.getNode(this.path);
            System.out.println(dataNodeGet);
            Assert.assertNull(dataNodeGet); //controllo che è stato tolto effettivamente il nodo


        } catch (Exception e){
            error = e;
            e.printStackTrace();
            Assert.assertNotNull(error);
        }

    }

    @Test //miglioramento TTL
    public void deleteNodeTTLTest(){

        Exception error;
        DataNode dataNodeGet;

        try {

            dataTreeTTL.deleteNode(this.path, this.zxid);
            dataNodeGet = dataTreeTTL.getNode(this.path);
            System.out.println(dataNodeGet);
            Assert.assertNull(dataNodeGet); //controllo che è stato tolto effettivamente il nodo


        } catch (Exception e){
            error = e;
            e.printStackTrace();
            Assert.assertNotNull(error);
        }


    }


}


