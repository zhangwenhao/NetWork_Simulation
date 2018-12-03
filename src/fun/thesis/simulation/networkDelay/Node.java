package fun.thesis.simulation.networkDelay;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Node {
	
	static enum Role{
		Transmater,Receiver,Eavesdropper;
	}
	
	Role role;
	Point2D.Double location;
	Queue<Packet> packetQueue=new LinkedList<Packet>();
	int receivedPacketsCount=0;
	int transmitPacketCount=0;
	Random random=new Random();
	
	Node(double x,double y){
		this.location=new Point2D.Double(x, y);
	}
	
	public void resetLocation(double x, double y){
		this.location.x = x;
		this.location.y = y;
	}
	
	public double getTransPower() {
		return Properties.TxPower;
	}
	
	public double getTransInformationPower() {
		return Properties.TxPower * Properties.TxPowerAllocationRatio;
	}

	public double getTransNoisePower() {
		return Properties.TxPower * (1 - Properties.TxPowerAllocationRatio);
	}
	
	public void resetRole(){
		if(random.nextDouble()<Properties.TransProbability){
			this.role=Role.Transmater;
		}else{
			this.role=Role.Receiver;
		}
	}
	
	public void packetArrival(Packet newPacket,int loopNo){
		if(newPacket!=null){
			newPacket.setReceptionTime(loopNo);
			packetQueue.add(newPacket);
			receivedPacketsCount++;
		}
	}
	
	public void packetReceived(Packet newPacket,int loopNo){
		if(newPacket!=null){
			newPacket.setGenerateTime(loopNo);
			packetQueue.add(newPacket);
			receivedPacketsCount++;
		}
	}
	
	public boolean hasPacket(){
		return packetQueue.size()>0;
	}
	
	public int packetsSize(){
		return packetQueue.size();
	}
	
	public Queue<Packet> getPacketList(){
		return packetQueue;
	}
	
	public Packet getPacket(int loopNo){
		transmitPacketCount++;
		Packet p=packetQueue.poll();
		p.setTransTimes(p.getTransTimes()+1);
		if(p.getBeginTransTime()==0){
			p.setBeginTransTime(loopNo);
		}
		return p;
	}
	
	static enum TransType{
		TransPower,TransNoise,TransInformation;
	}
	
	public double receiveSignal(Node targetTx,TransType transType){
		double rxPower=0.0;
		double txDistance = this.location.distance(targetTx.location);
		double gainValue = (-Properties.ExpMean) * Math.log(1 - random.nextDouble());
		switch (transType) {
		case TransPower:
			rxPower = targetTx.getTransPower()* Math.pow(txDistance, -Properties.PathLossExponent) * gainValue;
			break;
		case TransNoise:
			rxPower = targetTx.getTransNoisePower()* Math.pow(txDistance, -Properties.PathLossExponent) * gainValue;
		break;
		case TransInformation:
			rxPower = targetTx.getTransInformationPower()* Math.pow(txDistance, -Properties.PathLossExponent) * gainValue;
			break;
		}
		return rxPower;
	}
	
}
