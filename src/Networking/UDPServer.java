package Networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class UDPServer implements Runnable
{
    DatagramSocket socket = null;

    public UDPServer()
    {

    }

    public void run()
    {
        System.out.println("Listening...");

        try
        {
            socket = new DatagramSocket(9876);
            byte[] incomingData = new byte[1024];

            //list of clients
            Map <InetAddress, Boolean> upNodes = new HashMap<>();


            while (true)
            {
                DatagramPacket incomingPacket = new DatagramPacket(incomingData,
                        incomingData.length);
                socket.receive(incomingPacket);
                String message = new String(incomingPacket.getData());
                InetAddress IPAddress = incomingPacket.getAddress();
                int port = incomingPacket.getPort();

                System.out.println("Received message from client: " + message);
                System.out.println("Client IP: " + IPAddress.getHostAddress());
                System.out.println("Client port: " + port);

                upNodes.put(IPAddress, true);
                System.out.println("Clients: " + upNodes.entrySet() + "\n");

                //String reply = "Thank you for the message";
                String reply = upNodes.keySet().toString();
                byte[] data = reply.getBytes();

                DatagramPacket replyPacket =
                        new DatagramPacket(data, data.length, IPAddress, port);
                socket.send(replyPacket);
            }
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        UDPServer server = new UDPServer();
        server.run();
    }
}