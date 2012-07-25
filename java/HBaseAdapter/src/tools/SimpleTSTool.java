package tools;


import data.io.HBaseTSAdapter;
import data.ts.TSDataMapper;
import java.io.IOException;

/*
 *  Create some Time-Series and store them in HBase.
 */

/**
 *
 * @author kamir
 */
public class SimpleTSTool {
    
    public static void main( String[] args ) throws IOException, Exception {
        
        String defaultIP = "127.0.0.1";
        
        if ( args != null ) 
            defaultIP = args[0];
        
        System.out.println( defaultIP );
        
        boolean doCreate = false;
        boolean doLoad = true;
        int nrOfRows = 25;
        
        HBaseTSAdapter.init( defaultIP );

        if ( doCreate ) {
            for( int i = 0; i < nrOfRows ; i++ ) {
                TSDataMapper mapper = new TSDataMapper( 24*299 );
                HBaseTSAdapter.putAccessTS( "wikinodes", mapper , ""+i );
            }

            System.out.println( "Done ..." );
        }
        
        if ( doLoad ) {
            // load all data from DB ...
            for( int i = 0; i < nrOfRows ; i++ ) {
                Object o = HBaseTSAdapter.getAccessTS( "wikinodes", ""+i );
                TSDataMapper mapper = (TSDataMapper)o;
                // System.out.println( mapper.data.length );
            }
        }
        
    }
    
}
