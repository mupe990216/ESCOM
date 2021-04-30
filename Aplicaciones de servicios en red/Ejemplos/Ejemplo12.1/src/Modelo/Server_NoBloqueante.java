package Modelo;

/*
    Ejemplo de Servidor No Bloqueante
    Recepcion De Objetos por parte de clientes
*/

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Server_NoBloqueante {

    private static final int BUFF_SIZE = 100;
    static final String host = "127.0.0.1";
    static final int port = 8800;

    public static void main(String[] args) {

        try {
            Selector selector = Selector.open();
            ServerSocketChannel server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.socket().bind(new InetSocketAddress(host, port));
            server.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Server started");
            while (true) {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {

                    SelectionKey key = (SelectionKey) iterator.next();
                    iterator.remove();

                    if (key.isAcceptable()) {
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        continue;
                    }

                    if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        getSocketObjet(channel);
                        channel.close();
                        return;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getSocketObjet(SocketChannel socketChannel) throws IOException, ClassNotFoundException {
        ByteBuffer data = ByteBuffer.allocate(BUFF_SIZE);
        socketChannel.read(data);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data.array());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Student message = (Student) objectInputStream.readObject();
        System.out.println(message.toString());

    }
}
