package Modelo;

/*
    Ejemplo de Cliente No Bloqueante
    Envio De Objetos A un servidor
*/

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Client_NoBloqueante {

    private static final int BUFF_SIZE = 100;
    static final String host = "127.0.0.1";
    static final int port = 8800;

    public static void main(String[] args) {

        try {
            Selector selector = Selector.open();
            SocketChannel connectionClient = SocketChannel.open();
            connectionClient.configureBlocking(false);
            connectionClient.connect(new InetSocketAddress(host, port));
            connectionClient.register(selector, SelectionKey.OP_CONNECT);

            while (true) {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {

                    SelectionKey key = (SelectionKey) iterator.next();
                    iterator.remove();

                    SocketChannel client = (SocketChannel) key.channel();

                    //check a connection was established with a remote server.
                    if (key.isConnectable()) {

                        //if a connection operation has been initiated on this channel but not yet completed 
                        if (client.isConnectionPending()) {
                            try {
                                //invoke finishConnect() to complete the connection sequence
                                client.finishConnect();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        client.register(selector, SelectionKey.OP_WRITE);
                        continue;
                    }

                    if (key.isWritable()) {
                        SendSocketObject(client);
                        client.close();
                        return;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void SendSocketObject(SocketChannel client) throws IOException {
        Student outgoingMessage = new Student(15, "Joe Cook");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(outgoingMessage);
        objectOutputStream.flush();
        client.write(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));

    }
}
