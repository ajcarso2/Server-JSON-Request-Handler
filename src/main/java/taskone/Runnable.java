package taskone;

import java.net.Socket;

// New Runnable class to handle each client connection
class ClientHandler implements Runnable {
    private Socket socket;
    private StringList strings;

    public ClientHandler(Socket socket, StringList strings) {
        this.socket = socket;
        this.strings = strings;
    }

    @Override
    public void run() {
        Performer performer = new Performer(socket, strings);
        performer.doPerform();
        try {
            System.out.println("close socket of client ");
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
