package fun.thesis.simulation.networkDelay;


public class Properties {
	//The X-coordinate of legitimate node's location ranges from [-EveX_MAX/2, EveX_MAX/2].
	public static int NodeX_MAX = 100;
	//The Y-coordinate of legitimate node's location ranges from [-EveY_MAX/2, EveY_MAX/2].
	public static int NodeY_MAX = 100;
	//The X-coordinate of eavesdropper's location ranges from [-EveX_MAX/2, EveX_MAX/2].
	public static int EveX_MAX = 100;
	//The Y-coordinate of eavesdropper's location ranges from [-EveY_MAX/2, EveY_MAX/2].
	public static int EveY_MAX = 100;  
	
	//Legitimate density;
	public static double NodeDensity = 0.05;	
	//Transmission Probability;
	public static double TransProbability=0.4; 
	//Transmitter density;
	public static double TransDensity=NodeDensity*TransProbability;    
	//Receiver density;
	public static double RxDensity =NodeDensity*(1-TransProbability); 
	//Eavesdropper density;
	public static double EveDensity = 0.004; 
	//Receiver's decoding threshold;
	public static double RxDecThresh = 0.5; 
	//Eavesdropper's decoding threshold;
	public static double EveDecThresh = 0.1; 
	//Transmission power;	
	public static double TxPower = 1.0; 
	//Transmission Power Allocation Ratio;
	public static double TxPowerAllocationRatio=1; 
	//Path-loss exponent;
	public static double PathLossExponent = 4.0; 
	//Noise power for the special case of noise power is a fixed value across space and timeslot. 
	public static double NoisePower = 0.001; 
	//Theoretical Secrecy Capacity
	public static double SecrecyCapacity=0.419788;
	
	public static double getInputRate(double rho){
		return SecrecyCapacity*rho;
	}
	
	public static double R2DProbability=0.5;
	
	public static final double ExpMean = 1.0;
	
	public static final int TransmatterNodeId=0;
	
	public static final int ReceiverNodeId=1;
}
