// package client;

// import java.io.File;
// import java.io.IOException;
// import java.net.InetAddress;
// import java.net.Socket;
// import java.util.ArrayList;
// import java.util.Scanner;

// import util.Util;

// public class Client {
	
// 	public static void main(String[] args) throws IOException {
    	
// 		if(args.length < 2){
//     		System.out.println("It should be java client/Client folder port");
//     		return;
//     	}
		
//     	//Server information
//     	String serverAddress = "localhost";
//     	int serverPort = 3434;
    	
//     	if(args.length > 2){
//     		try{
//     			serverAddress = args[2];
//         		serverPort = Integer.parseInt(args[3]);
//     		} catch(Exception e){
//     			System.out.println("It should be java client/Client folder port serverAddress serverPort");
//     		}
    		
//     	}
    	
//     	String dir = args[0];
//     	File folder = new File(dir);
//     	int option;
//     	String fileName = null;
    	
//     	if(!folder.isDirectory()){
// 			System.out.println("Put a valid directory name");
// 			return;
//     	}
    	
//     	//Util.getExternalIP();
    	
//     	String address = InetAddress.getLocalHost().getHostAddress();
//     	int port = 3434;
//     	try{
//     		port = Integer.parseInt(args[1]);
//     	} catch (Exception e){
//     		System.out.println("Put a valid port number");
//     	}
    	
//     	ArrayList<String> fileNames = Util.listFilesForFolder(folder);
//     	final Peer peer = new Peer(dir, fileNames, fileNames.size(), address, port);
//     	Socket socket = null;
//     	try {
//     		socket = new Socket(serverAddress, serverPort);
//     	}catch (IOException e){
//     		System.out.println("There isn't any instance of server running. Start one first!");
//     		return;
//     	}
//     	peer.register(socket);
    	
//     	new Thread(){
//     		public void run(){
//     			try {
// 					peer.server();
// 				} catch (IOException e) {
// 					e.printStackTrace();
// 				}
//     		}
//     	}.start();
    	
//     	new Thread(){
//     		public void run(){
//     			try {
// 					peer.income();
// 				} catch (IOException e) {
// 					e.printStackTrace();
// 				}
//     		}
//     	}.start();
    	
//     	String [] peerAddress = new String[0];
    	
//     	Scanner scanner = new Scanner(System.in);
//     	while(true){
//     		System.out.println("\n\nSelect the option:");
//     		System.out.println("1 - Lookup for a file");
//     		System.out.println("2 - Download file");
// 			System.out.println("3 - Exit");
    		
//     		option = scanner.nextInt();
//     		int optpeer;
    		
//     		if(option == 1){
//     			System.out.println("Enter file name:");
//     			fileName = scanner.next();
// 				peerAddress = peer.lookup(fileName, new Socket(serverAddress, serverPort), 1);
//     		}
//     		else if (option == 2){
//     			if(peerAddress.length == 0){
//     				System.out.println("Lookup for the peer first.");
//     			}else if(peerAddress.length == 1 && Integer.parseInt(peerAddress[0].split(":")[2]) == peer.getPeerId()){
//     				System.out.println("This peer has the file already, not downloading then.");
//     			}else if(peerAddress.length == 1){
//     				String[] addrport = peerAddress[0].split(":");
//     				System.out.println("Downloading from peer " + addrport[2] + ": " + addrport[0] + ":" + addrport[1]);
//     				peer.download(addrport[0], Integer.parseInt(addrport[1]), fileName, -1);
//     			}else {
//     				System.out.println("Select from which peer you want to Download the file:");
//     				for(int i = 0; i < peerAddress.length; i++){
//     					String[] addrport = peerAddress[i].split(":");
//     					System.out.println((i+1) + " - " + addrport[0] + ":" + addrport[1]);
//     				}
//     				optpeer = scanner.nextInt();
//     				while(optpeer > peerAddress.length || optpeer < 1){
//     					System.out.println("Select a valid option:");
//     					optpeer = scanner.nextInt();
//     				}
//     				String[] addrport = peerAddress[optpeer-1].split(":");
//     				peer.download(addrport[0], Integer.parseInt(addrport[1]), fileName, -1);
//     			}
//     		}else if (option == 3){
//     			scanner.close();
//     			System.out.println("Peer desconnected!");
//     			return;
//     		}
    		
//     	}
//     }
// }





