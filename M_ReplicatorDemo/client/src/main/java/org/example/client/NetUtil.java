package org.example.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @description: TODO
 * @author: xiaoliang
 * @date: 2022/3/9 14:32
 **/
public class NetUtil {

    private static final Logger logger = LoggerFactory.getLogger( NetUtil.class );


    public static boolean isPortAvailable(int port) throws IOException {
        Socket s1 = new Socket();
        s1.bind(new InetSocketAddress("0.0.0.0", port));
        s1.close();
        logger.info("checking port v4... 0.0.0.0:{} is available",port);

        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        Enumeration<InetAddress> inetAddresses;
        InetAddress inetAddress;
        boolean ipV6 = false;
        while ( networkInterfaces.hasMoreElements() ) {
            inetAddresses = networkInterfaces.nextElement().getInetAddresses();
            while ( inetAddresses.hasMoreElements() ) {
                inetAddress = inetAddresses.nextElement();
                if (inetAddress != null
                        && !inetAddress.isLoopbackAddress()
                        && !inetAddress.isLinkLocalAddress()
                        && !inetAddress.isMulticastAddress()) {

                    String ip = inetAddress.getHostAddress();
                    if (inetAddress instanceof Inet6Address) {
                        ipV6 = true;
                        if(ip!=null&&ip.indexOf("%")>0) {
                            ip = ip.substring(0,(ip.indexOf("%")));
                        }
                    }

                    Socket s = new Socket();
                    s.bind(new InetSocketAddress(ip, port));
                    s.close();
                    logger.info("checking port on local ip... {}:{} is available",ip,port);
                }
            }

        }

        if (ipV6) {
            Socket s2 = new Socket();
            s2.bind(new InetSocketAddress("::", port));
            s2.close();
            logger.info("checking port v6... :::{} is available", port);
        }

        logger.info("checking port result: {} is available", port);
        return true;
    }

    public static String getInnerIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            logger.error( e.getMessage(), e );
        }
        return "127.0.0.1";
    }

    /**
     * 获取ip地址
     *
     * @return 当前ip
     */
    public static String getLocalIPList() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            NetworkInterface networkInterface;
            Enumeration<InetAddress> inetAddresses;
            InetAddress inetAddress;
            String ip;
            while ( networkInterfaces.hasMoreElements() ) {
                networkInterface = networkInterfaces.nextElement();
                inetAddresses = networkInterface.getInetAddresses();
                while ( inetAddresses.hasMoreElements() ) {
                    inetAddress = inetAddresses.nextElement();
                    if ( inetAddress != null
                            && inetAddress instanceof Inet4Address
                            && !inetAddress.isLoopbackAddress()
                            && !inetAddress.isLinkLocalAddress()
                            && !inetAddress.isMulticastAddress() ) {
                        // IPV4
                        ip = inetAddress.getHostAddress();
                        return ip;
                    }
                }
            }
        } catch ( SocketException e ) {
            logger.error( e.getMessage(), e );
        }
        return "127.0.0.1";
    }


    /**
     * 获取多个本机ip地址,包括v4,v6
     *
     * @return 当前ip
     * @throws SocketException
     */
    public static List<String> getLocalIpList() throws SocketException {
        List<String> ipList = new ArrayList<String>();
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        NetworkInterface networkInterface;
        Enumeration<InetAddress> inetAddresses;
        InetAddress inetAddress;
        while (networkInterfaces.hasMoreElements()) {
            networkInterface = networkInterfaces.nextElement();
            inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                inetAddress = inetAddresses.nextElement();
                if (inetAddress != null && !inetAddress.isLoopbackAddress()
                        && !inetAddress.isLinkLocalAddress() && !inetAddress.isMulticastAddress()) {
                    String ip = inetAddress.getHostAddress();
                    if (inetAddress instanceof Inet6Address) {
                        if(ip!=null&&ip.indexOf("%")>0) {
                            ip = ip.substring(0,(ip.indexOf("%")));
                        }
                    }
                    ipList.add(ip);
                }
            }
        }
        return ipList;
    }


    public static void main( String[] args ) {
        logger.info( getLocalIPList() );
    }
}
