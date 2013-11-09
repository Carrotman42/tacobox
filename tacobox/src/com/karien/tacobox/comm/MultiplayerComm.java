package com.karien.tacobox.comm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

import com.karien.taco.mapstuff.ActionMessage;

public class MultiplayerComm implements Runnable, MsgHandler {
	private final Socket sock;

	private ArrayBlockingQueue<ActionMessage> inActs = new ArrayBlockingQueue<ActionMessage>(5);
	
	public MultiplayerComm(Socket s) {
		sock = s;
		
		new Thread(this).start();
	}
	
	/**
	 * Call this in some tick loop to see if an action from the other player.
	 * @return The coordinate send from the partner or null if there was none to read.
	 */
	public ActionMessage recvAction() {
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
			
			ActionMessage msg = ActionMessage.fromString(line);

			// We want an exception on full because something's wrong in that case.
			inActs.add(msg);
		}
	}

	@Override
	public void postMessage(ActionMessage msg) throws IOException {
		// TODO: Check that this won't block so that we don't block the gui thread
		
		sock.getOutputStream().write(msg.toString().getBytes());
	}
}
