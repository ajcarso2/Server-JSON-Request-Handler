/**
  File: Performer.java
  Author: Student in Fall 2020B
  Description: Performer class in package taskone.
*/

package taskone;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.JSONObject;

/**
 * Class: Performer 
 * Description: Threaded Performer for server tasks.
 */
class Performer {

    private StringList state;
    private Socket conn;

    public Performer(Socket sock, StringList strings) {
        this.conn = sock;
        this.state = strings;
    }

    synchronized public JSONObject add(String str) {
        JSONObject json = new JSONObject();
        json.put("ok", true);
        json.put("type", "add");
        state.add(str);
        json.put("data", state.toString());
        return json;
    }

    synchronized public JSONObject clear() {
        JSONObject json = new JSONObject();
        json.put("ok", true);
        json.put("type", "clear");
        state.clear();
        json.put("data", state.toString());
        return json;
    }

    synchronized public JSONObject find(String str) {
        JSONObject json = new JSONObject();
        json.put("ok", true);
        json.put("type", "find");

        int index = -1;
        for (int i = 0; i < state.size(); i++) {
            if (state.get(i).equals(str)) {
                index = i;
                break;
            }
        }

        json.put("data", Integer.toString(index)); // Convert the integer to a string before adding it to the JSON object
        return json;
    }

    synchronized public static JSONObject error(String err) {
        JSONObject json = new JSONObject();
        json.put("ok", false);
        json.put("message", err);
        return json;
    }

    synchronized public JSONObject delete(int index) {
        JSONObject json = new JSONObject();
        if (index >= 0 && index < state.size()) {
            state.remove(index);
            json.put("ok", true);
            json.put("type", "delete");
            json.put("data", state.toString());
        } else {
            json.put("ok", false);
            json.put("message", "Invalid index: " + index);
        }
        return json;
    }

    synchronized public JSONObject prepend(int index, String str) {
        JSONObject json = new JSONObject();
        if (index >= 0 && index < state.size()) {
            String current = state.get(index);
            state.set(index, str + current);
            json.put("ok", true);
            json.put("type", "prepend");
            json.put("data", state.toString());
        } else {
            json.put("ok", false);
            json.put("message", "Invalid index: " + index);
        }
        return json;
    }

    synchronized public JSONObject displayList() {
        JSONObject json = new JSONObject();
        json.put("ok", true);
        json.put("type", "display");
        json.put("data", state.toString());
        return json;
    }

    public void doPerform() {
        boolean quit = false;
        OutputStream out = null;
        InputStream in = null;
        try {
            out = conn.getOutputStream();
            in = conn.getInputStream();
            System.out.println("Server connected to client:");
            while (!quit) {
                byte[] messageBytes = NetworkUtils.receive(in);
                JSONObject message = JsonUtils.fromByteArray(messageBytes);
                JSONObject returnMessage = new JSONObject();

                int choice = message.getInt("selected");
                switch (choice) {
                    case (1):
                        String inStr = (String) message.get("data");
                        returnMessage = add(inStr);
                        break;
                    case (2):
                        returnMessage = clear();
                        break;
                    case (3):
                        String findStr = (String) message.get("data");
                        returnMessage = find(findStr);
                        break;
                    case (4):
                        returnMessage = displayList();
                        break;
                    case (5):
                        int deleteIndex = message.getInt("data");
                        returnMessage = delete(deleteIndex);
                        break;
                    case (6):
                        String[] prependData = ((String) message.get("data")).split(" ", 2);
                        int prependIndex = Integer.parseInt(prependData[0]);
                        String prependStr = prependData[1];
                        returnMessage = prepend(prependIndex, prependStr);
                        break;
                    case (0):
                        quit = true;
                        break;
                    default:
                        returnMessage = error("Invalid selection: " + choice
                                + " is not an option");
                        break;
                }
                // we are converting the JSON object we have to a byte[]
                byte[] output = JsonUtils.toByteArray(returnMessage);
                NetworkUtils.send(out, output);
            }
            // close the resource
            System.out.println("close the resources of client ");
            try {
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}