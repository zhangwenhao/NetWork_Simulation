package fun.thesis.simulation.networkDelay;

import java.util.List;
import java.util.Random;

public class Main {
	
	public static void main(String[] args) {
		int loops=10;
		int loopTimes=100000;
		double rho[]={0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9};
		for (int i = 0; i < rho.length; i++) {
			double avgTime=0;
			for (int j = 0; j < loops; j++) {
				double times=0;
				NetWork netWork=new NetWork();
				int totalNodes=getPoissonRandom(Properties.NodeDensity * Properties.NodeX_MAX * Properties.NodeY_MAX);
				int eavNodes=getPoissonRandom(Properties.EveDensity * Properties.EveX_MAX * Properties.EveY_MAX);
				netWork.generateNode(totalNodes,eavNodes);
				for (int k = 0; k < loopTimes; k++) {
					if(k%10000==0){
						System.out.print(k+"->");
					}
					netWork.startTransmission(rho[i]*Properties.SecrecyCapacity, k+1);
					netWork.resetNewWork();
				}
				List<Node> nodeSet=netWork.nodeSet;
				Node transNode=nodeSet.get(Properties.TransmatterNodeId);
				Node recNode=nodeSet.get(Properties.ReceiverNodeId);
				System.out.println("\n发送节点收到："+transNode.receivedPacketsCount+",发送了："+transNode.transmitPacketCount+",还剩："+transNode.packetsSize());
				System.out.println("目的节点成功接收到："+recNode.receivedPacketsCount);
				for (Packet packet : recNode.packetQueue) {
					times+=packet.getReceptionTime()-packet.getGenerateTime()+1;
				}
				double aTime=DoubleComputeUtil.div(times, recNode.receivedPacketsCount);
				avgTime+=aTime;
				System.out.println("第"+(j+1)+"次，平均延迟："+aTime);
			}
			System.out.println("rho等于"+rho[i]+"时，平均延迟为："+DoubleComputeUtil.div(avgTime, loops));
		}
	}
	
	public static int getPoissonRandom(double mean) {
		Random generator = new Random();
		int totalNodes= 0;
		double t_sum=0.0;
		while (true) {
			t_sum+=(-1.0/mean)*Math.log(1-generator.nextDouble());
			if (t_sum >= 1.0){break;}
			totalNodes++;
		}
		return totalNodes;
	}
}
