import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {
	
	public static void main (String[] arg) throws IOException{
		
		//System.setIn(new FileInputStream("src/BJ_3392.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		PriorityQueue<Node> pq = new PriorityQueue<>();

		int yMax = 30000; // Y축 최대값
		int N = Integer.parseInt(st.nextToken());
		int[] tree = new int[yMax*4];
		
		for(int i=1; i<=N; i++){
			
			st = new StringTokenizer(br.readLine());
			
			int x1 = Integer.parseInt(st.nextToken());
			int y1 = Integer.parseInt(st.nextToken());
			int x2 = Integer.parseInt(st.nextToken());
			int y2 = Integer.parseInt(st.nextToken());
			
			pq.offer(new Node(x1,y1,y2,1));  // 사각형의 시작하는 지점이므로 node.mode=1 
			pq.offer(new Node(x2,y1,y2,-1)); // 사각형의 종료하는 지점이므로 node.mode=-1
			
		}
		
		long Answer=0;
		int preX=0;
		int preYLine=0;
		
		while(!pq.isEmpty()){
			
			Node node = pq.poll();
			
			// 현재 계산되는 사각형 : (이전 y축 길이값)과 (이동한 x축 길이값)과의 곱으로 계산
			Answer += (preYLine*(node.x-preX));			
			
			// 현재 x축에서의 y축 길이값을 계산 (사각형의 시작은 node.mode=1, 사각형의 종료는 node.mode=-1)
			update(tree, 1, 1, yMax, node.ys+1, node.ye, node.mode);
			
			// 이전 변수값 처리
			preX = node.x;
			preYLine = tree[1];
			
		}

		bw.write(Answer+"\n");

		bw.flush();
		bw.close();
		br.close();
		
	}
	
	static int update(int[] tree, int index, int s, int e, int left, int right, int value){
		if(s>right || e<left){
			if(s==e) return tree[index]>0?1:0; // Y축 길이에 사용되느냐 안되느냐의 문제이므로, 값이 존재하면 1 아니면 0을 return 
			return tree[index];
		}
		if(s==e){
			tree[index] += value;
			return tree[index]>0?1:0; // Y축 길이에 사용되느냐 안되느냐의 문제이므로, 값이 존재하면 1 아니면 0을 return 
		}		
		return tree[index] = update(tree, index*2, s, (s+e)/2, left, right, value)
				           + update(tree, index*2+1, (s+e)/2+1, e, left, right, value);
	}

	static class Node implements Comparable<Node>{

		int x,ys,ye,mode;
		
		Node(int x, int ys, int ye, int mode){
			this.x=x;
			this.ys=ys;
			this.ye=ye;
			this.mode=mode;
		}
		
		@Override
		public int compareTo(Node o) {
			// TODO Auto-generated method stub
			if(this.x==o.x) return this.ys-o.ys;
			return this.x-o.x;
		} 
	}
}