package client;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import util.Util;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Client {
    private JFrame frame;
    private JTextField folderDirectoryField;
    private JTextField clientPortField;
    private JTextField serverAddressField;
    private JTextField serverPortField;
    private JTextField fileNameField;
    private JButton lookupButton;
    private JButton downloadButton;
    private JButton exitButton;
    private Peer peer;
    private String[] peerAddress;

    public Client() {
        frame = new JFrame("Peer UI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

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

        JButton runClientButton = new JButton("Run Client");
        runClientButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String folderDirectory = folderDirectoryField.getText();
                int clientPort = Integer.parseInt(clientPortField.getText());
                String serverAddress = serverAddressField.getText();
                int serverPort = Integer.parseInt(serverPortField.getText());

                try {
                    runClient(folderDirectory, clientPort, serverAddress, serverPort);
					frame.removeAll();
					frame.revalidate();
                    frame.repaint();
                    panel.add(new JLabel("Options:"));
                    JButton lookupButton = new JButton("Lookup File");
                    lookupButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            // Add lookup file functionality here
                        }
                    });
                    panel.add(lookupButton);

                    JButton downloadButton = new JButton("Download File");
                    downloadButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            // Add download file functionality here
                        }
                    });
                    panel.add(downloadButton);

                    JButton exitButton = new JButton("Exit Server");
                    exitButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            // Add exit server functionality here
                        }
                    });
                    panel.add(exitButton);


                    frame.pack();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        panel.add(runClientButton);


        // panel.add(new JLabel("File Name:"));
        // fileNameField = new JTextField();
        // panel.add(fileNameField);

        // lookupButton = new JButton("Lookup");
        // lookupButton.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e) {
        //         String fileName = fileNameField.getText();
        //         try {
        //             peerAddress = peer.lookup(fileName, new Socket(serverAddressField.getText(), Integer.parseInt(serverPortField.getText())), 1);
        //             // Display the lookup result
        //             System.out.println("Lookup result: " + peerAddress);
        //         } catch (IOException ex) {
        //             ex.printStackTrace();
        //         }
        //     }
        // });
        // panel.add(lookupButton);

        // downloadButton = new JButton("Download");
        // downloadButton.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e) {
        //         String fileName = fileNameField.getText();
        //         if (peerAddress.length == 0) {
        //             System.out.println("Lookup for the peer first.");
        //         } else if (peerAddress.length == 1 && Integer.parseInt(peerAddress[0].split(":")[2]) == peer.getPeerId()) {
        //             System.out.println("This peer has the file already, not downloading then.");
        //         } else if (peerAddress.length == 1) {
        //             String[] addrport = peerAddress[0].split(":");
        //             System.out.println("Downloading from peer " + addrport[2] + ": " + addrport[0] + ":" + addrport[1]);
        //             try {
        //                 peer.download(addrport[0], Integer.parseInt(addrport[1]), fileName, -1);
        //             } catch (IOException ex) {
        //                 ex.printStackTrace();
        //             }
        //         } else {
        //             // Display a dialog to select the peer to download from
        //             String[] options = new String[peerAddress.length];
        //             for (int i = 0; i < peerAddress.length; i++) {
        //                 String[] addrport = peerAddress[i].split(":");
        //                 options[i] = addrport[0] + ":" + addrport[1];
        //             }
        //             String selectedPeer = (String) JOptionPane.showInputDialog(frame, "Select the peer to download from:", "Download File",
        //                     JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        //             if (selectedPeer != null) {
        //                 String[] addrport = selectedPeer.split(":");
        //                 try {
        //                     peer.download(addrport[0], Integer.parseInt(addrport[1]), fileName, -1);
        //                 } catch (IOException ex) {
        //                     ex.printStackTrace();
        //                 }
        //             }
        //         }
        //     }
        // });
        // panel.add(downloadButton);

        // exitButton = new JButton("Exit");
        // exitButton.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e) {
        //         System.exit(0);
        //     }
        // });
        // panel.add(exitButton);

        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    public void runClient(String folderDirectory, int clientPort, String serverAddress, int serverPort) throws IOException {
        String dir = folderDirectory;
        File folder = new File(dir);
        String address = InetAddress.getLocalHost().getHostAddress();
        ArrayList<String> fileNames = Util.listFilesForFolder(folder);
        peer = new Peer(dir, fileNames, fileNames.size(), address, clientPort);
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

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n\nSelect the option:");
            System.out.println("1 - Lookup for a file");
            System.out.println("2 - Download file");
            System.out.println("3 - Exit");

            int option = scanner.nextInt();

            if (option == 1) {
                System.out.println("Enter file name:");
                String fileName = scanner.next();
                peerAddress = peer.lookup(fileName, new Socket(serverAddress, serverPort), 1);
                // Display the lookup result
                System.out.println("Lookup result: " + peerAddress);
            } else if (option == 2) {
                System.out.println("Enter file name:");
                String fileName = scanner.next();
                if (peerAddress.length == 0) {
                    System.out.println("Lookup for the peer first.");
                } else if (peerAddress.length == 1 && Integer.parseInt(peerAddress[0].split(":")[2]) == peer.getPeerId()) {
                    System.out.println("This peer has the file already, not downloading then.");
                } else if (peerAddress.length == 1) {
                    String[] addrport = peerAddress[0].split(":");
                    System.out.println("Downloading from peer " + addrport[2] + ": " + addrport[0] + ":" + addrport[1]);
                    peer.download(addrport[0], Integer.parseInt(addrport[1]), fileName, -1);
                } else {
                    // Display a dialog to select the peer to download from
                    String[] options = new String[peerAddress.length];
                    for (int i = 0; i < peerAddress.length; i++) {
                        String[] addrport = peerAddress[i].split(":");
                        options[i] = addrport[0] + ":" + addrport[1];
                    }
                    String selectedPeer = (String) JOptionPane.showInputDialog(frame, "Select the peer to download from:", "Download File",
                            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if (selectedPeer != null) {
                        String[] addrport = selectedPeer.split(":");
                        peer.download(addrport[0], Integer.parseInt(addrport[1]), fileName, -1);
                    }
                }
            } else if (option == 3) {
                scanner.close();
                System.out.println("Peer disconnected!");
                return;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Client();
            }
        });
    }
}
