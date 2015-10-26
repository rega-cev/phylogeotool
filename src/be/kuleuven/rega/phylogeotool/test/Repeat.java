package be.kuleuven.rega.phylogeotool.test;
/**
 * Created on 11/02/11
 */ 

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author kreich
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Repeat {

	int value();
	
}
