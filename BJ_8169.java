import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

//방정보 Class
class Room {
	int index; // 방위치

	ArrayList<Integer> arrLinkRooms = new ArrayList<Integer>(); //연결된 방 정보
	
	public Room(int index)
	{
		this.index = index;
	}
	public void NextPoint( int next) {
		
		
		if(!this.arrLinkRooms.contains(next))
			this.arrLinkRooms.add(next);
	}

}

public class Main {

	static int N, S,K , result; 
	static ArrayList<Room> listRooms = new ArrayList<Room>();
	static long LimitCnt[][];   //소화기 처리 제한 저장
	static long CurentCnt[][];  //궁전의 처리 갯수 누적 저장
	public static void main(String[] args) throws IOException {
        
		// TODO Auto-generated method stub
		//System.setIn(new FileInputStream("src/BJ_8169.txt"));
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.valueOf(st.nextToken());
		S = Integer.valueOf(st.nextToken());
		K = Integer.valueOf(st.nextToken());
		
		LimitCnt = new long[N+1][K+2];  //소화기 사용 limit 갯수
		CurentCnt = new long[N+1][K+2]; //현재 처리 가능한 갯수
		
		for (int j = 0; j <= N; j++) {
			listRooms.add(new Room(j));
		}
		
		for (int j = 0; j < N-1; j++) {
			st = new StringTokenizer(br.readLine());
    		int start = Integer.parseInt(st.nextToken());
    		int end = Integer.parseInt(st.nextToken());
    		
    		listRooms.get(start).NextPoint(end);
    		listRooms.get(end).NextPoint(start);
		}
		
		dfs(1, -1);
        
		long h = 0, x;
		//출발 room에 대한 남아 있는 1번 궁전의 값을 계산  
		
	    for (int i = K+1; i > 0; i--) {
	        h += LimitCnt[1][i];//사용된 Room 수
	        if (h < CurentCnt[1][i]) {
	            x = (CurentCnt[1][i] - h + S - 1) / S;
	            result += x; //횟수를 더한다
	            h += x * S;
	        }
	        h -= CurentCnt[1][i]; //소화기 사용에 대한 제거
	    }
	    System.out.println(result);

	}
	
	public static void dfs(int index, int preIndex)//현재 궁전, 전 궁전번호 
	{
		Room r =  listRooms.get(index);
		//System.out.println("index: " + index);
		//연결된 정보만큼 체크
		for (int i = 0; i < r.arrLinkRooms.size(); i++) {
			
			int iNext = r.arrLinkRooms.get(i);//다음 간선
			if(iNext != preIndex)//전 궁전으로 돌아가면 안됨
			{
				dfs(iNext, index);	
				//재귀 밑의 여기 부분은 DFS로 한쪽방향 끝까지 도달되고 되돌아 오면서 계산되어 지겠죠..
				//아래에서 누적합을 계산하는 이유는 연결정점마다 도는 중에 계산해야되기 때문입니다.
				for (int j = 1; j <= K; j++) { //소화기 커버 가능 방역영역
					//System.out.println(LimitCnt[index][j] + " + " + LimitCnt[iNext][j + 1] );
					LimitCnt[index][j] += LimitCnt[iNext][j + 1];  // 이전 소화기 사용 누적값을 현재에 더한다
					//System.out.println(CurentCnt[index][j + 1] + " + " + CurentCnt[iNext][j]);
					CurentCnt[index][j + 1] += CurentCnt[iNext][j]; //다음 정점의 값을 현재누적값에 더한다
					//printMap(CurentCnt, "CurentCnt");
				    //printMap(LimitCnt, "LimitCnt");
				
				}
			}
		}
		//System.out.println("index: " + index);
		//dfs의 맨 끝에서 여기가 먼저 타겠죠...(왜 아래 위치에서 계산되어지는지 고민해보며 소스를 이해하면 좋을듯 합니다.)
		CurentCnt[index][1] = 1;//현재 궁전 1로 처리
		long x;
		//System.out.println("k+1: " + CurentCnt[index][K+1]);
	    x = (CurentCnt[index][K+1] + S - 1) / S; //1개가 있더라도 소개기를 사용한다
	    LimitCnt[index][K+1] = x * S; ///현재 Room 누적처리 갯수를 K+1에 넣는다,누적합(K+1)으로 소화기 사용(아래 MIN시 현재값과 누적값이 있는경우 -처리)
	    //System.out.println("x:" + x);
	    //printMap(CurentCnt, "CurentCnt");
	    //printMap(LimitCnt, "LimitCnt");
	    
	    result += x; //소화기 개수 추가
	    
	    //현재 값에 대한 소화기 사용하여 값을 빼준다
	    for (int i = 1; i <= K+1; i++) {
	    	x = Math.min(LimitCnt[index][i], CurentCnt[index][i]); //현재가지고 있는 궁전갯수와 소화기 갯수중 최소값 
	    	LimitCnt[index][i] -= x; 
	    	CurentCnt[index][i] -= x;
	    }
	    //이전 소화기 사용이 남아 있다면 그 값을 빼준다  
	    for (int i = 1; i <= K; i++) {
	    	x = Math.min(LimitCnt[index][i + 1], CurentCnt[index][i]);//현재 궁전 갯수와 이전 소화기 사용후 남은갯수중 최소값 
	    	LimitCnt[index][i + 1] -= x;
	    	CurentCnt[index][i] -= x;
	    }
	    //printMap(CurentCnt, "CurentCnt");
	    //printMap(LimitCnt, "LimitCnt");
	}
	
	//출력 소스
	public static void printMap(long[][] map, String sName)
	{
		System.out.println(sName);
		for(int i=1; i<= K+1;i++)
		{
			for(int j=1; j<= N;j++)
			{
				System.out.printf("%d ",map[j][i]);	
			}
			System.out.println();
		}
	
	}

}
