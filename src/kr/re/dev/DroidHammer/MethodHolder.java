package kr.re.dev.DroidHammer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;



public class MethodHolder {
	    protected  Method mMethod;
		protected String name = "";
		protected Class<?> mDeclaringClassType = Object.class;
		protected Class<?>[] mListParamType = new Class[0];
		protected Object[] mArgs;
		

		public  MethodHolder(Class<?> declaringType, String name,  Class<?>[] paramsType) {
			this(name, paramsType);
			mDeclaringClassType = declaringType;
		}
		
		public  MethodHolder(Method method) {
			this.mMethod = method;
			this.name = method.getName();
			this.mListParamType = method.getParameterTypes();
			if(mListParamType == null) mListParamType = new Class[0];
			this.mArgs = new Object[mListParamType.length];
			this.mDeclaringClassType = method.getDeclaringClass();
			defaultParamsToObjectType(mListParamType);
			createEmptyArgs(mArgs,mListParamType);
		}
		
		public  MethodHolder(String name,  Class<?>[] paramsType) {
			this.name = name;
			this.mListParamType = paramsType;
			if(mListParamType == null) mListParamType = new Class[0];
			defaultParamsToObjectType(mListParamType);
		}
		
		public Method getMethod() {
			return mMethod;
		}
		
		
		public void invoke(Object target) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
			if(!mMethod.isAccessible()) mMethod.setAccessible(true);
			mMethod.invoke(target, mArgs);
		}
		
		public Object invokeGetObject(Object target) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
			if(!mMethod.isAccessible()) mMethod.setAccessible(true);
			return mMethod.invoke(target, mArgs);
		}
		
		
		public void adaptArguments(Object[] args) {
			adapteArguments(mListParamType, mArgs, args);
		}
			
		private void adapteArguments(Class<?>[] targetArgsParamTypes, Object[] targetArgs,Object[] intoArgs) {
			createEmptyArgs(targetArgs, targetArgsParamTypes);
			for(int i = 0, n = targetArgs.length; i < n; ++i) {
				for(int j = 0;j < intoArgs.length; ++j) {
					if(targetArgsParamTypes[i].isInstance(intoArgs[j])) {
						targetArgs[i] = intoArgs[j];
					}
				}
			}
		}
		
		
		
		private void createEmptyArgs(Object[] args, Class<?>[] types) {
			for(int i = 0, n = args.length; i < n; ++i) {
				args[i] = createEmptyObject(types[i]); 
			}
		}
		
		private Object createEmptyObject( Class<?> type) {
			if(type.isAssignableFrom(boolean.class) || type.isAssignableFrom(Boolean.class)) return Boolean.valueOf(false);
			else if(type.isAssignableFrom(byte.class) || type.isAssignableFrom(Byte.class) ) return  Byte.valueOf((byte) 0);
			else if(type.isAssignableFrom(char.class) || type.isAssignableFrom(Character.class) ) return Character.valueOf('\0');
			else if(type.isAssignableFrom(long.class) || type.isAssignableFrom(Long.class)) return Long.valueOf(0);
			else if(type.isAssignableFrom(float.class) || type.isAssignableFrom(Float.class)) return Float.valueOf(0.0f);
			else if(type.isAssignableFrom(int.class) || type.isAssignableFrom(Integer.class) ) return Integer.valueOf(0);
			else if(type.isAssignableFrom(double.class) || type.isAssignableFrom(Double.class)  ) return Double.valueOf(0.0f);
			else if(type.isAssignableFrom(short.class) || type.isAssignableFrom(Short.class) ) return new Short((short) 0);
			return null;
		}
		
		private void defaultParamsToObjectType( Class<?>[] params) {
			for(int i = 0, n = params.length; i < n; ++i) {
				params[i] = defaultToObjectType(params[i]);
			}
		}
		
		private  Class<?> defaultToObjectType(Class<?> classType) {
			if(classType.equals(int.class)) classType = Integer.class;
			else if(classType.equals(float.class)) classType = Float.class;
			else if(classType.equals(boolean.class))classType = Boolean.class;
			else if(classType.equals(byte.class))classType = Byte.class;
			else if(classType.equals(short.class))classType = Short.class;
			else if(classType.equals(long.class))classType = Long.class;
			else if(classType.equals(double.class))classType = Double.class;
			else if(classType.equals(char.class))classType = Character.class;
			return classType;
		}
		
		/**
		 * 파라미터 타입까지 비교하여 같은 MethodHolder 인지 확인한다.
		 * @param o
		 * @return
		 */
		public boolean equalsIncludExcludesParams(Object o) {
			if(super.equals(o)) return true;
			if(o instanceof MethodHolder) {
				MethodHolder compareObj = (MethodHolder)o;
				if(!compareObj.name.equals(this.name)) return false;
				if(mListParamType.length != compareObj.mListParamType.length) return false;
				for(int i = 0, n = mListParamType.length; i <  n; ++i)  
					if(!mListParamType[i].equals(compareObj.mListParamType[i]) && 
							!mListParamType[i].isAssignableFrom(compareObj.mListParamType[i])) return false;
			}
			return true;
		}
		
		
		@Override
		public boolean equals(Object o) {
			if(super.equals(o)) return true;
			if(o instanceof MethodHolder) {
				MethodHolder compareObj = (MethodHolder)o;
				if(!compareObj.name.equals(this.name)) return false;
				if(mListParamType.length != compareObj.mListParamType.length) return false;
				for(int i = 0, n = mListParamType.length; i <  n; ++i)  
					if(!mListParamType[i].equals(compareObj.mListParamType[i])) return false;
			}
			return true;
		}
		
}
