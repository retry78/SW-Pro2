import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

	static PriorityQueue<Mnode> pq = new PriorityQueue<>();
	static ArrayList<Node> keys = new ArrayList<>();
	static int N,M;
	static int[][] dir = {{-1,0},{1,0},{0,-1},{0,1}}; // 상/하/좌/우 방향 배열 {row, col} 순서
	static char[][] map;
	static int[] parent;
	static int Answer = 0;
	
	public static void main (String[] args) throws IOException{

		//System.setIn(new FileInputStream("src/BJ_1944.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		map = new char[N+1][N+1]; // 지도 배열
		parent = new int[N*N+1]; // 부모 배열 (지도 2차 배열을 1차로 변형하여 저장)(ex. 좌표(2,2)=>7번째, 좌표(4,2)=>17번째)
		
		// 부모 배열 초기화
		for (int i=0; i<=N*N; i++){
			parent[i] = i;
		}
		
		// 지도 배열을 입력 받으면서, 시작점(s)와 KEY지점(K)은 별도 KEY 배열에 추가 저장(KEY배열 개수가 MST 수행시의 N값이 됨)
		for (int i=1; i<=N; i++){
			String input = br.readLine();
			for (int j=1; j<=N; j++){
				map[i][j] = input.charAt(j-1);
				if(map[i][j]=='S' || map[i][j]=='K'){
					keys.add(new Node(i,j));
				}
			}
		}

//		// 지도 배열 출력
//		for (int i=1; i<=N; i++){
//			for (int j=1; j<=N; j++){
//				System.out.print(map[i][j]+" ");
//			}
//			System.out.println();
//		}
//		System.out.println("==============================================================");
//		
//		// KEY 배열 출력
//		for (int i=0; i<keys.size(); i++){
//			System.out.println("(KEY)"+i+"->"+keys.get(i).n+":"+keys.get(i).m+" ");
//		}
//		System.out.println("==============================================================");
		
		// 시작점(S)과 KEY지점(K)를 시작점으로 하는 BFS를 각각 수행하여, MST 수행에 필요한 정점 및 간선 정보를 생성함 
		for (int i=0; i<keys.size(); i++){
			BFS(keys.get(i));
		}

		// MST 크루스칼 수행
		kru();
		
		// 정답 출력
//		bw.write("Answer:"+Answer+"\n");
		bw.write(Answer+"\n");
		
		bw.flush();
		bw.close();
		br.close();

	}
	
	static void BFS(Node node){
		
		boolean[][] visited = new boolean[N+1][N+1];
		int[][] d = new int[N+1][N+1];

		Queue<Node> q = new LinkedList<>();
		q.add(node);
		
		while(!q.isEmpty()){
			
			Node cur = q.poll();
			int nowN = cur.n;
			int nowM = cur.m;
			
			if (visited[nowN][nowM]) continue;
			visited[nowN][nowM] = true;
			
			for(int i=0; i<4; i++){
				
				int nextN = cur.n+dir[i][0];
				int nextM = cur.m+dir[i][1];

				if (visited[nextN][nextM]) continue;
				if(nextN < 1 || nextN > N || nextM < 1 || nextM > N) continue;
				if(map[nextN][nextM]=='1') continue;

				// 시작점(S)이나 KEY지점(K)에 도착한 경우, 최초 node의 좌표와 현재 좌표를 시작점/종료점으로 하는 MST간선 정보를 생성함 
				if(d[nextN][nextM]==0 && (map[nextN][nextM]=='K' || map[nextN][nextM]=='S')){
					// 우선순위큐에 MST간선 입력
					pq.add(new Mnode(N*(node.n-1)+node.m,N*(nextN-1)+nextM,d[nowN][nowM] + 1));
//					// 입력된 MES간선 정보 출력
//					System.out.println("(MST)"+(N*(node.n-1)+node.m)+"->"+(N*(nextN-1)+nextM)+":"+(d[nowN][nowM] + 1));
				}
				// 이동거리 배열 1증가
				d[nextN][nextM] = d[nowN][nowM] + 1;
				// 신규 node 큐에 추가
				q.add(new Node(nextN,nextM));
			}
		}
		
	}
	
	static void kru(){
		
		int cnt=0;
		
		while(!pq.isEmpty()){
			
			Mnode cur = pq.poll();
			if(find(cur.s)==find(cur.e)) continue;
			
			Answer += cur.cost;
			union(cur.s,cur.e);
			cnt++;
			// MST 이므로 N-1 인 경우, 크루스칼 종료
			if(cnt==keys.size()-1) break;
		}
		// 처리된 간선 수가 N-1이 아니면 -1 출력
		if(cnt!=keys.size()-1) Answer = -1;
	}
	
	static void union(int x, int y){
		int px = find(x);
		int py = find(y);
		if(px!=py){
			if(py>px){
				parent[py] = px;
			} else {
				parent[px] = py;
			}
		}
	}

	static int find(int x){
		if(parent[x]==x) return x;
		return parent[x] = find(parent[x]);
	}
		
	// BFS용 CLASS
	static class Node {
		int n,m;
		Node(int n, int m){
			this.n = n;
			this.m = m;
		}
	}

	// MST용 CLASS (Cost기준 오름차순)
	static class Mnode implements Comparable<Mnode>{
		int s,e,cost;
		Mnode(int s, int e, int cost){
			this.s = s;
			this.e = e;
			this.cost = cost;
		}
		@Override
		public int compareTo(Mnode o) {
			// TODO Auto-generated method stub
			return this.cost - o.cost;
		}
	}
}
