package appl;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
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
		Scanner reader = new Scanner(System.in); // Reading from System.in
		String brokerAddress = "10.128.0.2";
		int brokerPort = 8080;
		// System.out.print("Enter the Broker port (ex.8080): ");
		// int brokerPort = reader.nextInt();

		// System.out.print("Enter the Client address (ex. 10.128.0.3): ");
		// String clientAddress = reader.next();
		String clientAddress = "10.128.0.4";

		PubSubClient joubert = new PubSubClient(clientAddress, 8081);
		PubSubClient debora = new PubSubClient(clientAddress, 8082);
		PubSubClient jonata = new PubSubClient(clientAddress, 8083);

		joubert.subscribe(brokerAddress, brokerPort);
		debora.subscribe(brokerAddress, brokerPort);
		jonata.subscribe(brokerAddress, brokerPort);

		/*Thread accessOne = new ThreadSincronized(joubert, "x", "alan", brokerAddress, brokerPort, 1);
		Thread accessTwo = new ThreadSincronized(debora, "x", "roberto", brokerAddress, brokerPort, 1);
		Thread accessThree = new ThreadSincronized(joubert, "x", "luis", brokerAddress, brokerPort, 1);
		Thread accessFour = new ThreadSincronized(debora, "x", "luis", brokerAddress, brokerPort, 2);
		Thread accessFive = new ThreadSincronized(jonata, "x", "alan", brokerAddress, brokerPort, 2);
		Thread accessSix = new ThreadSincronized(debora, "x", "roberto", brokerAddress, brokerPort, 2);
		Thread accessSeven = new ThreadSincronized(joubert, "x", "alan", brokerAddress, brokerPort, 3);
		*/

		Thread accessOne = new ThreadSincronized(joubert, "x", "joubert", brokerAddress, brokerPort, 1);
		Thread accessTwo = new ThreadSincronized(debora, "x", "debora", brokerAddress, brokerPort, 1);
		Thread accessThree = new ThreadSincronized(joubert, "x", "joubert", brokerAddress, brokerPort, 2);
		Thread accessFour = new ThreadSincronized(debora, "x", "debora", brokerAddress, brokerPort, 2);
		Thread accessFive = new ThreadSincronized(jonata, "x", "jonata", brokerAddress, brokerPort, 1);
		Thread accessSix = new ThreadSincronized(debora, "x", "debora", brokerAddress, brokerPort, 3);
		Thread accessSeven = new ThreadSincronized(joubert, "x", "joubert", brokerAddress, brokerPort, 3);

		accessOne.start();
		accessTwo.start();
		accessThree.start();
		accessFour.start();
		accessFive.start();
		accessSix.start();
		accessSeven.start();
		try {
			accessTwo.join();
			accessOne.join();
			accessThree.join();
			accessFour.join();
			accessFive.join();
			accessSix.join();
			accessSeven.join();
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
		debora.stopPubSubClient();
		jonata.stopPubSubClient();
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

		public ThreadSincronized(PubSubClient c, String topic, String client_name, String host, int port, int id) {
			this.c = c;
			this.topic = topic;
			this.client_name = Integer.toString(id).concat(client_name);
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

			Set<Message> log5 = c.getLogMessages();
			Iterator<Message> it5 = log5.iterator();
			it5 = log5.iterator();
			while (it5.hasNext()) {
				Message aux = it5.next();
				System.out.print(aux.getContent() + aux.getLogId() + " | ");
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
					Thread.currentThread().sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} // 1 segundo
				
				Set<Message> log = c.getLogMessages();
				ArrayList<Message> log2 = new ArrayList <Message>();
				log2.clear();
				log2.addAll(log);
				Iterator<Message> it = log.iterator();
				it = log.iterator();
				while (it.hasNext()) {
					Message i = it.next();
					String content = i.getContent();
					try {
						String[] parts = content.split("_");
						/*if (parts[1].equals("acquire")) {
							log2.add(content);
						}*/

						if (parts[1].equals("release")) {
							Iterator<Message> it2 = log.iterator();
							while (it2.hasNext()) {
								Message j = it2.next();
								String content2 = j.getContent();
								String[] parts2 = content2.split("_");

								if (parts2[0].equals(parts[0])) {
									log2.remove(j);
									break;
								}
							}
						}
					} catch (Exception e) {
					}
				}

				Iterator<Message> it2 = log2.iterator();
				String first = it2.next().getContent();

				if (first.equals(client_name + "_acquire_x")) {

					System.out.print("\nAcessando X\n");
					try {
						Thread.currentThread().sleep(2000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} // 1 segundo

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