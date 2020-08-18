package net.vicp.fyhui.tools.alert;

import java.util.Random;

/**
 * 报警工具类
 * 捕获报警类型、时间、内容
 * 根据配置、报警值、间隔等 
 * @author Administrator
 *
 */
public class AlertUtil {

	public static void main(String[] args) {
		AlertUtil.test();
//		while (true) {
//			int realValue = new Random().nextInt(5000);
//			int low = 600; //阈值
//			int middle = 1500;
//			int high = 3000;
//			int longestTimes =1;
//			AlertUtil.alertSet(realValue, low, middle, high, longestTimes);
//			try {
//				Thread.sleep(4000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}
	
	public static void pushWarnings() {
		
	}
	
	/**
	 * 监测值高于低阈值之后，连续取平均值，平均值高于阈值开始告警
	 * @param realValue 监测值
	 * @param low	低警告阈值
	 * @param middle	中警告阈值
	 * @param high	高警告阈值
	 * @param longestTimes	连续检测次数
	 */
	public static void alertSet(int realValue,int low,int middle, int high,int longestTimes) {
		int count = 0;  //计数
		boolean alertlow = false; //报警标记
		boolean alertmiddle = false; //报警标记
		boolean alerthigh = false; //报警标记
		double average = 0; //多次平均值
		double tempvalue = 0; //临时值
		realValue = new Random().nextInt(5000); //生产随机数
//				System.out.println("value ==>> "+ value);
		tempvalue = average;
		if(realValue>=low) {
			count++;
			average = (tempvalue*(count-1)+realValue)/count;
//					System.out.println("---------");
		} else {
			if(alertlow||alertmiddle||alerthigh) {
				System.out.println("alert is goon, please release");
			}
			count=0;
			average=0;
			alertlow = false;
			alertmiddle = false;
			alerthigh = false;
		}
		if(count>=longestTimes&&average>=low) {
			if(average<middle) {
				if(!alertlow) {
					System.err.println("low alert, please attention , no tee.[low:"+low+",realValue:"+realValue+",average:"+average+"]");
					alertlow=true;
				} else {
					System.err.println("已通知,不再重复通知");
				}
			} else if(average<high) {
				if(!alertmiddle) {
					System.err.println("middle alert, middle alert, the road is broken![middle:"+middle+",realValue:"+realValue+",average:"+average+"]");
					alertmiddle=true;
				} else {
//							System.err.println("middle alert, please attention again");
					alertmiddle=false;
				}
			} else {
				alerthigh = true;
				System.err.println("high alert, high alert, high alert, the earth is explode![high:"+high+",realValue:"+realValue+",average:"+average+"]");
			}
			System.out.println("value ==>> "+ realValue);
			System.err.println("average ==>> "+ average);
			System.err.println("系统已告警，请工程师抓紧处理");
		}
		System.out.println("-------------------------");
	}
	
	/**
	 * 1、获取值
	 * 2、设置阈值，比较
	 * 3、时间间隔
	 * 
	 */
	public static void test() {
		int value = 1; //实时值
		int low = 600; //阈值
		int middle = 1500;
		int high = 3000;
		long wait = 2000;
		int count = 0;  //计数
		boolean alertlow = false; //报警标记
		boolean alertmiddle = false; //报警标记
		boolean alerthigh = false; //报警标记
		double average = 0; //多次平均值
		int times = 5; //连续告警次数
		double tempvalue = 0; //临时值
		while (true) {

			try {
				value = new Random().nextInt(5000); //生产随机数
//				System.out.println("value ==>> "+ value);
				tempvalue = average;
				if(value>=low) {
					count++;
					average = (tempvalue*(count-1)+value)/count;
//					System.out.println("---------");
				} else {
					if(alertlow||alertmiddle||alerthigh) {
						System.out.println("alert is goon, please release");
					}
					count=0;
					average=0;
					alertlow = false;
					alertmiddle = false;
					alerthigh = false;
				}
				if(count>=times&&average>=low) {
					if(average<middle) {
						if(!alertlow) {
							System.err.println("low alert, please attention , no tee.[low:"+low+",realValue:"+value+",average:"+average+"]");
							alertlow=true;
						} else {
							System.err.println("已通知,不再重复通知");
						}
					} else if(average<high) {
						if(!alertmiddle) {
							System.err.println("middle alert, middle alert, the road is broken![middle:"+middle+",realValue:"+value+",average:"+average+"]");
							alertmiddle=true;
						} else {
//							System.err.println("middle alert, please attention again");
							alertmiddle=false;
						}
					} else {
						alerthigh = true;
						System.err.println("high alert, high alert, high alert, the earth is explode![high:"+high+",realValue:"+value+",average:"+average+"]");
					}
					System.out.println("value ==>> "+ value);
					System.err.println("average ==>> "+ average);
					System.err.println("系统已告警，请工程师抓紧处理");
					
					
//					System.out.println("value ==>> "+ value);
//					System.err.println("average ==>> "+ average);
//					System.err.println("系统已告警，请工程师抓紧处理");
//					if(!alertlow) {
////						System.err.println("low alert, please attention");
//						alertlow=true;
//					} else {
//						System.err.println("已通知,不在重复通知");
//					}
//					if(average<middle) {
//						System.err.println("low alert, please attention");
//					} else if(average<high) {
//						alertmiddle = true;
//						System.err.println("middle alert, middle alert, the road is brocken!");
//					} else {
//						alerthigh = true;
//						System.err.println("high alert, high alert, the earth is boon!");
//					}
//					
//					if(average>=middle) {
////						System.err.println("middle alert, middle alert, the road is brocken!");
//						alertmiddle = true;
//						if(average>=high) {
////							System.err.println("high alert, high alert, the earth is boon!");
//							alerthigh = true;
//							Thread.sleep(5000);
//						}
//					}
//					if (alerthigh) {
//						System.err.println("high alert, high alert, the earth is boon!");
//					} else {
//						if (alertmiddle) {
//							System.err.println("middle alert, middle alert, the road is brocken!");
//						} else {
//							if (alertlow) {
//								System.err.println("low alert, please attention");
//							}
//						}
//					}
				}
//				System.err.println("count ==>> "+ count);
//				System.err.println("average ==>> "+ average);
				System.out.println("-------------------------");
				
				Thread.sleep(wait);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
