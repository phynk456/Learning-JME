package core.input;

import org.jspecify.annotations.NullMarked;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@NullMarked
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InputHandler
{
    String keyName();
}
