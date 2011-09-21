package org.hwdb.srpc;

import java.util.logging.Level;

/**
 * Hello world!
 *
 */
public class EchoServer
{

    public static void main(String[] args)
    {
        try {
            SRPC srpc = new SRPC(20000);
            Service service = srpc.offer("Echo");
            Message query;
            while ((query = service.query()) != null) {
                query.getConnection().response("1" + query.getContent());
            }

            } catch (Exception e) {
                System.exit(1);
            }

    }

    public static Service echo(SRPC srpc) {
            return srpc.offer("Echo");
    }





}
