import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {

	static BufferedReader br;
	static BufferedWriter bw;
	static StringTokenizer st;
	
	static class Node implements Comparable<Node>{
		int to;
		int cost;
		int time;
		Node(int v, int c, int d){
			this.to = v;
			this.cost = c;
			this.time = d;
		}
		@Override
		public int compareTo(Node o) {
			// TODO Auto-generated method stub
			if(this.time==o.time) return this.cost-o.cost; // 시간이 동일한 경우, 비용이 작은 순으로 정렬
			return this.time-o.time; // 시간이 작은 순으로 정렬
		}
	}
	
	public static void main(String[] arg) throws IOException{
				
		//System.setIn(new FileInputStream("src/BJ_10217.txt"));
		br = new BufferedReader(new InputStreamReader(System.in));
		bw = new BufferedWriter(new OutputStreamWriter(System.out));
		st = new StringTokenizer(br.readLine());
		
		int T = Integer.parseInt(st.nextToken());
		
		for(int testCase = 1; testCase<=T ; testCase++){
			
			st = new StringTokenizer(br.readLine());
			int N = Integer.parseInt(st.nextToken());
			int M = Integer.parseInt(st.nextToken());
			int K = Integer.parseInt(st.nextToken());

			ArrayList<ArrayList<Node>> list = new ArrayList<ArrayList<Node>>(); // 간선 정보 저장(인접list)
			int[][] dist = new int[N+1][M+1]; // N정점, M비용일때 최소시간 저장
			PriorityQueue<Node> pq = new PriorityQueue<Node>(); // 속도 향상을 위해 우선순위큐 사용
			int Answer = Integer.MAX_VALUE;

			// 인접list, DP배열 초기화
			for(int i=0; i<=N; i++){
				list.add(new ArrayList<Node>());
				Arrays.fill(dist[i], Integer.MAX_VALUE);
			}

			// 데이터 입력 받기
			for(int i=1; i<=K; i++){
				st = new StringTokenizer(br.readLine());
				int u = Integer.parseInt(st.nextToken());
				int v = Integer.parseInt(st.nextToken());
				int c = Integer.parseInt(st.nextToken());
				int d = Integer.parseInt(st.nextToken());

				// 간선 정보 추가(방향성 존재)
				list.get(u).add(new Node(v,c,d));
			}
			
			// Queue 초기화
			pq.offer(new Node(1,0,0));
			dist[1][0] = 0; // 1번 정점 0비용인 시간은 0

			// 다익스트라 수행
			while(!pq.isEmpty()){
				Node nodeFrom = pq.poll();
				int now = nodeFrom.to;
				int nowCost = nodeFrom.cost;
				int nowTime = nodeFrom.time;
				
				//최종 정점(N)에 도착한 경우, 루프 중지.
				if(now==N){
					Answer = Math.min(Answer, nowTime);
					break;
				}
				
				//인접 리스트 만큼 시간 및 비용 비교
				for(int i=0; i<list.get(now).size(); i++){
					Node nodeTo = list.get(now).get(i);
					int next = nodeTo.to;
					int nextCost = nodeTo.cost;
					int nextTime = nodeTo.time;
					int sumCost = nowCost + nextCost;
					int sumTime = nowTime + nextTime;
					
					// 누적 비용이 M보다 큰 경우, SKIP
					if(sumCost > M) continue;

					//정점별 누적 비용 배열의 누적 시간이 줄어든 경우, Queue에 추가
					if(dist[next][sumCost] > sumTime){
						dist[next][sumCost] = sumTime;
						pq.offer(new Node(next, sumCost, sumTime));
					}
				}
			}

			// 최소 시간 출력
			if(Answer==Integer.MAX_VALUE){
				bw.write("Poor KCM\n");
			} else {
				bw.write(Answer+"\n");
			}
			
		}

		bw.flush();
		bw.close();
		br.close();
		
	}
}
