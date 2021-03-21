package appl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import core.Message;

public class OneAppl {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		new OneAppl(true);
	}

	public OneAppl() {
		PubSubClient client = new PubSubClient();
		client.startConsole();
	}

	public OneAppl(boolean flag) throws InterruptedException {
		Scanner reader = new Scanner(System.in);  // Reading from System.in
		System.out.print("Enter the Broker address (ex. localhost): ");
		String brokerAddress = reader.next();
		System.out.print("Enter the Broker port (ex.8080): ");
		int brokerPort = reader.nextInt();
		PubSubClient joubert = new PubSubClient(brokerAddress, 8080);
		//PubSubClient debora = new PubSubClient(brokerAddress, 8082);
		//PubSubClient jonata = new PubSubClient(brokerAddress, 8083);

		
		joubert.subscribe(brokerAddress, brokerPort);
		//debora.subscribe(brokerAddress, brokerPort);
		//jonata.subscribe(brokerAddress, brokerPort);
		Thread accessOne = new ThreadSincronized(joubert, "x", "joubert", brokerAddress, brokerPort);
		//Thread accessTwo = new ThreadSincronized(debora, "x", "jonata", brokerAddress, brokerPort);
		//Thread accessThree = new ThreadSincronized(jonata, "x", "joubert", brokerAddress, brokerPort);
		//Thread accessFour = new ThreadSincronized(jonata, "x", "debora", brokerAddress, brokerPort);
		// "localhost", 8080);

		accessOne.start();
		/*accessTwo.start();
		accessThree.start();
		accessFour.start();*/

		try {
			//accessTwo.join();
			accessOne.join();
			//accessThree.join();
			//accessFour.join();
		} catch (Exception e) {

		}
		/*
		 * Set<Message> logJoubert = joubert.getLogMessages(); Set<Message> logDebora =
		 * debora.getLogMessages(); Set<Message> logJonata = jonata.getLogMessages();
		 * Iterator<Message> it = logJoubert.iterator();
		 * 
		 * Message first = it.next(); String content = first.getContent();
		 * System.out.print(content);
		 * 
		 * if (content.equals("joubert_acquire_x")) {
		 * System.out.print("\nAcessando X\n"); TimeUnit.SECONDS.sleep(1); accessOne =
		 * new ThreadWrapper(joubert, "joubert_release_x", "localhost", 8080);
		 * accessOne.start(); try { accessOne.join(); } catch (Exception e) { } } else
		 * if (content.equals("debora_acquire_x")) {
		 * System.out.print("\nAcessando X\n"); TimeUnit.SECONDS.sleep(1); accessTwo =
		 * new ThreadWrapper(debora, "debora_release_x", "localhost", 8080);
		 * accessTwo.start(); try { accessTwo.join(); } catch (Exception e) { } } else
		 * if (content.equals("jonata_acquire_x")) {
		 * System.out.print("\nAcessando X\n"); TimeUnit.SECONDS.sleep(1); accessThree =
		 * new ThreadWrapper(jonata, "jonata_release_x", "localhost", 8080);
		 * accessThree.start(); try { accessThree.join(); } catch (Exception e) { } }
		 * 
		 * it = logJonata.iterator(); System.out.print("Log Jonata itens: "); while
		 * (it.hasNext()) { Message aux = it.next(); System.out.print(aux.getContent() +
		 * aux.getLogId() + " | "); } System.out.println();
		 * 
		 * it = logDebora.iterator(); System.out.print("Log Debora itens: "); while
		 * (it.hasNext()) { Message aux = it.next(); System.out.print(aux.getContent() +
		 * aux.getLogId() + " | "); } System.out.println();
		 */
		joubert.stopPubSubClient();
		//debora.stopPubSubClient();
		//jonata.stopPubSubClient();
	}

	class ThreadWrapper extends Thread {
		PubSubClient c;
		String msg;
		String host;
		int port;

		public ThreadWrapper(PubSubClient c, String msg, String host, int port) {
			this.c = c;
			this.msg = msg;
			this.host = host;
			this.port = port;
		}

		public void run() {
			c.publish(msg, host, port);
		}
	}

	class ThreadSincronized extends Thread {
		PubSubClient c;
		String topic;
		String client_name;
		String host;
		int port;

		public ThreadSincronized(PubSubClient c, String topic, String client_name, String host, int port) {
			this.c = c;
			this.topic = topic;
			this.client_name = client_name;
			this.host = host;
			this.port = port;
		}

		public void run() {
			Thread access = new ThreadWrapper(c, client_name.concat("_acquire_x"), host, port);
			access.start();
			try {
				access.join();
			} catch (Exception e) {
			}
			/*
			 * Thread access = new ThreadWrapper(c, "joubert_acquire_x", "localhost", 8080);
			 * access.start(); try { access.join(); } catch (Exception e) { }
			 * 
			 * access = new ThreadWrapper(c, "jonata_acquire_x", "localhost", 8080);
			 * access.start(); try { access.join(); } catch (Exception e) { }
			 * 
			 * access = new ThreadWrapper(c, "joubert_acquire_x", "localhost", 8080);
			 * access.start(); try { access.join(); } catch (Exception e) { }
			 * 
			 * access = new ThreadWrapper(c, "debora_acquire_x", "localhost", 8080);
			 * access.start(); try { access.join(); } catch (Exception e) { }
			 * 
			 * access = new ThreadWrapper(c, "joubert_release_x", "localhost", 8080);
			 * access.start(); try { access.join(); } catch (Exception e) { }
			 * 
			 * access = new ThreadWrapper(c, "jonata_release_x", "localhost", 8080);
			 * access.start(); try { access.join(); } catch (Exception e) { }
			 * 
			 * access = new ThreadWrapper(c, "joubert_release_x", "localhost", 8080);
			 * access.start(); try { access.join(); } catch (Exception e) { }
			 */

			while (true) {
				try {
					Thread.currentThread().sleep(5000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} // 1 segundo

				Set<Message> log = c.getLogMessages();
				ArrayList<String> log2 = new ArrayList<String>();
				Iterator<Message> it = log.iterator();
				while (it.hasNext()) {
					Message i = it.next();
					String content = i.getContent();
					log2.add(content);
				}
				it = log.iterator();
				while (it.hasNext()) {
					Message i = it.next();
					String content = i.getContent();
					String[] parts = content.split("_");
					if (parts[1].equals("release")) {
						Iterator<Message> it2 = log.iterator();
						while (it2.hasNext()) {
							Message j = it2.next();
							String content2 = j.getContent();
							String[] parts2 = content2.split("_");

							if (parts2[0].equals(parts[0])) {
								log2.remove(content2);
								break;
							}
						}
					}
				}

				Iterator<String> it2 = log2.iterator();
				String first = it2.next();

				if (first.equals(client_name + "_acquire_x")) {
					
					System.out.print("\nAcessando X\n");
					/*
					 * try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e1) { // TODO
					 * Auto-generated catch block e1.printStackTrace(); }
					 */
					access = new ThreadWrapper(c, client_name.concat("_release_x"), host, port);
					access.start();
					try {
						access.join();
					} catch (Exception e) {
					}
					break;
				}
			}
			Set<Message> log = c.getLogMessages();
			Iterator<Message> it = log.iterator();
			it = log.iterator();
			while (it.hasNext()) {
				Message aux = it.next();
				System.out.print(aux.getContent() + aux.getLogId() + " | ");
			}
			System.out.println();

		}
	}

}