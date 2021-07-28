import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

// KCM Travel
// 다익스트라 + DP 문제
public class Main {
	static List<Airport>[] graph;	// 모든 vertex를 담는 리스트
	static int[][] moneyDp;
	static final int INF = Integer.MAX_VALUE / 2;
	
	public static void main(String args[]) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int tc = Integer.valueOf(st.nextToken());
		StringBuilder sb = new StringBuilder();
		
		while(tc --> 0) {
			st = new StringTokenizer(br.readLine());
			int n = Integer.valueOf(st.nextToken());
			int m = Integer.valueOf(st.nextToken());
			int k = Integer.valueOf(st.nextToken());
			moneyDp = new int[n + 1][m + 1];	// i : i번 공항까지의 경로, j : 비용 j만큼 소모함, 값 : 가장 작은 시간 (최단시간)   
			graph = new ArrayList[n + 1];
			
			for(int i = 1; i <= n; i++) {
				graph[i] = new ArrayList<Airport>();
				Arrays.fill(moneyDp[i], INF);
			}
			
			// 공항 정보 입력받음 
			for(int i = 0; i < k; i++) {
				st = new StringTokenizer(br.readLine());
				int u = Integer.valueOf(st.nextToken());
				int v = Integer.valueOf(st.nextToken());
				int c = Integer.valueOf(st.nextToken()); // 비용 
				int d = Integer.valueOf(st.nextToken()); // 소요시간 
				graph[u].add(new Airport(v, c, d));
			}
			
			dijkstra(n, m, 1);	// 다익스트라 가즈아잇
			
			int min = INF;
			for(int i = 0; i <= m; i++) { // 가장 최소 시간을 구해보자 
				min = Math.min(min, moneyDp[n][i]);
			}
			
			if(min == INF) { // 도착하지 못했을 때 
				sb.append("Poor KCM");
			}
			else {
				sb.append(min);
			}
			sb.append("\n");
		}
		
		System.out.println(sb.toString());
	}
	
	// 다익스트라 알고리즘 
	static void dijkstra(int n, int m, int start) {
		PriorityQueue<Airport> pq = new PriorityQueue<Airport>();
		pq.offer(new Airport(start, 0, 0));
		moneyDp[start][0] = 0;	// 1번 공항까지 0원을 소모했을 때, 최단시간은 0.
		
		while(!pq.isEmpty()) {
			Airport airport = pq.poll();
			int nowAirport = airport.number;
			int nowMoney = airport.money;
			int nowTime = airport.time;
			
			if(nowAirport == n) { // 최단 시간에 목적지 도착. 
				break;
			}
			
			for(Airport air : graph[nowAirport]) {
				int nextNum = air.number;
				int nextMoney = air.money + nowMoney;
				int nextTime = air.time + nowTime;
				
				if(nextMoney > m) {
					continue;
				}
				
				if(moneyDp[nextNum][nextMoney] <= nextTime) {	// 이미 해당 금액으로 해당 번 공항으로 왔을 때의 최소 시간보다 크면 업데이트할 의미가 없다. 
					continue;
				}
				
				// 불필요한 삽입 방지를 위해 그 이상의 모든 값에 현재 최단시간 값을 설정해준다.
				// 그래야 위 조건에 부합해서 continue 떠서 넘어가지.
                for(int j = nextMoney; j <= m; j++){
                    if(moneyDp[nextNum][j] > nextTime) {
                    	moneyDp[nextNum][j] = nextTime;
                    }
                }
                
				moneyDp[air.number][nextMoney] = nextTime;
				pq.offer(new Airport(air.number, nextMoney, nextTime));
			}
		}
	}
}

class Airport implements Comparable<Airport> {
	int number;
	int money;
	int time;
	
	
	Airport(int number, int money, int time) {
		this.number = number;
		this.time = time;
		this.money = money;
	}
	
	@Override
	public int compareTo(Airport o) {
		if(this.time < o.time) {
			return -1;
		}
		else if(this.time == o.time) {
			if(this.money < o.money) {
				return -1;
			}
			return 0;
		}
		return 1;
	}
}