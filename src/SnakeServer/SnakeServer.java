/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SnakeServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Brian
 */
public class SnakeServer {

    public static void main(String args[]) throws IOException, ClassNotFoundException {

        //Abrindo um server
        ServerSocket server = new ServerSocket(2048);
        List<Player> players = new ArrayList<Player>();

        try {
            //Se já existir o arquivo com pontuações, queremos ler as pontuações existentes 
            File f = new File("players.ser");
            if (f.exists() && !f.isDirectory()) {
                ObjectInputStream input = new ObjectInputStream(new FileInputStream("players.ser"));
                players = (ArrayList<Player>) input.readObject();
                input.close();
            }
        } catch (Throwable t) {
        }

        //Queremos escutar indefinidamente.
        boolean run = true;
        while (run) {
            //Escutar uma nova conexão
            Socket connection = server.accept();

            DataOutputStream sent = new DataOutputStream(connection.getOutputStream());
            DataInputStream received = new DataInputStream(connection.getInputStream());

            sent = new DataOutputStream(connection.getOutputStream());
            //Pegar os dados do jogador
            int points = received.readInt();
            String name = received.readUTF();

            //Serializar e guardar na lista
            Player p;
            p = new Player(points, name);
            players.add(p);

            try {
                //Guardando os dados do jogador
                ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("players.ser"));

                output.writeObject(players);
                output.close();
            } catch (Throwable t) {
                run = !run;
            }

            Collections.sort(players);
            sent.writeInt(players.indexOf(p) + 1);
            sent.close();
        }
        server.close();
    }

    public static class Player implements Serializable, Comparable {

        private int points;
        private String name;

        private Player(int points, String name) {
            this.points = points;
            this.name = name;
        }

        public long getPoints() {
            return this.points;
        }

        //Comparação para ordenar os players
        @Override
        public int compareTo(Object t) {
            long compare = ((Player) t).getPoints();
            return (int) ((int) compare - this.points);
        }
    }
}
