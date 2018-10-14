import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class ClientHandler extends Thread {
    protected Socket socket;
    DataOutputStream out = null;

    public ClientHandler(Socket clientSocket) {
        this.socket = clientSocket;
    }

    public void run() {
        InputStream inp = null;
        BufferedReader brinp = null;

        try {
            inp = socket.getInputStream();
            brinp = new BufferedReader(new InputStreamReader(inp));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            return;
        }

        while (true) {
            try {
                Server.gameState = brinp.readLine();
                System.out.println(Server.gameState);

//                out.writeBytes(Server.gameState + "\n");
//                out.flush();

                // says "foo" every half second
                Timer t = new Timer();
                t.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            out.writeBytes(Server.gameState + "\n");
                            out.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, 0, 500);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}