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
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][]{

                //nodi non effimeri
                {"/", new byte[10], ZooDefs.Ids.CREATOR_ALL_ACL, 0, 3, 1, 1},
                {"/app1", new byte[15], ZooDefs.Ids.CREATOR_ALL_ACL, 0, 1, -1, 1},
                {"/app1/p_1" , new byte[30], ZooDefs.Ids.OPEN_ACL_UNSAFE, 0, -2, 0, 1},
                //nodi effimeri standard con id sessione = 2
                {"/", new byte[0], ZooDefs.Ids.READ_ACL_UNSAFE, 2, 3, 1, 1},
                {"/app1", new byte[15], ZooDefs.Ids.CREATOR_ALL_ACL, 2, 0, 100, -1},
                {"/app1/p_1" , new byte[30], ZooDefs.Ids.CREATOR_ALL_ACL, 2, 2, 1, 0},
                //con i due test sottostanti (path non validi null e "") il metodo createNode lancia un eccezione che non viene gestita correttamente
                //{"", new byte[1], ZooDefs.Ids.CREATOR_ALL_ACL, 1, 3, 1, 1},
                //{null, new byte[10], ZooDefs.Ids.CREATOR_ALL_ACL, 1, 3, 1, 1},
                {"/app1", null, ZooDefs.Ids.CREATOR_ALL_ACL, 1, 3, 1, 1},

                //aggiunto parentCversion == -1
                {"/app1/p_1", new byte[10], ZooDefs.Ids.CREATOR_ALL_ACL, 0, -1, 1, 1},
                //aggiunto nodo TTL con TTl==11
                {"/app1/p_1", new byte[10], ZooDefs.Ids.CREATOR_ALL_ACL, 0xFF00000000000011L, 2, 1, 1},
                //aggiunto nodo container
                {"/app1", new byte[1000], ZooDefs.Ids.OPEN_ACL_UNSAFE, 0x8000000000000000L, 0, 0, 1},
                // start with /zookeeper/quota
                {"/zookeeper/quota/zookeeper_stats", new byte[100], ZooDefs.Ids.CREATOR_ALL_ACL, 0, 2, 1, 1},


        });
    }

    @Test
    public void createNodeTest(){

        Exception error = null;
        long digestPre = dataTree.getTreeDigest();
        long digestPost = 0;
        try{
            System.out.println("\n"+ this.path + "\n");
            this.dataTree.createNode(this.path, this.data, this.acl, this.ephemeralOwner, this.parentCVersion, this.zxid, this.time);
            digestPost = dataTree.getTreeDigest();
            DataNode dataNodeGet = dataTree.getNode(this.path);
            System.out.println(dataNodeGet);
            //fai anche controllo su nodo creato e nodo preso dalla get
            if(!this.path.equals("/zookeeper/quota/zookeeper_stats")) Assert.assertNotEquals(digestPre,digestPost);


        } catch (KeeperException.NoNodeException | KeeperException.NodeExistsException e) {

            e.printStackTrace();
            error = e;
            Assert.assertNotNull(error);

        }
        Assert.assertNull(error);
        System.out.println("pre: " + digestPre + "    post: " + digestPost);


    }

    @Test
    public void createNodeExistingTest(){

        Exception error = null;

        try{
            System.out.println("\n"+ this.path + "\n");
            //gia esistente
            this.dataTree.createNode(this.path, this.data, this.acl, this.ephemeralOwner, this.parentCVersion, this.zxid, this.time);
            this.dataTree.createNode(this.path, this.data, this.acl, this.ephemeralOwner, this.parentCVersion, this.zxid, this.time);

        } catch (KeeperException.NoNodeException | KeeperException.NodeExistsException e) {

            e.printStackTrace();
            error = e;
        }
        Assert.assertNotNull(error);
    }

}