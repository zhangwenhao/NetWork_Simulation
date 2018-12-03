package fun.thesis.simulation.networkDelay;

public class Packet {
	private int generateTime;
	private int receptionTime;
	private int beginTransTime;
	private int transTimes;
	
	public int getGenerateTime() {
		return generateTime;
	}
	public void setGenerateTime(int generateTime) {
		this.generateTime = generateTime;
	}
	public int getReceptionTime() {
		return receptionTime;
	}
	public void setReceptionTime(int receptionTime) {
		this.receptionTime = receptionTime;
	}
	public int getBeginTransTime() {
		return beginTransTime;
	}
	public void setBeginTransTime(int beginTransTime) {
		this.beginTransTime = beginTransTime;
	}
	public int getTransTimes() {
		return transTimes;
	}
	public void setTransTimes(int transTimes) {
		this.transTimes = transTimes;
	}
	
	@Override
	public String toString() {
		return "Packet [generateTime=" + generateTime + ", receptionTime="
				+ receptionTime + ", beginTransTime=" + beginTransTime
				+ ", transTimes=" + transTimes + "]";
	}
}
