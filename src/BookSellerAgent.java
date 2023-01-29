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

//package examples.bookTrading;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.*;

public class BookSellerAgent extends Agent {
	/**
	 * 
	 */
	
	public class caracteristicas{
		private int price;
		private String quality;
		
		// Constructor
		public caracteristicas(int price, String quality) {
			this.price = price;
			this.quality = quality;
		}
		
		// get price
		public int getPrice() {
			return price;
		}		
		
		// get quality
		public String getQuality() {
			return quality;
		}
	}
	
	private static final long serialVersionUID = 1L;
	// The catalogue of books for sale (maps the title of a book to its price)
	/*private Hashtable<String,Integer> cataloguePrice;
	private Hashtable<String,String> catalogueQuality;*/
	private Hashtable<String, caracteristicas> catalogue;
	// The GUI by means of which the user can add books in the catalogue
	private BookSellerGui myGui;

	// Put agent initializations here
	protected void setup() {
		// Create the catalogue
		/*cataloguePrice = new Hashtable<String,Integer>();
		catalogueQuality = new Hashtable<String,String>();
		*/
		catalogue = new Hashtable<String, caracteristicas>();
		
		// Create and show the GUI 
		myGui = new BookSellerGui(this);
		myGui.showGui();

		// Register the book-selling service in the yellow pages
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("book-selling");
		sd.setName("JADE-book-trading");
		dfd.addServices(sd);	
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}

		// Add the behaviour serving queries from buyer agents
		// Servidor de Requisições de Ofertas
		addBehaviour(new OfferRequestsServer());

		// Add the behaviour serving purchase orders from buyer agents
		// Servidor de Pedidos de Compras
		addBehaviour(new PurchaseOrdersServer());
	}

	/**
	   Inner class OfferRequestsServer.
	   This is the behaviour used by Book-seller agents to serve incoming requests 
	   for offer from buyer agents.
	   If the requested book is in the local catalogue the seller agent replies 
	   with a PROPOSE message specifying the price. Otherwise a REFUSE message is
	   sent back.
	 */
	// Servidor de Requisições de Ofertas
	//FIPA PROTOCOLS: http://www.fipa.org/specs/fipa00030/
	private class OfferRequestsServer extends CyclicBehaviour {

		private static final long serialVersionUID = 1L;

		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				// CFP Message received. Process it
				String title = msg.getContent();
				ACLMessage replyPrice = msg.createReply();
				// ACLMessage replyQuality = msg.createReply();

				caracteristicas price = (caracteristicas) catalogue.get(title);
				// String quality = catalogueQuality.get(title);
				if (price != null) {
					// The requested book is available for sale. Reply with the price
					replyPrice.setPerformative(ACLMessage.PROPOSE);
					replyPrice.setContent(String.valueOf(price.getPrice()));
					/*
					replyQuality.setPerformative(ACLMessage.PROPOSE);
					replyQuality.setContent(String.valueOf(quality));
					*/
				}
				else {
					// The requested book is NOT available for sale.
					replyPrice.setPerformative(ACLMessage.REFUSE);
					replyPrice.setContent("not-available");
				}
				myAgent.send(replyPrice);
				//myAgent.send(replyQuality);
			}
			else {
				block();
			}
		}
	}  // End of inner class OfferRequestsServer

	/**
	   Inner class PurchaseOrdersServer.
	   This is the behaviour used by Book-seller agents to serve incoming 
	   offer acceptances (i.e. purchase orders) from buyer agents.
	   The seller agent removes the purchased book from its catalogue 
	   and replies with an INFORM message to notify the buyer that the
	   purchase has been sucesfully completed.
	 */
	// Servidor de Pedidos de Compras
	private class PurchaseOrdersServer extends CyclicBehaviour {

		private static final long serialVersionUID = 1L;

		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				// ACCEPT_PROPOSAL Message received. Process it
				String title = msg.getContent();
				ACLMessage reply = msg.createReply();

				/*Integer price = (Integer) cataloguePrice.remove(title);
				catalogueQuality.remove(title);*/
				caracteristicas price = (caracteristicas) catalogue.remove(title);
				if (price != null) {
					reply.setPerformative(ACLMessage.INFORM);
					System.out.println(title+" sold to agent "+msg.getSender().getName());
				}
				else {
					// The requested book has been sold to another buyer in the meanwhile .
					reply.setPerformative(ACLMessage.FAILURE);
					reply.setContent("not-available");
				}
				myAgent.send(reply);
			}
			else {
				block();
			}
		}
	}  // End of inner class OfferRequestsServer
	
	
	
	/**
    This is invoked by the GUI when the user adds a new book for sale
	 */
	public void updateCatalogue(final String title, final int price, final String quality) {
		addBehaviour(new OneShotBehaviour() {

			private static final long serialVersionUID = 1L;

			public void action() {
				catalogue.put(title, new caracteristicas(price, quality));
				/*cataloguePrice.put(title, new Integer(price));
				catalogueQuality.put(title, new String(quality));*/
				System.out.println(title+" inserted into catalogue. Price = "+price +"Quality = "+quality);
			}
		} );
	}
	
	
	// Put agent clean-up operations here
		protected void takeDown() {
			// Deregister from the yellow pages
			try {
				DFService.deregister(this);
			}
			catch (FIPAException fe) {
				fe.printStackTrace();
			}
			// Close the GUI
			myGui.dispose();
			// Printout a dismissal message
			System.out.println("Seller-agent "+getAID().getName()+" terminating.");
		}
}

