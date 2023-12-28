package org.apache.zookeeper.server;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(Parameterized.class)
public class CreateNodeInvalidPathTest {

    private String path;
    private byte[] data;
    private List<ACL> acl;
    private long ephemeralOwner;
    private int parentCVersion;
    private long zxid;
    private long time;

    private DataTree dataTree;

    public CreateNodeInvalidPathTest(String path, byte[] data, List<ACL> acl, long ephemeralOwner, int parentCVersion, long zxid, long time) throws KeeperException.NoNodeException, KeeperException.NodeExistsException {

        this.path = path;
        this.data = data;
        this.acl = acl;
        this.ephemeralOwner = ephemeralOwner;
        this.parentCVersion = parentCVersion;
        this.zxid = zxid;
        this.time = time;
        this.dataTree = new DataTree(); //inizializzo dataTree

        //l'inizializzazione del dataTree manca per realizzare il path invalido
    }

    /*@Before
    public void setUpTree() {

        if(path != null) {

            try {

                String[] pathElements = path.split("/");

                System.out.println(Arrays.toString(pathElements));
                String oldPath = "";
                int i = 1;
                for (String elem : pathElements) {
                    System.out.println(pathElements.length + "    i: " + i);
                    if (!(pathElements.length == i || i == 1)) {

                        System.out.println(oldPath + "/" + pathElements[i - 1]);
                        this.dataTree.createNode(oldPath + "/" + pathElements[i - 1], new byte[10], ZooDefs.Ids.CREATOR_ALL_ACL, 0, dataTree.getNode(oldPath).stat.getCversion(), 0, 1);
                        oldPath = oldPath + "/" + pathElements[i - 1];

                    }
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }*/

    @Parameterized.Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][]{

                //nodi non effimeri
                //path non valido, errore non preso dal try-catch, fallisce il test
                //{"app1", new byte[10], ZooDefs.Ids.CREATOR_ALL_ACL, 0, 1, 1, 1},

                //path non valido perché relativo
                {"/app1/../p_2", new byte[30], ZooDefs.Ids.READ_ACL_UNSAFE, 0, 3, 1, 1},
                //path non valido, ma non viene verificato, viene lanciata la NoNodeException
                {"app1/p_1", new byte[10], ZooDefs.Ids.CREATOR_ALL_ACL, 0, 1, 1, 0},
                //path valido ma non corretto, non c'è il nodo/app1, exception-NoNode
                {"/app1/p_1", new byte[30], ZooDefs.Ids.OPEN_ACL_UNSAFE, 0, 3, 25, -1},
                //path valido ma non corretto, nodo TTL
                {"/app1/p_1", new byte[10], ZooDefs.Ids.CREATOR_ALL_ACL, 0xFF00000000000011L, 1, 1, 0},
                //path valido ma non corretto, nodo container
                {"app1/p_1", new byte[10], ZooDefs.Ids.CREATOR_ALL_ACL, 0x8000000000000000L, 1, 1, 0},




        });
    }

    @Test
    public void createNodeInvalidTest(){

        Exception error = null;

        try{
            System.out.println("\n"+ this.path + "\n");
            this.dataTree.createNode(this.path, this.data, this.acl, this.ephemeralOwner, this.parentCVersion, this.zxid, this.time);

            DataNode dataNodeGet = dataTree.getNode(this.path);
            Assert.assertNull(dataNodeGet);


        } catch (KeeperException.NoNodeException | KeeperException.NodeExistsException e) {

            e.printStackTrace();
            error = e;
            Assert.assertNotNull(error);

        }

    }





}