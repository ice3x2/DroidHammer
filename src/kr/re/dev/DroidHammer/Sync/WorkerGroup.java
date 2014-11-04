package kr.re.dev.DroidHammer.Sync;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;

import kr.re.dev.DroidHammer.MethodHolder;
import android.util.Log;


public class WorkerGroup  {

	private LinkedList<Worker> mWorkerList = new LinkedList<Worker>();

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



	public Worker findWorker(StackTraceElement[] stackTraceElements,Class<?> classType,Object... arguments) {
		for(int i = 0;i < stackTraceElements.length;++i) {
			if(i > 10) return null;
			StackTraceElement stackElement =  stackTraceElements[i];
			String stackClassName =  stackElement.getClassName();
			Class<?> compareClassType = classType;
			boolean isContinue = true;
			while(!compareClassType.equals(Object.class)) {
				Log.d("debug",compareClassType.getName() + " == " +  stackClassName);
				if(stackClassName.equals(compareClassType.getName())) {
					isContinue = false;
					break;
				}
				else compareClassType = compareClassType.getSuperclass();
			}
			Log.d("debug","-----------------------------------------------");
			if(isContinue) {
				continue;
			}
			Log.d("debug","OK");

			
			Iterator<Worker> iterWorker = mWorkerList.iterator();
			Method[] methods = compareClassType.getDeclaredMethods();
			while(iterWorker.hasNext()) {
				Worker worker = iterWorker.next();
				MethodHolder holder =  worker.getMethodHolder();
				if((arguments != null && holder.getParamCount() == arguments.length) && 
					(classType.isAssignableFrom(holder.getDeclaringClassType()) || 
								holder.getDeclaringClassType().isAssignableFrom(classType))
								&& holder.getMethod().getName().equals(stackElement.getMethodName())) {
					for(Method method : methods) {
						MethodHolder eqMethodHolder = new MethodHolder(method);
						if(holder.equals(eqMethodHolder)) {
							Class<?>[] types = eqMethodHolder.getParamsTypes();
							boolean sameType = true;
							for(int t = 0,tn = types.length; t < tn; ++t) {
								if(!types[t].equals(arguments[t].getClass())) {
									sameType = false;
									break;
								}
							}
							if(sameType) return worker;
						}
					}
				}
			}
			iterWorker = mWorkerList.iterator();
			while(iterWorker.hasNext()) {
				Worker worker = iterWorker.next();
				MethodHolder holder =  worker.getMethodHolder();
				if((arguments != null && holder.getParamCount() == arguments.length) && 
						(classType.isAssignableFrom(holder.getDeclaringClassType()) || 
								holder.getDeclaringClassType().isAssignableFrom(classType))
								&& holder.getMethod().getName().equals(stackElement.getMethodName())) {
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
