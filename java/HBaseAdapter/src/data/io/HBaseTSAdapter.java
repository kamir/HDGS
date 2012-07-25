/**
 * 
 * High level adapter for storing and retrieving Time Series from HBase.
 * 
 */

package data.io;

import java.io.*;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author kamir
 */
public class HBaseTSAdapter {
    
    private HBaseTSAdapter() {};
   
    static String defaultZookeeperIP = "172.16.14.129";
    
    static HBaseAdapter hba = null;
    
    /**
     * Connect to a Zookeeper, who knows the location of an HBase Master.
     * 
     * @param zk 
     */
    public static void init( String zk ) {
        defaultZookeeperIP = zk;
        hba = new HBaseAdapter( defaultZookeeperIP );
    }
    
    /** 
     * 
     * the object can be a Messreihe.
     * 
     * @param data
     * @param pageID 
     */
    public static void putAccessTS( String tablename, Object data, String pageID ) throws IOException, Exception {
    
        // To add to a row, use Put.  A Put constructor takes the name of the row
        // you want to insert into as a byte array.  In HBase, the Bytes class has
        // utility for converting all kinds of java types to byte arrays.  In the
        // below, we are converting the String "pageID" into a byte array to
        // use as a row key for our update. Once you have a Put instance, you can
        // adorn it by setting the names of columns you want to update on the row,
        // the timestamp to use in your update, etc.If no timestamp, the server
        // applies current time to the edits.
        
        String rowKey = pageID;
        Put p = new Put(Bytes.toBytes( rowKey ));

        // now we serialize the object ...
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);   
        out.writeObject(data);
        byte[] dataBytes = bos.toByteArray();
        out.close();
        bos.close();
        
        
        // To set the value you'd like to update in the row "pageID", specify
        // the column family, column qualifier, and value of the table cell you'd
        // like to update.  The column family must already exist in your table
        // schema.  The qualifier can be anything.  All must be specified as byte
        // arrays as hbase is all about byte arrays.  Lets pretend the table
        // 'wikinodes' was created with a family 'accessts'.
        p.add(Bytes.toBytes("accessts"), Bytes.toBytes("raw.random"), dataBytes);

        // Once you've adorned your Put instance with all the updates you want to
        // make, to commit it do the following (The HTable#put method takes the
        // Put instance you've been building and pushes the changes you made into
        // hbase)
        HTable table = hba.getTable(tablename);
        table.put(p);
        System.out.println("> stored data: " + pageID );
    }
    
//    /** 
//     * 
//     * the object can be a Messreihe.
//     * 
//     * @param data
//     * @param pageID 
//     */
//    public static void putEditTS( Object data, String pageID ) {
//    
//    }
    
    public static Object getAccessTS( String tablename, String pageID ) throws IOException, Exception {
        Object o = null;
        HTable table = hba.getTable(tablename);
        
        // Now, to retrieve the data we just wrote. The values that come back are
        // Result instances. Generally, a Result is an object that will package up
        // the hbase return into the form you find most palatable.
        Get g = new Get(Bytes.toBytes(pageID));
        Result r = table.get(g);
        byte [] value = r.getValue(Bytes.toBytes("accessts"), Bytes.toBytes("raw.random") );
        
        // If we convert the value bytes, we should get back 'Some Value', the
        // value we inserted at this location.
        String valueStr = Bytes.toString(value);
        // System.out.println("GET: " + valueStr);

        ByteArrayInputStream bis = new ByteArrayInputStream(value);
        ObjectInput in = new ObjectInputStream(bis);
        o = in.readObject(); 
        
        bis.close();
        in.close();
        
        System.out.println("> loaded data: " + pageID );
        return o;
    }
    
//    public static Object getEditsTS( String pageID ) {
//        Object o = null;
//        return o;
//    }
 
    
    
}
