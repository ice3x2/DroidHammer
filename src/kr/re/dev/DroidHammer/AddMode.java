package kr.re.dev.DroidHammer;

/**
 * UiThread 또는 Background 의 Worker 메소드 인스턴스가 작업 대기큐에 추가될 위치에 대한 옵션.
 * {@value #Last} : 항상 작업 대기큐 마지막에 추가된다.
 * {@link #First} : 항상 작업 대기큐 첫 번째에 추가된다.
 * {@value #Default} : 기본값.  
 * @author ice3x2
 */
public enum AddMode {
	Last, Default, First
}