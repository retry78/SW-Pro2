import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
	
	static class Edge implements Comparable<Edge> {
		int to, cost;

		public Edge(int to, int cost) {
			this.to = to;
			this.cost = cost;
		}

		public int compareTo(Edge o) {
			return this.cost - o.cost;
		}
	}

	static int N, M, cnt;
	static int[] dist, path;
	static List<Edge>[] edgeList;

	public static void main(String[] args) {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out))){
			StringTokenizer st = new StringTokenizer(br.readLine());
			N = Integer.parseInt(st.nextToken());
			M = Integer.parseInt(st.nextToken());
			edgeList = new ArrayList[N + 1];
			for (int i = 1; i <= N; ++i) {
				edgeList[i] = new ArrayList<>();
			}
			for (int i = 0; i < M; ++i) {
				st = new StringTokenizer(br.readLine());
				int A = Integer.parseInt(st.nextToken());
				int B = Integer.parseInt(st.nextToken());
				int C = Integer.parseInt(st.nextToken());
				edgeList[A].add(new Edge(B, C));
				edgeList[B].add(new Edge(A, C));
			}

			dijkstra();

			StringBuilder sb = new StringBuilder();
			for (int v = 2; v <= N; ++v) {
				if (path[v] == 0) continue;
				cnt++;
				sb.append(v + " " + path[v] + "\n");
			}
			bw.write(cnt + "\n" + sb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void dijkstra() {
		dist = new int[N + 1];
		path = new int[N + 1];
		Arrays.fill(dist, Integer.MAX_VALUE);
		dist[1] = 0;
		Queue<Edge> pq = new PriorityQueue<>();
		pq.offer(new Edge(1, 0));

		while(!pq.isEmpty()) {
			Edge e = pq.poll();

			if (e.cost > dist[e.to]) continue;

			for (Edge ne : edgeList[e.to]) {
				if (dist[ne.to] > e.cost + ne.cost) {
					dist[ne.to] = e.cost + ne.cost;
					path[ne.to] = e.to; //경로 저장
					pq.offer(new Edge(ne.to, dist[ne.to]));
				}
			}
		}
	}
}