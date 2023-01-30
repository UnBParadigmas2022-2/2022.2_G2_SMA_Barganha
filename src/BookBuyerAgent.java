/*****************************************************************
JADE - Java Agent DEvelopment Framework is a framework to develop
multi-agent systems in compliance with the FIPA specifications.
Copyright (C) 2000 CSELT S.p.A.

GNU Lesser General Public License

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation,
version 2.1 of the License.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the
Free Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA  02111-1307, USA.
*****************************************************************/


import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.ArrayList;
import java.util.List;


public class BookBuyerAgent extends Agent {
	

	private static final long serialVersionUID = 1L;
	// The title of the book to buy
	private String targetBookTitle;
	// The quality of the book to buy
	private String wantedBookQuality;
	// The amount of money that buyer agent has
	private int proposal = 130; // The buyer will send 130 for the target book as a proposal
	// The list of known seller agents
	private AID[] sellerAgents;

	private String bookTitles;

	private BookBuyerGui myGui;

	private BookBuyerAgent thisAgent;

	// Put agent initializations here
	protected void setup() {
		thisAgent = this;
		// Printout a welcome message
		System.out.println("Hallo! Buyer-agent "+getAID().getName()+" is ready.");

		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("book-selling");
		template.addServices(sd);
		try {
			DFAgentDescription[] result = DFService.search(this, template);
			System.out.println("Found the following seller agents:");
			sellerAgents = new AID[result.length];
			for (int i = 0; i < result.length; ++i) {
				sellerAgents[i] = result[i].getName();
				System.out.println(sellerAgents[i].getName());
			}
			addBehaviour(new RequestPerformerBookList());
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}


	/**
	 Inner class RequestPerformer.
	 This is the behaviour used by Book-buyer agents to request seller
	 agents the target book.
	 */
	private class RequestPerformer extends Behaviour {

		private static final long serialVersionUID = 1L;
		private AID bestSeller; // The agent who provides the best offer

		private int repliesCnt = 0; // The counter of replies from seller agents
		private MessageTemplate mt; // The template to receive replies
		private int step = 0;
		private int bestPrice = proposal;  // The best offered price
		private int currentSeller = 0;
		private String finalQuality; // The quality of the book sold

		public void action() {
			switch (step) {
			case 0:
				if (currentSeller > sellerAgents.length) {
					step = 4;
					break;
				}

				// Send the cfp to all sellers
				ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
				cfp.addReceiver(sellerAgents[currentSeller]);
				cfp.setContent(targetBookTitle);
				cfp.setConversationId("book-trade");
				cfp.setReplyWith("cfp"+System.currentTimeMillis()); // Unique value
				myAgent.send(cfp);
				// Prepare the template to get proposals
				mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
						MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
				step = 1;
				break;
			case 1:
				// Receive all proposals/refusals from seller agent
				ACLMessage reply = myAgent.receive(mt);
				if (reply != null) {
					// Reply received
					if (reply.getPerformative() == ACLMessage.PROPOSE) {
						// This is an offer 
						// Separando resposta 
						String text = reply.getContent();
						String[] resp = text.split(";");
						String quality = resp[0];
						int price = Integer.parseInt(resp[1]);
						while (price > proposal) {
							int priceSeller = askSellerToReducePrice(reply.getSender(), price);
							// if the seller do not want to lower the price, the buyer will ask the next seller
							if (priceSeller == price) {
								System.out.println("Seller " + reply.getSender().getName() + " do not want to lower the price");
								break;
							}

							price = priceSeller;
						}
						
						switch(wantedBookQuality) {
						case "Novo":
							if(quality.equals("Novo")) {
								if (bestSeller == null || price < bestPrice) {
									// This is the best offer at present
									bestPrice = price;
									bestSeller = reply.getSender();
									finalQuality = quality;
								}
							}
							break;
						case "Seminovo":
							if(quality.equals("Novo") || quality.equals("Seminovo")) {
								if (bestSeller == null || price < bestPrice) {
									// This is the best offer at present
									bestPrice = price;
									bestSeller = reply.getSender();
									finalQuality = quality;
								}
							}
							break;
						case "Usado":
							if (bestSeller == null || price < bestPrice) {
								// This is the best offer at present
								bestPrice = price;
								bestSeller = reply.getSender();
								finalQuality = quality;
							}
						}
					}
					repliesCnt++;
					if (repliesCnt >= sellerAgents.length) {
						// We received all replies
						step = 2;
					} else {
						currentSeller = currentSeller + 1;
						step = 0;
					}
				}
				else {
					block();
				}
				break;
			case 2:
				// Send the purchase order to the seller that provided the best offer
				if (bestPrice > proposal) {
					System.out.println("Unfortunately, there is no Seller with the desired price! My maximum price is: " + proposal + " and the best offer was: " + bestPrice);
					myAgent.doDelete();
				}
				else {
					System.out.println("Send the purchase order to the seller that provided the best offer");

					ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
					order.addReceiver(bestSeller);
					order.setContent(targetBookTitle);
					order.setConversationId("book-trade");
					order.setReplyWith("order"+System.currentTimeMillis());
					myAgent.send(order);
					// Prepare the template to get the purchase order reply
					mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
							MessageTemplate.MatchInReplyTo(order.getReplyWith()));
					step = 3;
				}
				break;
			case 3:
				// Receive the purchase order reply
				reply = myAgent.receive(mt);
				if (reply != null) {
					// Purchase order reply received
					if (reply.getPerformative() == ACLMessage.INFORM) {
						// Purchase successful. We can terminate
						System.out.println(targetBookTitle+" successfully purchased from agent "+reply.getSender().getName());
						System.out.println("Price = "+bestPrice);
						System.out.println("Quality = "+finalQuality);
						myAgent.doDelete();
					}
					else {
						System.out.println("Attempt failed: requested book already sold.");
					}

					step = 4;
				}
				else {
					block();
				}
				break;
			}
		}

		public boolean done() {

			if (step == 2 && bestSeller == null) {
				System.out.println("Attempt failed: "+targetBookTitle+" not available for sale");
			}

			boolean bookIsNotAvailable = (step == 2 && bestSeller == null);
			boolean negotiationIsConcluded = (step == 4);

			boolean isDone = false;
			if (bookIsNotAvailable || negotiationIsConcluded) {
				isDone = true;
			}
			else {
				isDone = false;
			}

			return isDone;
			//return ((step == 2 && bestSeller == null) || step == 4);
		}

		// this function asks the seller to lower the price
		public int askSellerToReducePrice(AID seller, int price) {
			int proposal = price - 5;
			int sellerPrice = price;

			// create a message to ask the seller to lower the price
			ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
			cfp.addReceiver(seller);
			cfp.setContent(targetBookTitle+"/"+proposal);
			cfp.setConversationId("book-negotiation");
			cfp.setReplyWith("cfp"+System.currentTimeMillis()); // Unique value
			myAgent.send(cfp);
			// Prepare the template to get proposal
			mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-negotiation"),
					MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));

