import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {
	
	static int N,M,maxDepth;
	static ArrayList<ArrayList<Integer>> list;
	static int[][] ac;
	static int[] depth;
	
	public static void main(String[] arg) throws IOException{
		
		//System.setIn(new FileInputStream("src/BJ_11438.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));		
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		
		// 최대 depth 산정
		// 2^maxDepth > N인 maxDepth 찾기
		maxDepth = 0;
		for (int i = 1; i <= N; i *= 2) {
			maxDepth++;
		}

		ac = new int[N+1][maxDepth+1]; // 부모배열
		depth = new int[N+1]; // N값별 depth배열
		list = new ArrayList<ArrayList<Integer>>(); // 간선배열
		
		// 간선배열 초기화
		for(int i=0;i<=N;i++){
			list.add(new ArrayList<Integer>());
		}

		// 간선 입력받기(쌍방향)
		for(int i=1;i<N;i++){
			st = new StringTokenizer(br.readLine());
			int a = Integer.parseInt(st.nextToken());
			int b = Integer.parseInt(st.nextToken());
			list.get(a).add(b);
			list.get(b).add(a);
		}
		
		// 부모배열 초기화
		init(1,0);
		
		st = new StringTokenizer(br.readLine());		
		M = Integer.parseInt(st.nextToken());

		for(int i=1;i<=M;i++){
			st = new StringTokenizer(br.readLine());
			int a = Integer.parseInt(st.nextToken());
			int b = Integer.parseInt(st.nextToken());
			// 각 쿼리별 LCA 조회 및 출력
			bw.write(getLCA(a,b)+"\n");
		}
		bw.flush();
		bw.close();
		br.close();
	}
	
	static void init(int now, int parent){
		
		ac[now][0] = parent;
		depth[now] = depth[parent] + 1;
		
		for(int i=1; i<=maxDepth; i++){		
			ac[now][i] = ac[ac[now][i-1]][i-1];
		}
		
		for(int j=0; j<list.get(now).size(); j++){
			int next = list.get(now).get(j);
			if(next==parent) continue;
			init(next,now);
		}
		
	}

	static int getLCA(int a, int b){
		
		int ans=0;
		
		// 항상 depth가 높은 쪽이 b가 되도록 설정 (b의 depth만 올려서 a,b depth를 맞출수 있다)
		if(depth[a]>depth[b]){
			int temp = a;
			a = b;
			b = temp;
		}
		
		// depth 가 다른 경우, depth를 동일하게 맞추어 준다.
		for(int i=maxDepth;i>=0;i--){
			if(depth[a] <= depth[ac[b][i]]){
				b=ac[b][i];
			}
		}
		
		// b의 조상에 a가 있는 경우, 아래가 정답이 됨.
		ans = a;
		
		// a, b 값이 다른 경우, 부모배열을 maxDepth 만큼 이동하며 동일값을 확인한다.
		if(a!=b){
			
			for(int i=maxDepth;i>=0;i--){
				
				if(ac[a][i] != ac[b][i]){
					a = ac[a][i];
					b = ac[b][i];
				}				
				ans = ac[a][i];
			}
			
		}
			
		return ans;
	}
	
}
