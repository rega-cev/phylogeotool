package be.kuleuven.rega.phylogeotool.test;
/**
 * Created on 11/02/11
 */ 

import java.lang.annotation.*;

/**
 * @author kreich
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Repeat {

	int value();
	
}
