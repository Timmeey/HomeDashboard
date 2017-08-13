package de.timmeey.iot.homeDashboard.lights;

import de.timmeey.libTimmeey.printable.Printed;
import java.awt.Color;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

/**
 * UDPLight.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@Slf4j
public final class UDPLight extends RGBLight{

    public static final String BROADCAST_ADDRESS = "192.168.134.255";
    public static final int PORT = 15724;
    public static final String RECEIP = "all";
    private final DatagramSocket sendSocket;

    public UDPLight(final ColorSource colorSource, DatagramSocket socket) {
        super(colorSource);
        this.sendSocket = socket;

    }

    @Override
    void switchOff() {
        this.showColor(Color.BLACK);
    }

    @Override
    void switchOn() {
        this.showColor(this.getLastColor());
    }

    @Override
    void showColor(final Color color) {
        try {
            sendSocket.setBroadcast(true);
            byte[] bytes = createBuffer(color, RECEIP);
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
            packet.setPort(PORT);
            packet.setAddress(Inet4Address.getByName(BROADCAST_ADDRESS));
            sendSocket.send(packet);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private byte[] createBuffer(Color col, String receip) throws UnsupportedEncodingException {
        return String.format("%s,%s,%s,%s", col.getRed(), col.getGreen(), col.getBlue(), receip).getBytes(StandardCharsets.UTF_8);

    }
    @Override
    Printed enrich(final Printed printed) {
        printed.with("type","UDPLight");
        printed.with("broadcastAddress",BROADCAST_ADDRESS);
        printed.with("port",PORT);
        printed.with("receipIdentifier",RECEIP);
        return printed;
    }
}
