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
    private static final long serialVersionUID = 1L;
    // The catalogue of books for sale (maps the title of a book to its price)
    private Hashtable<String, Book> catalogue;
    // The GUI by means of which the user can add books in the catalogue
    private BookSellerGui myGui;

    // Put agent initializations here
    protected void setup() {
        // Create the catalogue
        catalogue = new Hashtable<String, Book>();

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
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        // Add the behaviour serving queries from buyer agents
        // Servidor de Requisi��es de Ofertas
        addBehaviour(new OfferRequestsServer());

        // Add the behaviour serving purchase orders from buyer agents
        // Servidor de Pedidos de Compras
        addBehaviour(new PurchaseOrdersServer());
        
        // Add the behaviour serving steal orders from thief agents
        addBehaviour(new StealOrdersServer());
    }

    /**
     * Inner class OfferRequestsServer.
     * This is the behaviour used by Book-seller agents to serve incoming requests
     * for offer from buyer agents.
     * If the requested book is in the local catalogue the seller agent replies
     * with a PROPOSE message specifying the price. Otherwise a REFUSE message is
     * sent back.
     */
    // Servidor de Requisi��es de Ofertas
    //FIPA PROTOCOLS: http://www.fipa.org/specs/fipa00030/
    private class OfferRequestsServer extends CyclicBehaviour {

        private static final long serialVersionUID = 1L;

        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                // CFP Message received. Process it
                String[] receivedContent = msg.getContent().split("/");
                ACLMessage reply = msg.createReply();

                Book book = catalogue.get(receivedContent[0]);
                if (book != null) {

                    int productPrice = book.getInitialPrice();

                    // Check if there was a discount request
                    if (receivedContent.length > 1 && receivedContent[1].trim() != "") {
                        int requestedPrice = Integer.valueOf(receivedContent[1]);

                        productPrice = book.getMinPrice() < requestedPrice ? requestedPrice : book.getMinPrice();
                    }

                    System.out.println("productPrice: " + productPrice);

                    // The requested book is available for sale. Reply with the price
                    reply.setPerformative(ACLMessage.PROPOSE);
                    reply.setContent(String.valueOf(productPrice));
                } else {
                    // The requested book is NOT available for sale.
                    reply.setPerformative(ACLMessage.REFUSE);
                    reply.setContent("not-available");
                }
                myAgent.send(reply);
            } else {
                block();
            }
        }
    }  // End of inner class OfferRequestsServer

    /**
     * Inner class StealOrdersServer.
     */
    private class StealOrdersServer extends CyclicBehaviour {

        private static final long serialVersionUID = 1L;

        public void action() {
            MessageTemplate mt = MessageTemplate.MatchConversationId("book-steal");
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                // ACCEPT_PROPOSAL Message received. Process it
            	if(catalogue.size() != 0) {
            		String title = getRandomBook();
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContent(title);
                    System.out.println("The book " + title + " was stolen by agent " + msg.getSender().getName());
					System.out.println("Books remaining: " + catalogue.size());
					myAgent.send(reply);
            	} else {
            		// The requested book has been sold or stolen to another buyer in the meanwhile .
            		ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.FAILURE);
                    reply.setContent("not-available");
                    myAgent.send(reply);
            	}
                

       

            } else {
                block();
            }
        }
        
        private String getRandomBook() {
        	Collection<String> books = catalogue.keySet();
        	List<String> booksList = new ArrayList<>(books);
        	Random rand = new Random();
        	int index = rand.nextInt(booksList.size());
        	return booksList.get(index);
        }
    }  // End of inner class StealOrdersServer
    
    private class PurchaseOrdersServer extends CyclicBehaviour {

        private static final long serialVersionUID = 1L;

        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                // ACCEPT_PROPOSAL Message received. Process it
                String title = msg.getContent();
                ACLMessage reply = msg.createReply();

                Book removedBook = catalogue.remove(title);
                if (removedBook != null) {
                    reply.setPerformative(ACLMessage.INFORM);
                    System.out.println(title + " sold to agent " + msg.getSender().getName());
                } else {
                    // The requested book has been sold to another buyer in the meanwhile .
                    reply.setPerformative(ACLMessage.FAILURE);
                    reply.setContent("not-available");
                }
                myAgent.send(reply);
            } else {
                block();
            }
        }
    } 

    private class Book {
        private static final long serialVersionUID = 1L;
        private Integer initialPrice;
        private Integer minPrice;

        public Book(final int initialPrice, final int minPrice) {
            this.initialPrice = new Integer(initialPrice);
            this.minPrice = new Integer(minPrice);
        }

        public Integer getInitialPrice() {
            return this.initialPrice;
        }

        public Integer getMinPrice() {
            return this.minPrice;
        }
    }


    /**
     * This is invoked by the GUI when the user adds a new book for sale
     */
    public void updateCatalogue(final String title, final int initialPrice, final int minPrice) {
        Book newBook = new Book(initialPrice, minPrice);
        addBehaviour(new OneShotBehaviour() {

            private static final long serialVersionUID = 1L;

            public void action() {
                catalogue.put(title, newBook);
                System.out.println(title + " inserted into catalogue."
                        + " initialPrice = " + initialPrice
                        + ", minPrice = " + minPrice);
            }
        });
    }


    // Put agent clean-up operations here
    protected void takeDown() {
        // Deregister from the yellow pages
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        // Close the GUI
        myGui.dispose();
        // Printout a dismissal message
        System.out.println("Seller-agent " + getAID().getName() + " terminating.");
    }
}
