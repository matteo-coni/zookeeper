package org.apache.zookeeper.server;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
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

    private DataTree dataTree;

    public CreateNodeDataTreeTest(String path, byte[] data, List<ACL> acl, long ephemeralOwner, int parentCVersion, long zxid, long time) throws KeeperException.NoNodeException, KeeperException.NodeExistsException {

        this.path = path;
        this.data = data;
        this.acl = acl;
        this.ephemeralOwner = ephemeralOwner;
        this.parentCVersion = parentCVersion;
        this.zxid = zxid;
        this.time = time;
        this.dataTree = new DataTree(); //inizializzo dataTree
        System.out.println(dataTree.getAllChildrenNumber("/"));

        //l'inizializzazione del dataTree è stata prima realizzata funzionante in questo punto, poi spostata nel Before
    }

    @Before
    public void setUpTree() {
        try {

            String [] pathElements = path.split("/");

            System.out.println(Arrays.toString(pathElements));
            String oldPath = "";
            int i=1;
            for(String elem : pathElements) {
                System.out.println(pathElements.length + "    i: " + i);
                if (!(pathElements.length == i || i==1)){

                    System.out.println(oldPath + "/" + pathElements[i-1]);
                    this.dataTree.createNode(oldPath + "/" + pathElements[i-1] , new byte[10], ZooDefs.Ids.CREATOR_ALL_ACL, 1, 3, 1, 1);
                    oldPath = oldPath + "/" + pathElements[i-1];

                }
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown(){
        //verifica se fa pulito l'ambiente
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][]{

                {"/path1/path2/path3", new byte[10], ZooDefs.Ids.CREATOR_ALL_ACL, 1, 4, 1, 1}, //prova
                {"/ao", new byte[10], ZooDefs.Ids.CREATOR_ALL_ACL, 0, 3, 1, 1},
                //{"/path3", new byte[10], ZooDefs.Ids.CREATOR_ALL_ACL, 0xFF00000000000111L, 3, 1, 1}

        });
    }

    @Test
    public void createNodeTest(){

        //prova


        Exception error = null;
        long digestPre = dataTree.getTreeDigest();
        long digestPost = 0;
        try{
            this.dataTree.createNode(this.path, this.data, this.acl, this.ephemeralOwner, this.parentCVersion, this.zxid, this.time);
            digestPost = dataTree.getTreeDigest();
            DataNode dataNodeGet = dataTree.getNode(this.path);
            System.out.println(dataNodeGet);
            //fai anche controllo su nodo creato e nodo preso dalla get


        } catch (KeeperException.NoNodeException | KeeperException.NodeExistsException e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
            error = e;
        }
        Assert.assertNull(error);
        System.out.println("pre: " + digestPre + "    post: " + digestPost);


    }

}