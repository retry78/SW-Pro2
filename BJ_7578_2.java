import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class Main{
	
	static int n,m;
	static int[] tree;
	static int ts = 1; // Index Tree N=1의 Tree 시작점 
	static int[] num = new int[1000001];
	
	public static void main(String args[]) throws IOException{
		
		//System.setIn(new FileInputStream("src/BJ_7578.txt"));
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
 		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        n = Integer.parseInt(st.nextToken());
        
        // index tree n=1의 tree index 산정 (ts값 설정)
		while(ts < n){
			ts *= 2;
		}
		tree = new int[ts*2];
		
		// A열의 기계 순번을 index tree로 사용하도록 값 입력 받음
        st = new StringTokenizer(br.readLine());
        for (int i=1; i<=n; i++){
        	int a = Integer.parseInt(st.nextToken());
        	num[a] = i;
        }
        
        long result = 0;
        st = new StringTokenizer(br.readLine());
        
        // B열 기계 순번으로 A열 index를 구하여, index tree update 및 정답 합산
        for (int i=1; i<=n; i++){
        	int a = Integer.parseInt(st.nextToken());
        	update(num[a],1); // 현재 기계번호 순번에 방문여부(1) A열에 표시
        	result += sum(num[a]+1,n); // A열 index tree에서 이미 방문된 index 수를 합산
        }
        
        bw.write(result+"\n");
        
        bw.flush();
        bw.close();
        br.close();
        
	}
	
	static long sum(int idx1,int idx2){
		int t = ts+idx1-1;
		int s = ts+idx2-1;
		long count = 0;
		while (t<=s){
			if (t%2 == 1) count += tree[t++];
			if (s%2 == 0) count += tree[s--];
			t/=2;
			s/=2;
		}
		return count;
	}
	
	static void update(int idx,int num){
		int st = ts + idx -1;
		tree[st] = num;
		while (st>1){
			st/=2;
			tree[st] = tree[st*2] + tree[st*2+1];
		}
	}

}