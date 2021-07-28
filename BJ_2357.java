import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
	
	static int N, M, maxSize;
	static int[] input;
	static int[] treeMin;
	static int[] treeMax;

	public static void main(String[] args) throws IOException{
		
			//System.setIn(new FileInputStream("src/BJ_2357.txt"));
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
			StringTokenizer st = new StringTokenizer(br.readLine());
			
			N = Integer.parseInt(st.nextToken());
			M = Integer.parseInt(st.nextToken());
			
			// 입력 배열 생성
			input = new int[N+1];
			
			// tree 사이즈 확인
			maxSize = 1;
			while(maxSize<N) {
				maxSize <<=1;
			}
			
			// teee 배열 생성
			treeMin = new int[maxSize*2];
			treeMax = new int[maxSize*2];
			
			// treeMin 배열 초기화
			Arrays.fill(treeMin, Integer.MAX_VALUE);
			
			// 입력값 저장
			for (int i = 1; i <= N; ++i) {
				st = new StringTokenizer(br.readLine());
				input[i] = Integer.parseInt(st.nextToken());
			}
			
			// tree 값 설정
			initMin(1, 1, N, 1, N);
			initMax(1, 1, N, 1, N);

			// 쿼리(a,b)에 해당하는 min, max값 출력
			for (int i = 1; i <= M; ++i) {
				st = new StringTokenizer(br.readLine());
				int a = Integer.parseInt(st.nextToken());
				int b = Integer.parseInt(st.nextToken());
				bw.write(getMin(1, 1, N, a, b) + " " + getMax(1, 1, N, a, b) + "\n");
			}
			
			bw.flush();
			bw.close();
			br.close();
			
	}

	public static int initMin(int index, int start, int end, int left, int right) {
		if(end<left || start>right) return Integer.MAX_VALUE;
		if(start==end) {
			return treeMin[index] = input[start];
		}
		int mid = (start+end)/2;
		treeMin[index*2] = initMin(index*2,start,mid,left,right);
		treeMin[index*2+1] = initMin(index*2+1,mid+1,end,left,right);
		return treeMin[index] = Math.min(treeMin[index*2], treeMin[index*2+1]);
	}

	public static int initMax(int index, int start, int end, int left, int right) {
		if(end<left || start>right) return 0;
		if(start==end) {
			return treeMax[index] = input[start];
		}
		int mid = (start+end)/2;
		treeMax[index*2] = initMax(index*2,start,mid,left,right);
		treeMax[index*2+1] = initMax(index*2+1,mid+1,end,left,right);
		return treeMax[index] = Math.max(treeMax[index*2], treeMax[index*2+1]);
	}

	public static int getMin(int index, int start, int end, int left, int right) {
		if(end<left || start>right) return Integer.MAX_VALUE;
		if(left<=start && end<=right) {
			return treeMin[index];
		}
		int mid = (start+end)/2;
		return Math.min(getMin(index*2,start,mid,left,right),getMin(index*2+1,mid+1,end,left,right));
	}

	public static int getMax(int index, int start, int end, int left, int right) {
		if(end<left || start>right) return 0;
		if(left<=start && end<=right) {
			return treeMax[index];
		}
		int mid = (start+end)/2;
		return Math.max(getMax(index*2,start,mid,left,right),getMax(index*2+1,mid+1,end,left,right));
	}

}