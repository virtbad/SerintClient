package ch.virtbad.serint.client.networking;

import ch.virt.pseudopackets.handlers.ClientPacketHandler;
import ch.virt.pseudopackets.packets.Packet;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author VirtCode
 * @version 1.0
 */
@Slf4j
public abstract class CustomClientPacketHandler extends ClientPacketHandler {

    public abstract void connected();

    public abstract void disconnected();

    public void handle(Packet packet){
        try {
            Method method = this.getClass().getMethod("handle", packet.getClass());
            method.invoke(this, packet);
        } catch (InvocationTargetException e) {
            Throwable t = e.getCause();
            if (t != null) t.printStackTrace();

            log.warn("Failed handling Packet {}", packet.getClass().getName());
        } catch (Exception e){
            log.warn("Failed to handle Packet {}", packet.getClass().getName());

        }
    }
}