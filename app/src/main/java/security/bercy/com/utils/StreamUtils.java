package security.bercy.com.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Bercy on 7/25/17.
 */
/*
read stream
 */
public class StreamUtils {
    /*
        return string after read the stream
     */
    public static String readFromStream(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int length = 0;
        byte[] buffer = new byte[1024];

        while ((length = in.read(buffer)) != -1) {
            out.write(buffer, 0, length);
        }
        String result = out.toString();
        in.close();
        out.close();
        return result;
    }

}