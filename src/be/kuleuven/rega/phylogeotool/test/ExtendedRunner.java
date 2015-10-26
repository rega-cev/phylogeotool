package be.kuleuven.rega.phylogeotool.test;
/**
 * Created on 11/02/11
 */

import org.junit.Ignore;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * @author kreich
 */
public class ExtendedRunner extends BlockJUnit4ClassRunner {

	public ExtendedRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}

	/**
	 * In case the method is annotated with Repeat, do hierarchic description.
	 * Otherwise, pass the handle to the super. 
	 */
	@Override
	protected Description describeChild(FrameworkMethod method) {
		if (method.getAnnotation(Repeat.class) != null &&
				method.getAnnotation(Ignore.class) == null) {
			return describeRepeatTest(method);
		}
		return super.describeChild(method);
	}

	private Description describeRepeatTest(FrameworkMethod method) {
		int times = method.getAnnotation(Repeat.class).value();

		Description description = Description.createSuiteDescription(
				testName(method) + " [" + times + " times]",
				method.getAnnotations());

		for (int i = 1; i <= times; i++) {
			description.addChild(Description.createTestDescription(
					getTestClass().getJavaClass(),
					"[" + i + "] " + testName(method)));
		}
		return description;
	}

	/**
	 * In case the method is annotated with {@link Repeat} do specific handle.
	 * Otherwise, pass the handle to the super.
	 */
	@Override
	protected void runChild(final FrameworkMethod method, RunNotifier notifier) {
		Description description = describeChild(method);
		
		if (method.getAnnotation(Repeat.class) != null &&
				method.getAnnotation(Ignore.class) == null) {
			runRepeatedly(methodBlock(method), description, notifier);
		}
		super.runChild(method, notifier);
	}

	private void runRepeatedly(Statement statement, Description description,
			RunNotifier notifier) {
		for (Description desc : description.getChildren()) {
			runLeaf(statement, desc, notifier);
		}
	}
	
}
