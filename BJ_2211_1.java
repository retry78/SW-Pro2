import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
	static BufferedReader br;
	static BufferedWriter bw;
	static StringTokenizer st;
	static int N,M;
	static int[][] dist;
	static ArrayList<ArrayList<Node>> line;
	static Queue<Node> que;
	
	// 간선 저장 class
	static class Node{
		
		int to;
		int time;
		Node(int a, int b){
			to = a;
			time = b;
		}
	}
	
	public static void main(String[] arg) throws IOException{
		
		//System.setIn(new FileInputStream("src/BJ_2211.txt"));
		br = new BufferedReader(new InputStreamReader(System.in));
		bw = new BufferedWriter(new OutputStreamWriter(System.out));
		st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());

		line = new ArrayList<ArrayList<Node>>(); // 간선 정보 저장(인접list)
		dist = new int[N+1][2]; // N정점의 최소시간 저장 ([0] 최소시간, [1] 최소시간 저장시 출발점)
		que = new LinkedList<Node>(); // BFS를 위한 Queue
		
		// 인접list 초기화
		for(int i=0; i<=N; i++){
			line.add(new ArrayList<Node>());
			dist[i][0] = Integer.MAX_VALUE;
		}
		
		// 데이터 입력 받기
		for(int i=1; i<=M; i++){
			st = new StringTokenizer(br.readLine());
			int a = Integer.parseInt(st.nextToken());
			int b = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());
			
			// 방향성이 없으므로 양방향으로 간선 정보 추가
			line.get(a).add(new Node(b,c));
			line.get(b).add(new Node(a,c));
		}
		
		que.offer(new Node(1,0));
		dist[1][0] = 0;
		
		// 다익스트라 수행
		while(!que.isEmpty()){
			Node nodeFrom = que.poll();
			
			for(int i=0; i<line.get(nodeFrom.to).size(); i++){
				Node nodeTo = line.get(nodeFrom.to).get(i);
				
				//현재 정점+to정점간의 간선 시간이 to정점의 누적 시간보다 작은 경우, to정점의 누적 시간 업데이트
				if(dist[nodeFrom.to][0]+nodeTo.time < dist[nodeTo.to][0]){
					dist[nodeTo.to][0] = dist[nodeFrom.to][0]+nodeTo.time; // 정점의 최소누적시간 업데이트
					dist[nodeTo.to][1] = nodeFrom.to; // 최소누적시간일때 출발점 업데이트
					que.offer(nodeTo); // 갱신된 정점을 큐에 추가
				}
			}			
		}
		
		// 간선의 개수 출력
		bw.write(N-1+"\n");
		
		// 최단 경로 출력
		for (int i=1; i<=N; i++){
			if(dist[i][1]!=0){
				bw.write(dist[i][1]+" "+i+" "+"\n");
			}
		}
		bw.flush();
		bw.close();
		br.close();
		
	}
	
}
