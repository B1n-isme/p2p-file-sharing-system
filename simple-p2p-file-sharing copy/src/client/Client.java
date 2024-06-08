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

public class Client {
    public JFrame frame;
	public JPanel panel;
    public JButton lookupButton;
    public JButton downloadButton;
    public JButton exitButton;
    public Peer peer;
    public String[] peerAddress = new String[0];
	public JButton runClientButton;
	public JLabel backgroundLabel;
	// public class BackgroundPanel extends JPanel {
    // private Image img;

    // public BackgroundPanel(String filename) {
    //     try {
    //         img = ImageIO.read(new File(filename));
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

    // @Override
    // protected void paintComponent(Graphics g) {
    //     super.paintComponent(g);
    //     g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
    // }
// }
	public JTextField createCustomTextField(String text) {
		JTextField field = new JTextField();
		field.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e){
				if (field.getText().equals(text)) {
					field.setForeground(Color.BLACK);
					field.setText("");
				}
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				if (field.getText().isEmpty()) {
					field.setForeground(Color.GRAY);
					field.setText(text);
				}
			}
		});
		return field;
	}

	public JTextField folderDirectoryField = createCustomTextField("Enter the folder directory here");
    public JTextField clientPortField = createCustomTextField("Enter the client port here");
    public JTextField serverAddressField = createCustomTextField("Enter the server address here");
    public JTextField serverPortField = createCustomTextField("Enter the server port here");
    public JTextField fileNameField;

    public Client() {
        frame = new JFrame("Peer UI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 300);
		frame.setLocationRelativeTo(null);

		// Set the background image
		// JFrame frame = new JFrame("Background Image");
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ImageIcon background = new ImageIcon("C:/Users/ADMIN/Desktop/code/java/DistributedSys/project/Bin/p2p-file-sharing-system/simple-p2p-file-sharing copy/background.jpg"); // Replace with your image path
		backgroundLabel = new JLabel();
		backgroundLabel.setIcon(background);
		backgroundLabel.setHorizontalAlignment(JLabel.CENTER);
		frame.add(backgroundLabel, BorderLayout.NORTH);


        panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        panel.add(new JLabel("Folder Directory:"));
		folderDirectoryField.setForeground(Color.GRAY);
		folderDirectoryField.setText("Enter the folder directory here");
        panel.add(folderDirectoryField);

        panel.add(new JLabel("Client Port:"));
		clientPortField.setForeground(Color.GRAY);
		clientPortField.setText("Enter the client port here");
        panel.add(clientPortField);

        panel.add(new JLabel("Server Address:"));
		serverAddressField.setForeground(Color.GRAY);
		serverAddressField.setText("Enter the server address here");
        panel.add(serverAddressField);

        panel.add(new JLabel("Server Port:"));
		serverPortField.setForeground(Color.GRAY);
		serverPortField.setText("Enter the server port here");
        panel.add(serverPortField);

        runClientButton = new JButton("Run Client");
		runClientButton.addActionListener(new runClientButtonListener());
		panel.add(runClientButton);

		// panel.setBackground(Color.WHITE);

		// bgpanel.add(panel, BorderLayout.CENTER);

		// Add panel to frame
		frame.add(panel);
		frame.setVisible(true);


        //
    }
	public class runClientButtonListener implements ActionListener{
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
	}

    public void runClient(String folderDirectory, int clientPort, String serverAddress, int serverPort) throws IOException {
        String dir = folderDirectory;
        File folder = new File(dir);
		String fileName = null;
        String address = InetAddress.getLocalHost().getHostAddress();

		//get all file names in folder
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
    	// String [] peerAddress = new String[0];

		// panel.removeAll();
		// panel.setLayout(new GridLayout(1,3)); // 1 row, 3 columns
		
		JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
		// buttonPanel.add(new JLabel("Options:"));
		lookupButton = new JButton("Lookup File");
		downloadButton = new JButton("Download File");
		exitButton = new JButton("Exit Server");
		
		buttonPanel.add(lookupButton);
		buttonPanel.add(downloadButton);
		buttonPanel.add(exitButton);

		frame.add(buttonPanel, BorderLayout.SOUTH);

		lookupButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.removeAll();
				panel.setLayout(new GridLayout(1,3));
				panel.add(new JLabel("File Name:"));
				fileNameField = new JTextField();
				panel.add(fileNameField);
				JButton lookupButton2 = new JButton("Lookup");
				lookupButton2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String fileName = fileNameField.getText();
						try {
							peerAddress = peer.lookup(fileName, new Socket(serverAddressField.getText(), Integer.parseInt(serverPortField.getText())), 1);
							// Display the lookup result
							System.out.println("Lookup result: " + peerAddress);
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
				});
				panel.add(lookupButton2);
				panel.revalidate();
				panel.repaint();
			}
		});

		downloadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fileName = fileNameField.getText();
				if (peerAddress.length == 0) {
					System.out.println("Lookup for the peer first.");
				} else if (peerAddress.length == 1 && Integer.parseInt(peerAddress[0].split(":")[2]) == peer.getPeerId()) {
					System.out.println("This peer has the file already, not downloading then.");
				} else if (peerAddress.length == 1) {
					String[] addrport = peerAddress[0].split(":");
					System.out.println("Downloading from peer " + addrport[2] + ": " + addrport[0] + ":" + addrport[1]);
					try {
						peer.download(addrport[0], Integer.parseInt(addrport[1]), fileName, -1);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
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
						try {
							peer.download(addrport[0], Integer.parseInt(addrport[1]), fileName, -1);
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
				}

			}
		});

		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Peer desconnected!");
				System.exit(0);
			}
		});

		// Refresh the panel
		// panel.revalidate();
		// panel.repaint();

		frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Simulate a loop with JOptionPane
        // while (true) {
        //     String[] options = {"Lookup File", "Download File", "Exit Server"};
        //     int choice = JOptionPane.showOptionDialog(frame, "Select an option:", "Peer GUI",
        //             JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
        //             null, options, options[0]);

        //     if (choice == 0) {
        //         lookupButton.doClick();
        //     } else if (choice == 1) {
        //         downloadButton.doClick();
        //     } else if (choice == 2) {
        //         exitButton.doClick();
        //         break;
        //     }
        // }
	}
		

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Client();
            }
        });
    }
}
