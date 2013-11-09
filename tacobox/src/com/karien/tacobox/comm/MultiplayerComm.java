package com.karien.tacobox.comm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

import com.karien.taco.mapstuff.Coord;

public class MultiplayerComm implements Runnable {
	private final Socket sock;
	
	private ArrayBlockingQueue<Coord> inActs = new ArrayBlockingQueue<Coord>(5);
	
	public MultiplayerComm(Socket s) {
		sock = s;
		
		new Thread(this).start();
	}
	
	/**
	 * Call this in some tick loop to see if an action from the other player.
	 * @return The coordinate send from the partner or null if there was none to read.
	 */
	public Coord recvAction() {
		return inActs.poll();
	}
	
	public static MultiplayerComm connect(int port) throws IOException {
		ServerSocket serv = new ServerSocket(port);
		
		Socket sock;
		while(true) {
			Socket rec = serv.accept();
			
			int d = rec.getInputStream().read();
			if (d == 42) {
				sock = rec;
				rec.getOutputStream().write(44);
				serv.close();
				break;
			} else {
				rec.close();
			}
		}
		
		return new MultiplayerComm(sock);
	}

	public static MultiplayerComm connect(String addr, int port) throws IOException {
		Socket s;
		while (true) {
			s = new Socket(addr, port);
			s.getOutputStream().write(42);
			
			if (s.getInputStream().read() != 44) {
				s.close();
			} else {
				break;
			}
		}
		
		return new MultiplayerComm(s);
	}

	@SuppressWarnings("resource")
	@Override
	public void run() {
		Scanner sc;
		try {
			sc = new Scanner(sock.getInputStream());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		while (true) {
			String line = sc.nextLine();
			
			int comma = line.indexOf(',');
			if (comma == -1) {
				throw new RuntimeException("Bad line recv: " + line);
			}
			Coord c;
			try {
				c = new Coord(
						Integer.parseInt(line.substring(0, comma).trim()), 
						Integer.parseInt(line.substring(comma+1).trim())
				);
			}catch (NumberFormatException x) {
				throw new RuntimeException("Invalid line format (bad integer): " + line);
			}
			
			
			// We want an exception on full because something's wrong in that case.
			inActs.add(c);
		}
	}
}
