package Networking;

import java.io.IOException;
import java.net.*;

public class UDPClient implements Runnable
{
    DatagramSocket Socket;
    private int msDelay;
    private int uptime;

    public UDPClient()
    {
        msDelay = 5000;
        uptime = -msDelay;
    }

    private void incUptime()
    {
        uptime += msDelay;
    }

    private int getUptime()
    {
        return uptime;
    }

    public void run()
    {
        try
        {
            Socket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName("localhost");
            byte[] incomingData = new byte[1024];
            int packetNum = 1;

            while (true)
            {
                String sentence = "packet " + Integer.toString(packetNum);
                byte[] data = sentence.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, 9876);
                Socket.send(sendPacket);
                System.out.println("Message sent from client");
                packetNum++;

                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                Socket.receive(incomingPacket);
                String response = new String(incomingPacket.getData());
                System.out.println("Response from server: " + response);

                this.incUptime();
                System.out.println("Client uptime: " + this.getUptime() + "ms\n");
                Thread.sleep(msDelay);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        UDPClient client = new UDPClient();
        client.run();
    }
}