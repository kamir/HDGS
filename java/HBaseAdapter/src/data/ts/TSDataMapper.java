/**
 *
 * Our simplest representation of Time Series is
 * a binary data array and a properties object.
 * 
 **/
package data.ts;

import java.io.Serializable;
import java.util.Properties;

/**
 *
 * @author kamir
 */
public class TSDataMapper implements Serializable {
    
    public double[] data;
    public TSDataMapper( int nrOfvalues ) {
        data = new double[nrOfvalues];
        for( int i = 0; i < nrOfvalues; i++ ) {
            data[i] = 0.0;
        }
    }
    
    public Properties props = new Properties();
    
    public static TSDataMapper initRandomSeries( int z ) {
        TSDataMapper mapper = new TSDataMapper( z );
        for( int i = 0; i < z; i++ ) {
            mapper.data[i] = Math.random();
        }
        return mapper;
    }
}
