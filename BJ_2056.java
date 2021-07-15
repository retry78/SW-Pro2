import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
	
	static BufferedReader br;
	static BufferedWriter bw;
	static StringTokenizer st;
	
	static int N;
	static int cost[];
	static int d[];
	static int degree[];
	static boolean visited[];		
	static ArrayList<Integer>[] adj;
	
	public static void main(String[] arg) throws IOException{
		//System.setIn(new FileInputStream("src/Baek_2056.txt"));
		br = new BufferedReader(new InputStreamReader(System.in));
		bw = new BufferedWriter(new OutputStreamWriter(System.out));
		st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());		

		visited = new boolean[N+1];
		cost = new int[N+1];
		degree = new int[N+1];
		d = new int[N+1];
		Arrays.fill(d, -1);
		
		adj = new ArrayList[N+1];
		for(int i=0; i<=N; i++){
			adj[i] = new ArrayList<Integer>();
		}
		
		for(int i=1; i<=N; i++){
			st = new StringTokenizer(br.readLine());
			cost[i] = Integer.parseInt(st.nextToken());
			int m = Integer.parseInt(st.nextToken());

			for(int j=1; j<=m; j++){
				int y = Integer.parseInt(st.nextToken());
				adj[y].add(i);
				degree[i]++;
			}					
		}
		
		BFS();
		
		int answer = 0;
		
		for(int i=1; i<=N; i++){
			if(answer < d[i]) answer = d[i];
		}
		
		bw.write(answer+"\n");
		bw.flush();
		bw.close();
		br.close();
	}
	
	private static void BFS(){
		Queue<Integer> que = new LinkedList<Integer>();
		
		for(int i=1; i<=N; i++){
			if(degree[i]==0){
				que.offer(i);
				d[i] = cost[i];
			}
		}
		while(!que.isEmpty()){
			int now = que.poll();
			if(visited[now]) continue;
			visited[now] = true;
			for(int i=0;i<adj[now].size();i++){
				int next = adj[now].get(i);
				if(d[now]+cost[next] > d[next]){
					d[next] = d[now]+cost[next];
				}
				degree[next]--;
				if(degree[next]==0){
					que.offer(next);
				}
			}
		}
	}
}