			// Receive proposal from the seller
			doWait();
			ACLMessage reply = myAgent.receive(mt);

			if (reply != null) {
				// Reply received
				if (reply.getPerformative() == ACLMessage.PROPOSE) {
					// This is an offer
					String offer = reply.getContent();
					String[] offer2 = offer.split(";");
					sellerPrice = Integer.parseInt(offer2[1]);
					System.out.println("Seller-agent "+seller.getName()+" accepted to reduce the price to "+sellerPrice+" from buyer "+getAID().getName());
				}
			} else {
				block();
			}

			return sellerPrice;
		}
	}  // End of inner class RequestPerformer

	private class RequestPerformerBookList extends Behaviour {

		private static final long serialVersionUID = 1L;
		private MessageTemplate mt; // The template to receive replies
		private int step = 0;
		private int repliesCnt = 0;
		StringBuilder sb = new StringBuilder();

		public void action() {
			switch (step) {
			case 0:
				// Send the cfp to all sellers
				ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
				for (int i = 0; i < sellerAgents.length; ++i) {
					cfp.addReceiver(sellerAgents[i]);
				}
				cfp.setContent("all-books");
				cfp.setConversationId("book-trade");
				cfp.setReplyWith("cfp"+System.currentTimeMillis()); // Unique value
				myAgent.send(cfp);
				// Prepare the template to get proposals
				mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
						MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
				step = 1;
				break;
			case 1:
				// Receive all proposals/refusals from seller agents
				ACLMessage reply = myAgent.receive(mt);
				if (reply != null) {
					if(reply.getPerformative() == ACLMessage.PROPOSE) {
						sb.append(reply.getContent());
					}
					repliesCnt++;
					if (repliesCnt >= sellerAgents.length) {
						// We received all replies
						bookTitles = sb.toString();
						System.out.println("Lista de livros recebida: " + bookTitles);
						myGui = new BookBuyerGui(thisAgent);
						myGui.showGui();
						step = 2;
					}
				}
				else {
					block();
				}
				break;
			}
		}

		public boolean done() {

			boolean listRetreived = (step == 2);

			boolean isDone = false;
			if (listRetreived) {
				isDone = true;
			}
			else {
				isDone = false;
			}

			return isDone;
		}
	}

	public String[] getBookTitles() {
		String[] values = bookTitles.split("/");
		List<String> finalResult = new ArrayList<>();
		finalResult.add(values[0]);

		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < finalResult.size(); j++) {
				if (values[i].equals(finalResult.get(j))) {
					break;
				}
				if (j == finalResult.size() - 1) {
					finalResult.add(values[i]);
				}
			}
		}

		return finalResult.toArray(new String[0]);
	}

	public void performBuyRequest(String choosenBookTitle, String proposalPrice, String qualityBook) {
		targetBookTitle = choosenBookTitle;
		wantedBookQuality = qualityBook;
		try {
			proposal = Integer.parseInt(proposalPrice);
		} catch(Exception e) {
			System.out.println();
		}


		// Add a TickerBehaviour that schedules a request to seller agents every 10 seconds
		addBehaviour(new TickerBehaviour(this, 10000) {
			private static final long serialVersionUID = 1L;

			protected void onTick() {
				System.out.println("Trying to buy "+targetBookTitle);
				// Update the list of seller agents
				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType("book-selling");
				template.addServices(sd);
				try {
					DFAgentDescription[] result = DFService.search(myAgent, template);
					System.out.println("Found the following seller agents:");
					sellerAgents = new AID[result.length];
					for (int i = 0; i < result.length; ++i) {
						sellerAgents[i] = result[i].getName();
						System.out.println(sellerAgents[i].getName());
					}
				}
				catch (FIPAException fe) {
							fe.printStackTrace();
				}
				// Perform the request
				thisAgent.addBehaviour(new RequestPerformer());
			}
		});
	}

	// Put agent clean-up operations here
	protected void takeDown() {
		myGui.dispose();
		// Printout a dismissal message
		System.out.println("Buyer-agent "+getAID().getName()+" terminating.");
	}

}

