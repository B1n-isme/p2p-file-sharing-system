package client;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import util.Util;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class haha {
    private JFrame frame;
    private JTextField folderDirectoryField;
    private JTextField clientPortField;
    private JTextField serverAddressField;
    private JTextField serverPortField;

    public haha() {
        frame = new JFrame("Peer UI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        panel.add(new JLabel("Folder Directory:"));
        folderDirectoryField = new JTextField();
        panel.add(folderDirectoryField);

        panel.add(new JLabel("Client Port:"));
        clientPortField = new JTextField();
        panel.add(clientPortField);

        panel.add(new JLabel("Server Address:"));
        serverAddressField = new JTextField();
        panel.add(serverAddressField);

        panel.add(new JLabel("Server Port:"));
        serverPortField = new JTextField();
        panel.add(serverPortField);

        JButton button = new JButton("Run Client");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String folderDirectory = folderDirectoryField.getText();
                int clientPort = Integer.parseInt(clientPortField.getText());
                String serverAddress = serverAddressField.getText();
                int serverPort = Integer.parseInt(serverPortField.getText());

                try {
                    runClient(folderDirectory, clientPort, serverAddress, serverPort);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        panel.add(button);

        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    public void runClient(String folderDirectory, int clientPort, String serverAddress, int serverPort) throws IOException {
        String dir = folderDirectory;
        File folder = new File(dir);
        String address = InetAddress.getLocalHost().getHostAddress();
        ArrayList<String> fileNames = Util.listFilesForFolder(folder);
        final Peer peer = new Peer(dir, fileNames, fileNames.size(), address, clientPort);
        Socket socket = new Socket(serverAddress, serverPort);
        peer.register(socket);

        new Thread() {
            public void run() {
                try {
                    peer.server();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    peer.income();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Client();
            }
        });
    }
}