package kr.re.dev.DroidHammer.Sync;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
 


public class TargetMap {
	
	private HashMap<TargetRef, WorkerGroup> mWorkerGroupMap = new HashMap<TargetRef, WorkerGroup>();
	private static TargetMap mThis;
	
	private class TargetRef {
		
		 WeakReference<Object> weakReference;
		 public TargetRef(Object target) {
			 weakReference = new WeakReference<Object>(target);
		 };
		 public void clear() {
			 if(weakReference != null) weakReference.clear();
		 }
		 public boolean isEmpty() {
			 if(weakReference == null || weakReference.get() == null) return true;
			 return false;
		 }		 
 		 @Override
		 public boolean equals(Object o) {
			 if(o == null || !(o instanceof TargetRef)) return false;
			 TargetRef param = (TargetRef) o;
			 if(param.weakReference.get() == null || weakReference.get() == null) return false; 
			 if(param.weakReference.get() == weakReference.get()) {
				 return true;
			 }
			return false;
		}
	}
	
	private TargetMap() {}
	
	protected static TargetMap getInstance() {
		if(mThis == null) {
			mThis = new TargetMap();
		}
		return mThis;
	}
	
	
	
	/**
	 * WorkerGroup 을 반환한다. 
	 * @param target 
	 * @return
	 */
	protected WorkerGroup getWorkerGroup(Object target) {
		emptyGabage();
		WorkerGroup workerGroup =  addWorkerGroupIfNeed(target);
		if(workerGroup != null) return workerGroup;
		emptyGabage();
		TargetRef targetRef = new TargetRef(target);
		workerGroup =  mWorkerGroupMap.get(targetRef);
		targetRef.clear();
		return workerGroup;
	}
	
	
	protected WeakReference<Object> getTargetRef(Object target) {
		emptyGabage();
		Set<TargetRef> refSet = mWorkerGroupMap.keySet();
		Iterator<TargetRef> itor = refSet.iterator();
		while(itor.hasNext()) {
			TargetRef targetRef = itor.next();
			if(target == targetRef.weakReference.get()) {
				return targetRef.weakReference;
			}
		}
		return null;
	}
	
	
	/**
	 * 만약 target 이 등록되지 않았다면 target 을 등록하고, 그것을 key값으로 workerGroup 을 value 로 넣는다.
	 * @param target 타킷
	 * @return 생성된 wokerGroup 의 인스턴스. 만약 이미 target 이 등록되어 있다면 자연스레 workerGroup 을 생성하지 않을 것이고, null 을 반환하게 된다.
	 */
	private WorkerGroup addWorkerGroupIfNeed(Object target) {
		TargetRef targetRef = new TargetRef(target);
		if(!mWorkerGroupMap.containsKey(targetRef)) {
			WorkerGroup workerGroup = WorkerGroup.create(target);
			mWorkerGroupMap.put(targetRef, workerGroup);
			return workerGroup;
		}
		return null;
	}
	
	/**
	 * 메모리에서 해지된(가비지 컬렉트가 끝난)된 target 을 정리해준다.
	 * @return
	 */
	private boolean emptyGabage() {
		Set<Entry<TargetRef, WorkerGroup>> targetRefSet =  mWorkerGroupMap.entrySet();
		Iterator<Entry<TargetRef, WorkerGroup>> iter =  targetRefSet.iterator();
		while(iter.hasNext()) {
			Entry<TargetRef, WorkerGroup> entry = iter.next();
			if(entry.getKey() == null || entry.getKey().isEmpty()) {
				iter.remove();
				return true;
			}
		}
		return false;
	}
	
	
}
