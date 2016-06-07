package net.hawkengine.ws;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class Command {
	private Object object;
	private String methodName;
	private List<Object> methodArgs;

	public Command(Object object, String methodName, List<Object> methodArgs) {
		this.object = object;
		this.methodName = methodName;
		this.methodArgs = methodArgs;
	}

	public Object execute() {
		Class objectClass = object.getClass();
		Method method;
		try {
			if (this.methodArgs == null || this.methodArgs.size() == 0) {
				method = objectClass.getMethod(this.methodName);
				return method.invoke(this.object);
			} else {
				Class[] argTypes = new Class[this.methodArgs.size()];
				for (int i = 0; i < this.methodArgs.size(); i++) {
					argTypes[i] = this.methodArgs.get(i).getClass();
				}

				method = objectClass.getMethod(this.methodName, argTypes);
				return method.invoke(this.object, this.methodArgs.toArray());
			}
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

		return null;
	}
}
