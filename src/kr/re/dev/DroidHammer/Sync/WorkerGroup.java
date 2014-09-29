package kr.re.dev.DroidHammer.Sync;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;

import android.os.Message;

import kr.re.dev.DroidHammer.MethodHolder;


public class WorkerGroup  {
	
	private LinkedList<Worker> mWorkerList = new LinkedList<Worker>();
	/**
	 * Object 바로 전 까지의 조상 클래스들을 갖고 있는 리스트.
	 * 기준이 되는 클래스 -------- 상속받은 클래스 ---- .... 
	 */
	private LinkedList<Class<?>>  mTypePedigreeList = new LinkedList<>();
	
	/**
	 * WorkerGroup 을 생성한다. 인자값으로 받은 target 의 인스턴스를 Worker 들을 생성하기 위해서만 사용하고 내부적으로 갖고있지 않는다. (not strong reference)
	 * @param target object. 
	 * @return 생성된 workerGroup.
	 */
	protected static WorkerGroup create(Object target) {
		return new WorkerGroup(target);
	}
	
	private WorkerGroup(Object target) {
		mapping(target);
	}
	
	private void mapping(Object target) {
		Class<?> classType = target.getClass();
		while(!classType.equals(Object.class)) {
			mTypePedigreeList.add(classType);
			Method[] methods =  classType.getDeclaredMethods();
			for(Method method : methods) {
				Worker worker = Worker.factory(method);
				if(worker != null) {
					mWorkerList.add(worker);
				}
			}
			classType = classType.getSuperclass();
		}
	}
	
	
	
	public Worker findWorker(StackTraceElement[] stackTraceElements,Class<?> classType,Object... object) {
		for(int i = 0;i < stackTraceElements.length;++i) {
			 if(i > 10) return null;
			 StackTraceElement stackElement =  stackTraceElements[i];
			 if(!stackElement.getClassName().equals(classType.getName())) continue;
			 Iterator<Worker> iterWorker = mWorkerList.iterator();
			 while(iterWorker.hasNext()) {
				 Worker worker = iterWorker.next();
				 MethodHolder holder =  worker.getMethodHolder();
				 if((object != null && holder.getParamCount() == object.length) && 
						 (classType.isAssignableFrom(holder.getDeclaringClassType()) || 
					  holder.getDeclaringClassType().isAssignableFrom(classType))
					  && holder.getMethod().getName().equals(stackElement.getMethodName())) {
					Method[] methods = classType.getDeclaredMethods();
					for(Method method : methods) {
						MethodHolder eqMethodHolder = new MethodHolder(method);
						if(holder.equalsAssignableParams(eqMethodHolder)) {
							return worker;
						}
					}
				 }
			 }
		}
		return null;
	}
	
	
}
