package org.lia.multithreading;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

public class SendRequest implements Runnable {

    GetRequest request;

    public SendRequest(GetRequest getRequest) {
        request = getRequest;
    }

    @Override
    public void run() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(request.response);
            byte[] secondaryBuffer = baos.toByteArray();
            ByteBuffer mainBuffer = ByteBuffer.wrap(secondaryBuffer);
            request.dc.send(mainBuffer, request.address);
            request.buf.clear();
        } catch (IOException e) {
           e.printStackTrace();
        }
    }
}
