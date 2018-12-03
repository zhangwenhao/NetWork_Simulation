package fun.thesis.simulation.networkDelay;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fun.thesis.simulation.networkDelay.Node.TransType;


public class NetWork {

	List<Node> nodeSet=new ArrayList<Node>();
	
	List<Node> eavNodes=new ArrayList<Node>();
	
	Random ran=new Random();
	
	public void generateNode(int totalNodes,int totalevaNodes){
		for (int i = 0; i < totalNodes; i++) {
			Node node=new Node(getRandomLocation(Properties.NodeX_MAX), getRandomLocation(Properties.NodeY_MAX));
			node.resetRole();
			nodeSet.add(node);
		}
		for (int i = 0; i <totalevaNodes; i++) {
			Node node=new Node(getRandomLocation(Properties.EveX_MAX), getRandomLocation(Properties.EveY_MAX));
			node.role=Node.Role.Eavesdropper;
			eavNodes.add(node);
		}
	}
	
	public void resetNewWork(){
		for (Node node : nodeSet) {
			node.resetLocation(getRandomLocation(Properties.NodeX_MAX), getRandomLocation(Properties.NodeY_MAX));
			node.resetRole();
		}
		for (Node node : eavNodes) {
			node.resetLocation(getRandomLocation(Properties.EveX_MAX), getRandomLocation(Properties.EveY_MAX));
		}
	}
	
	public void startTransmission(double inputRate,int loopNo){
		Node transNode=nodeSet.get(Properties.TransmatterNodeId);
		Packet newPacket=packetArriveBernouli(inputRate);
		transNode.packetReceived(newPacket, loopNo);
		if(transNode.role.equals(Node.Role.Transmater)){
			if(transNode.hasPacket()){
				int id = nearestNode(transNode);
				if(id==Properties.ReceiverNodeId){
					if(reliableProbability(transNode,nodeSet.get(id))&&secureProbability(transNode)){
						packetTransmission(nodeSet.get(id),transNode,loopNo);
						return;
					};
				}else{
					if(ran.nextDouble()>Properties.R2DProbability&&reliableProbability(transNode,nodeSet.get(id))&&secureProbability(transNode)){
						packetTransmission(nodeSet.get(id),nodeSet.get(Properties.TransmatterNodeId),loopNo);
					};
				}
			}
		}
		if(nodeSet.get(Properties.ReceiverNodeId).role.equals(Node.Role.Receiver)){
			for (Node node : nodeSet) {
				if(node.role.equals(Node.Role.Transmater)&&node.hasPacket()&&nodeSet.indexOf(node)!=Properties.TransmatterNodeId){
					int id = nearestNode(node);
					if(id==Properties.ReceiverNodeId){
						if(ran.nextDouble()>Properties.R2DProbability&&reliableProbability(node,nodeSet.get(id))&&secureProbability(node)){
							packetTransmission(nodeSet.get(id),node,loopNo);
							return;
						};
					}
				}
			}
		}
	}
	
	public void packetTransmission(Node nearestNode,Node transNode,int loopNo){
		Packet packet=transNode.getPacket(loopNo);
		if(packet!=null){
			nearestNode.packetArrival(packet,loopNo);
		}
	}
	
	public double getRandomLocation(double rand) {
		double randomD = 0.0;
		randomD = ran.nextDouble()*rand;
		return randomD;
	}
	
	/**
	 * packet arrive process under Bernouli
	 * @param inputRate
	 */
	public Packet packetArriveBernouli(double inputRate) {
		if (ran.nextDouble() > inputRate) {
			return null;
		} else {
			Packet newPacket = new Packet();
			return newPacket;
		}
	}
	
	public int nearestNode(Node transNode) {
		double[] distances = new double[nodeSet.size()];
		int tmpNo = 0;
		double tmpDistance=100000000;
		//0为选中的传输者，不能从0开始
		for (int i = 1; i < nodeSet.size(); i++) {
			if(nodeSet.get(i).role.equals(Node.Role.Receiver)){
				distances[i] = transNode.location.distance(nodeSet.get(i).location);
				if(distances[i]<tmpDistance){
					tmpNo = i;
					tmpDistance = distances[i];
				}
			}
		}
		return tmpNo;
	}
	
	public boolean reliableProbability(Node transNode,Node nearestNode){
		if (computeSINR(transNode, nearestNode) > Properties.RxDecThresh) {
			return true;
		}
		return false;
	}
	
	public double computeSINR(Node transmitter, Node receiver){
		double refSINR = 0.0; 
		double signal = 0.0; 
		double interference = 0.0; 
		signal = receiver.receiveSignal(transmitter,TransType.TransPower);
		for (Node node : nodeSet) {
			if(node==transmitter||node.role.equals(Node.Role.Receiver)){
				continue;
			}
			interference += receiver.receiveSignal(node,TransType.TransPower);
		}
		refSINR = signal / (interference + Properties.NoisePower);
		return refSINR;
	}
	
	public boolean secureProbability(Node transNode){
		for (Node eavesdropper : eavNodes) {
			if (computeEavSINR(transNode, eavesdropper) > Properties.EveDecThresh) {
				return false;
			}
		}
		return true;
	}
	
	public double computeEavSINR(Node transmitter, Node eavesdropper){
		double refSINRE = 0.0; 
		double signal = 0.0;
		double interference = 0.0; 
		signal = eavesdropper.receiveSignal(transmitter,TransType.TransPower);
		/*for (Node node : nodeSet) {
			if(node.role.equals(Node.Role.Receiver)){
				continue;
			}
			interference += eavesdropper.receiveSignal(node,TransType.TransNoise);
		}*/
		refSINRE = signal / (interference + Properties.NoisePower);
		return refSINRE;
		}
}
