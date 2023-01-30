import jade.core.Agent;

import java.util.Random;

import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class BookThiefAgent extends Agent {

	private static final long serialVersionUID = 1L;
	// The title of the book to buy
	private String targetBookTitle;
	// The amount of money that buyer agent has
	private int proposal = 130; // The buyer will send 130 for the target book as a proposal
	// The list of known seller agents
	private AID[] sellerAgents;

	protected void setup() {
		// Printout a welcome message
		System.out.println("Hehe! Thief-agent "+getAID().getName()+" is ready to take some books.");

		// Add a TickerBehaviour that schedules a request to seller agents every 10 seconds
		addBehaviour(new TickerBehaviour(this, 10000) {

			private static final long serialVersionUID = 1L;

			protected void onTick() {
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
				myAgent.addBehaviour(new StealBook());
			}
		} );
	}
	
	private class StealBook extends OneShotBehaviour {
		private int seller;
		private MessageTemplate mt; // The template to receive replies
		public void action() {
			seller = getRandomNumber(sellerAgents.length);
			
			// Send the cfp to random seller
			ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
			inform.addReceiver(sellerAgents[seller]);
			inform.setConversationId("book-steal");
			inform.setReplyWith("inform"+System.currentTimeMillis());
//			ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
//			cfp.addReceiver(sellerAgents[seller]);
//			cfp.setContent(targetBookTitle);
//			cfp.setConversationId("book-steal");
//			cfp.setReplyWith("cfp"+System.currentTimeMillis()); // Unique value
//			myAgent.send(cfp);
			myAgent.send(inform);
			// Prepare the template to get proposals
			mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-steal"),
					MessageTemplate.MatchInReplyTo(inform.getReplyWith()));
			
			// Receive all proposals/refusals from seller agents
			ACLMessage reply = myAgent.receive(mt);
			if (reply != null) {
				// Reply received
				if (reply.getPerformative() == ACLMessage.INFORM) {
					String title = reply.getContent();
					System.out.println("Haha, I stole the book: " + title);
					myAgent.doDelete();
				} else {
					String title = reply.getContent();
					System.out.println("Haha, I stole the book: " + title);
				}
			} else {
				block();
			}
		}
		
		private int getRandomNumber(int upperbound) {
			Random rand = new Random();
			return rand.nextInt(upperbound);
		}
		
	}
	// Put agent clean-up operations here
	protected void takeDown() {
		// Printout a dismissal message
		System.out.println("Thief-agent "+getAID().getName()+" terminating.");
	}
}
